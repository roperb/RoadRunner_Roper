
package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

//The thing to notice in this OpMode is how short it is.  Most of the logic is happening in the hardware class that is stored
//in the variable "robot".  Note how we just construct a robot object using the Hardware Class, pass it some variables from the opmode
//to be used in the hardware class, and then call methods from the hardware class.  There are also some values that are being called
//from the Drive Constants class.  This kind of process of splitting up parts of the code into different files / classes is called
//"Encapsulation" and it helps keep the logic separated into distinct bites.  When writing your own code, think about inputs and outputs.
//What information needs to come in and what needs to go out, and then you can write a class or a method that encapsulates that information
//Keeping all of the logic locked inside.

@TeleOp(name="Two Wheel Drive Using Hardware Class", group="Simple Examples")
@Disabled
public class TwoWheelDriveUsingHardwareClass extends OpMode
{
    ElapsedTime clock = new ElapsedTime();
    TwoWheelDriveHardware robot;//new TwoWheelDriveHardware(telemetry, hardwareMap);

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot = new TwoWheelDriveHardware(telemetry,hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        clock.reset();
    }

    @Override
    public void loop() {
        double drive = -gamepad1.left_stick_y;
        double turn  =  gamepad1.right_stick_x;
        boolean inSlowMode = gamepad1.right_bumper;

        robot.driveRobot(drive,turn, inSlowMode);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + clock.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", robot.getLeftPower(), robot.getRightPower());
    }

    @Override
    public void stop() {
    }

}
