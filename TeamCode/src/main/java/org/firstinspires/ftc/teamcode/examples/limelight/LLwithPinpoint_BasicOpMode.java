package org.firstinspires.ftc.teamcode.examples.limelight;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.util.List;

/*
 * The goal of this code is to use the limelight and the pinpoint device to look at
 *
 */
@TeleOp (name = "Limelight and Pinpoint Info OpMode")
public class LLwithPinpoint_BasicOpMode extends OpMode {

    public Limelight3A limelight = null;
    GoBildaPinpointDriver pinpoint = null;
    boolean toggle = false;

    @Override
    public void init() {
        //multiple telemetry
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        //limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();

        //pinpoint is used for the imu
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        configurePinpoint();

        // Set the location of the robot - this should be the place you are starting the robot from
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
    }

    @Override
    public void loop() {
        //toggles using MegaTag 1 or 2
        if (gamepad1.a) {
            toggle = true;
        }
        if (gamepad1.b) {
            toggle = false;
        }

        telemetry.addData("Megatag 1", toggle);
        telemetry.addData("Megatag 2", !toggle);
        telemetry.addLine("Press X to reset the position");
        if (gamepad1.x) {
            // You could use readings from April Tags here to give a new known position to the pinpoint
            pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
        }

        pinpoint.update();
        Pose2D pose2D = pinpoint.getPosition();

        telemetry.addData("X coordinate (IN)", pose2D.getX(DistanceUnit.INCH));
        telemetry.addData("Y coordinate (IN)", pose2D.getY(DistanceUnit.INCH));
        telemetry.addData("Heading angle (DEGREES)", pose2D.getHeading(AngleUnit.DEGREES));


        double robotYaw = pose2D.getHeading(AngleUnit.DEGREES);
        limelight.updateRobotOrientation(robotYaw);

        Pose3D botpose = null;

        LLResult result = limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                telemetry.addData("stalelness", result.getStaleness());
                if (toggle) {
                    botpose = result.getBotpose();
                    telemetry.addData("Target X, tx", result.getTx());
                    telemetry.addData("Target Y, ty", result.getTy());
                    telemetry.addData("Target Area, ta", result.getTa());
                    telemetry.addData("Botpose", botpose.toString());
                } else {
                    botpose = result.getBotpose_MT2();
                    telemetry.addData("Target X, tx", result.getTx());
                    telemetry.addData("Target Y, ty", result.getTy());
                    telemetry.addData("Target Area, ta", result.getTa());
                    telemetry.addData("Botpose", botpose.toString());
                    // Use botpose data
                }
                List<LLResultTypes.FiducialResult> aprilTags = result.getFiducialResults();
                if (!aprilTags.isEmpty()){
                    for (LLResultTypes.FiducialResult aprilTag : aprilTags){
                        int id = aprilTag.getFiducialId(); // The ID number of the fiducial
                        double x = aprilTag.getTargetXDegrees(); // Where it is (left-right)
                        double y = aprilTag.getTargetYDegrees(); // Where it is (up-down)
                        double dist_x = aprilTag.getRobotPoseTargetSpace().getPosition().x;
                        double dist_y = aprilTag.getRobotPoseTargetSpace().getPosition().y;
                        double dist_yaw = aprilTag.getRobotPoseTargetSpace().getOrientation().getYaw();
                        telemetry.addLine("AprilTag Relative to Robot");
                        telemetry.addData("Fiducial " + id, "is " + dist_x + " meters sideways from the robot");
                        telemetry.addData("Fiducial " + id, "is " + dist_y + " meters away from the robot");
                        telemetry.addData("Fiducial " + id, "is " + dist_yaw + " angled from the robot");
                        telemetry.addLine("Testing Larger Outputs from April Tags");
                        telemetry.addData("Robot Position Target Space", aprilTag.getRobotPoseTargetSpace());
                        telemetry.addData("Camera Position Target Space", aprilTag.getCameraPoseTargetSpace());
                        telemetry.addData("Robot Position Field Space", aprilTag.getRobotPoseFieldSpace());
                    }
                }
            }
        } else {
            telemetry.addData("Limelight", "No Targets");
        }


        if (botpose != null && gamepad1.y){
            pinpoint.setPosition(new Pose2D(
                    DistanceUnit.INCH, //TODO is this the right unit?
                    botpose.getPosition().x,
                    botpose.getPosition().y,
                    AngleUnit.DEGREES,
                    botpose.getOrientation().getYaw(AngleUnit.DEGREES)));
        }
        //no need to run telemetry.update()?

    }

    public void configurePinpoint(){
        /*
         *  Set the odometry pod positions relative to the point that you want the position to be measured from.
         *
         *  The X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is.
         *  Left of the center is a positive number, right of center is a negative number.
         *
         *  The Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is.
         *  Forward of center is a positive number, backwards is a negative number.
         */
        //TODO update these offsets

        pinpoint.setOffsets(-84.0, -168.0, DistanceUnit.MM); //these are tuned for 3110-0002-0001 Product Insight #1

        /*
         * Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
         * the goBILDA_SWINGARM_POD, or the goBILDA_4_BAR_POD.
         * If you're using another kind of odometry pod, uncomment setEncoderResolution and input the
         * number of ticks per unit of your odometry pod.  For example:
         *     pinpoint.setEncoderResolution(13.26291192, DistanceUnit.MM);
         */

        //TODO update this info
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        /*
         * Set the direction that each of the two odometry pods count. The X (forward) pod should
         * increase when you move the robot forward. And the Y (strafe) pod should increase when
         * you move the robot to the left.
         */

        //TODO update this info
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        /*
         * Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
         * The IMU will automatically calibrate when first powered on, but recalibrating before running
         * the robot is a good idea to ensure that the calibration is "good".
         * resetPosAndIMU will reset the position to 0,0,0 and also recalibrate the IMU.
         * This is recommended before you run your autonomous, as a bad initial calibration can cause
         * an incorrect starting value for x, y, and heading.
         */
        pinpoint.resetPosAndIMU();
    }
}
