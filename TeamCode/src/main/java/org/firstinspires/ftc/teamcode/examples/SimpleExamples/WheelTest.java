
package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This opmode is a simple test of the direction of the wheels for a 4 motor robot.
 */

@TeleOp(name="Wheel Test", group="Simple Examples")
//@Disabled
public class WheelTest extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime clock = new ElapsedTime();
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private DcMotor rightFront = null;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

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

        if (gamepad1.a){
            leftBack.setPower(1);
        }
        else if (gamepad1.b){
            rightBack.setPower(1);
        }
        else if (gamepad1.x){
            rightFront.setPower(1);
        }
        else if (gamepad1.y){
            leftFront.setPower(1);
        }
        else {
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
        }
        telemetry.addLine("Turn the controller 45 deg CCW.");
        telemetry.addLine("The buttons will now resemble the orientation");
        telemetry.addLine("of the motors if the robot faces away from you");
        telemetry.addLine("Y (Front Left) (Front Right) X");
        telemetry.addLine("A (Back Left)  (Back  Right) B");
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
