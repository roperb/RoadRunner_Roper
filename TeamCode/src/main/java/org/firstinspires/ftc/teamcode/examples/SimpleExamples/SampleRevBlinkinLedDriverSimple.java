package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

/*
 * This OpMode demonstrates use of the REV Robotics Blinkin LED Driver.
 * MANUAL mode allows the user to manually change patterns using the
 * left and right bumpers of a gamepad.
 *
 * Configure the driver on a servo port, and name it "blinkin".
 *
 * Displays the first pattern upon init.
 */
@TeleOp(name="BlinkinExample")
@Disabled
public class SampleRevBlinkinLedDriverSimple extends OpMode {

    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

    ElapsedTime timer = new ElapsedTime();
    int DELAY_TIME = 200;


    @Override
    public void init()
    {
        //starts in rainbow
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        pattern = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
        blinkinLedDriver.setPattern(pattern);

        telemetry.addData("Pattern: ", pattern.toString());

    }

    @Override
    public void loop()
    {
        if (timer.milliseconds() > 200 && gamepad1.left_bumper){
            pattern = pattern.previous();
            timer.reset();
        }
        else if (timer.milliseconds() > 200 && gamepad1.right_bumper){
            pattern = pattern.next();
            timer.reset();
        }

        if (gamepad1.a){
            pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
        }
        if (gamepad1.b){
            pattern = RevBlinkinLedDriver.BlinkinPattern.BLUE;
        }
        
        telemetry.addData("Current Pattern", pattern.toString());

        blinkinLedDriver.setPattern(pattern);

    }



}
