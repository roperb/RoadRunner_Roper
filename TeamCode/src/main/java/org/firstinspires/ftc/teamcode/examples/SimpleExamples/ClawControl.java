
package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Config
@TeleOp(name="Claw Control", group="Simple Examples")
@Disabled
public class ClawControl extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Servo leftClaw = null;
    private Servo rightClaw = null;

    public static double leftOpen = 1;
    public static double rightOpen = 1;
    public static double leftClosed = 0;
    public static double rightClosed = 0;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addData("Status", "Initialized");

        leftClaw  = hardwareMap.get(Servo.class, "leftclaw");
        rightClaw = hardwareMap.get(Servo.class, "rightclaw");

        leftClaw.setDirection(Servo.Direction.REVERSE);
        rightClaw.setDirection(Servo.Direction.FORWARD);
        leftClaw.setPosition(leftOpen);
        rightClaw.setPosition(rightOpen);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        if(gamepad1.a){
            rightClaw.setPosition(rightOpen);
        }
        else if (gamepad1.b){
            rightClaw.setPosition(rightClosed);
        }

        if (gamepad1.x) {
            leftClaw.setPosition(leftOpen);
        }
        else if (gamepad1.y){
            leftClaw.setPosition(leftClosed);
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    void openClaw(){
        leftClaw.setPosition(leftOpen);
        rightClaw.setPosition(rightOpen);
    }
    void closeClaw(){
        leftClaw.setPosition(leftClosed);
        rightClaw.setPosition(rightClosed);
    }

}
