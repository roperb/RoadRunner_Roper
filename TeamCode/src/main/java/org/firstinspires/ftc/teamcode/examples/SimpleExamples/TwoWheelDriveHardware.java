package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import static org.firstinspires.ftc.teamcode.examples.SimpleExamples.TwoWheelDriveConstants.MAX_SPEED;
import static org.firstinspires.ftc.teamcode.examples.SimpleExamples.TwoWheelDriveConstants.SLOW_MODE_SPEED;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class TwoWheelDriveHardware {
    //Note that there is a class called "TwoWheelDriveConstants that contain some values that are imported here
    //This allows us to store all of our constants in one convenient place.

    /* Declare OpMode members. */
    // For getting access to OpMode functionality.
    final Telemetry telemetry;
    final HardwareMap hardwareMap;

    // Define Motor and Servo objects  (Make them private so they can't be accessed externally)
    private final DcMotor leftMotor;
    private final DcMotor rightMotor;

    List<LynxModule> allHubs;

    // Define a constructor that allows objects common in OpMode to be used in this class.
    // OpMode have telemetry and hardwareMap built in, but other classes don't, so to have access to them
    // We need to have variables to hold onto that data.
    public TwoWheelDriveHardware(Telemetry telemetry, HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        allHubs = hardwareMap.getAll(LynxModule.class);
        leftMotor = hardwareMap.get(DcMotor.class, "left");
        rightMotor = hardwareMap.get(DcMotor.class, "right");

        //a specific piece of code used for "bulk reads".  Read gm0 for more info on Bulk Reads.
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        //Reverse one of the motors due to pointing in opposite directions
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        // If there are encoders connected, switch to RUN_USING_ENCODER mode for greater accuracy
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void driveRobot(double drive, double turn, boolean SLOW_MODE) {
        // Combine drive and turn for blended motion.
        double leftPower  = drive + turn;
        double rightPower = drive - turn;

        double SPEED_CAP = MAX_SPEED;
        if (SLOW_MODE) { // slow mode button for fine driving.
            SPEED_CAP = SLOW_MODE_SPEED;
        }
        leftPower = Range.clip(drive + turn, -SPEED_CAP, SPEED_CAP);
        rightPower = Range.clip(drive - turn, -SPEED_CAP, SPEED_CAP);

        setDrivePower(leftPower, rightPower);
    }

    public void setDrivePower(double leftPower, double rightPower) {
        // Output the values to the motor drives.
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }

    public double getLeftPower(){
        return leftMotor.getPower();
    }

    public double getRightPower(){
        return rightMotor.getPower();
    }
}
