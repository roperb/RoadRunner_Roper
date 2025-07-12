package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Demonstrates a PID controlled lifting mechanism.  Could be modified to be PIDF controller
 * https://www.ctrlaltftc.com/the-pid-controller source.
 * PIDF Example https://www.youtube.com/watch?v=E6H6Nqe6qJo - helps when there is gravity always acting down on something.
 */

@Config
@TeleOp(name = "Lift: PID Example", group = "Simple Examples")
//@Disabled
public class LiftPIDExample extends OpMode {

  private ElapsedTime timer = new ElapsedTime();
  private DcMotor lift;

  public static double Kp = 0; //.03
  public static double Kd = 0; //couldn't find good tuning for lift.
  public static double Ki = 0; //.001
  public static double Kf = 0; //not tested
  double lastError = 0;
  double integralSum = 0;
  public static int THRESHOLD = 0;
  public static int INTEGRALSUM_MAX = 0;


  public static int LOW_POSITION = 0;
  public static int HIGH_POSITION = 600;
  public static int targetPosition = LOW_POSITION;

  /**
   * This method will be called once, when the INIT button is pressed.
   */
  @Override
  public void init() {
    telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    lift = hardwareMap.get(DcMotor.class,"lift");
    lift.setDirection(DcMotorSimple.Direction.REVERSE);
    lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //likely not needed, but sets encoders to 0.
                                                          // Make sure lift actually at zero point before hitting INIT
    lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    lift.setPower(0);
    telemetry.addData("Status", "Initialized");
  }

  @Override
  public void init_loop() {
  }

  @Override
  public void start() {
    timer.reset();
  }

  @Override
  public void loop() {

    if (gamepad1.dpad_up){
      targetPosition = HIGH_POSITION;
    }
    if (gamepad1.dpad_down){
      targetPosition = LOW_POSITION;
    }
    int currentPosition = lift.getCurrentPosition();
    double output = LiftPIDController(targetPosition, currentPosition, THRESHOLD);

    //Data for output and graphing.  Graph targetPosition and currentPosition when tuning in Dashboard
    telemetry.addData("PID Output", output);
    telemetry.addData("Target Position", targetPosition);
    telemetry.addData("Lift Position", currentPosition);
    telemetry.addData("Error", targetPosition-currentPosition);
    telemetry.addData("integral sum", integralSum);

    //Sets the lift power to the output of the PID controller calculation.
    //The lift doesn't actually care how far away it is, it just needs to know how much power and what direction to spin.
    //The PID Controller uses the encoders to determine whether to spin clockwise or counterclockwise and by how much.
    lift.setPower(output);
  }

  @Override
  public void stop() {
  }

  double LiftPIDController(int target, int current, int threshold){

    int error = target - current; // determine the amount of error between where you want to be and where you are
    double derivative = (error-lastError)/timer.seconds();  // calculate how quickly the errors are changing
    integralSum+=error; //add up all of the errors

    //Prevent the integral sum of errors from getting arbitrarily high.  Helpful when tuning Ki term.
    if (integralSum > INTEGRALSUM_MAX){
      integralSum = INTEGRALSUM_MAX;
    }
    if (integralSum < -INTEGRALSUM_MAX){
      integralSum = -INTEGRALSUM_MAX;
    }
    lastError = error; //store the current error to be the previous error in the next loop;
    timer.reset();
    double output = Kp * error + Kd * derivative + Ki * integralSum;  //Calculates what the output should be based on the 3 calculated terms.

    //Put in a threshold deadzone parameter in case desired.
    if (Math.abs(error) < threshold){
      return 0;
    }
    else {
      return output;
    }
  }
}
