package org.firstinspires.ftc.teamcode.examples.limelight;

import static org.firstinspires.ftc.teamcode.examples.limelight.LLConstants.PIPELINE;
import static org.firstinspires.ftc.teamcode.examples.limelight.LLConstants.START_HEADING;
import static org.firstinspires.ftc.teamcode.examples.limelight.LLConstants.START_X;
import static org.firstinspires.ftc.teamcode.examples.limelight.LLConstants.START_Y;
import static org.firstinspires.ftc.teamcode.examples.limelight.LLConstants.kTurn;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.MecanumDrive;

import java.util.List;

@TeleOp (name = "Drive Control Align Limelight")
public class DriveControlLL extends OpMode {
    MecanumDrive drive = null;
    Limelight3A limelight = null;
    Pose2d startPose = new Pose2d(START_X,START_Y,START_HEADING);

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, startPose);
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        //merge Dashboard telemetry object and FTC_SDK telemetry object
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(PIPELINE);
        limelight.start();
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();
    }
    @Override
    public void init_loop(){
        drive.localizer.setPose(startPose); //allows you to update the pose during initloop in dashboard?
    }

    @Override
    public void loop() {
        //Limelight Telemetry from SensorLimelight3a.java
        LLStatus status = limelight.getStatus();
        telemetry.addData("Name", "%s",
                status.getName());
        telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                status.getTemp(), status.getCpu(),(int)status.getFps());
        telemetry.addData("Pipeline", "Index: %d, Type: %s",
                status.getPipelineIndex(), status.getPipelineType());

        LLResult result = limelight.getLatestResult();
        //Code to output info about detections
        if (result.isValid() && !gamepad1.a) {
            // Access general information
            Pose3D botpose = result.getBotpose();
            double captureLatency = result.getCaptureLatency();
            double targetingLatency = result.getTargetingLatency();
            double parseLatency = result.getParseLatency();
            telemetry.addData("LL Latency", captureLatency + targetingLatency);

            //results
            telemetry.addData("tx", result.getTx());
            telemetry.addData("txnc", result.getTxNC());
            telemetry.addData("ty", result.getTy());
            telemetry.addData("tync", result.getTyNC());
            telemetry.addData("Botpose", botpose.toString());

            // Access fiducial results
            List<LLResultTypes.FiducialResult> aprilTagsTelemetry = result.getFiducialResults();
            for (LLResultTypes.FiducialResult aprilTag : aprilTagsTelemetry) {
                telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f",
                        aprilTag.getFiducialId(),
                        aprilTag.getFamily(),
                        aprilTag.getTargetXDegrees(),
                        aprilTag.getTargetYDegrees()
                );
            }
        }
        else {
            telemetry.addData("Limelight", "No data available");
        }

        //DRIVING
        double d, s, t = 0;

        //sets powers based on limelight results when holding down button
        //display 2 different ways to show position relative to the target using RobotPoseTargetSpace
        //and tx, ty, ta.
        if (gamepad1.a && result.isValid()) {
            List<LLResultTypes.FiducialResult> aprilTags = result.getFiducialResults();
            //grabs first april tag - could make this more specific
            LLResultTypes.FiducialResult aprilTag = aprilTags.get(0);
            Pose3D robotRelativePose = aprilTag.getRobotPoseTargetSpace();
            double x = robotRelativePose.getPosition().x;
            double y = robotRelativePose.getPosition().y;
            double yaw = robotRelativePose.getOrientation().getYaw(AngleUnit.DEGREES);
            double tx = aprilTag.getTargetXDegrees();
            double ty = aprilTag.getTargetYDegrees();
            double ta = aprilTag.getTargetArea();
            telemetry.addData("bot x to target", x);
            telemetry.addData("bot y to target", y);
            telemetry.addData("bot yaw to target", yaw);
            telemetry.addData("target tx", tx);
            telemetry.addData("target ty", ty);
            telemetry.addData("target ta", ta);

            d = 0;
            s = 0;
            t = -tx*kTurn; //negative sign ok?
        }
        else { // sets powers based on controllers
            d = -gamepad1.left_stick_y;
            s = -gamepad1.left_stick_x;
            t = -gamepad1.right_stick_x;
        }

        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(d, s), t)
        );




    }
}
