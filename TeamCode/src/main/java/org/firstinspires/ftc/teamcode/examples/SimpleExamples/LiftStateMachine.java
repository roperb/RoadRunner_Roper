package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * Demonstrates a lift that has 2 set positions and transition using a state machine
 */
@Config
@TeleOp(name = "Lift: State Machine", group = "Simple Examples")
@Disabled
public class LiftStateMachine extends OpMode {

  private ElapsedTime runtime = new ElapsedTime();
  private DcMotor lift;

  //A bunch of variables we can change
  public static int LOW_POSITION = 0;
  public static int HIGH_POSITION = 600;
  public static double LIFT_SPEED = 0.8;
  public static double FLOAT_SPEED = 5;
  public static double TRIGGER_LIMIT = .05;
  public static int target = LOW_POSITION;    //need to set the target position, so we set it at the assumed starting low position.

  public enum LiftState {LOW,HIGH,FLOAT}  //Names for the states the lift can be in.  Float is anywhere between low and high
  LiftState liftState;                    //Variable that keeps track of the current state of the lift position.

  @Override
  public void init() {
    telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    //Initializing hardware using a hardware map
    lift = hardwareMap.get(DcMotor.class,"lift");

    //Setting motor basic directions and behavior
    lift.setDirection(DcMotorSimple.Direction.REVERSE);
    lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    //setting up RUN_TO_POSITION Mode - note the order of steps!  Very important!
    lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //likely not needed, but sets encoders to 0.
    lift.setTargetPosition(LOW_POSITION);
    lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    lift.setPower(LIFT_SPEED);

    //Assume the lift starts at the lowest position.
    liftState = LiftState.LOW;

    telemetry.addData("Lift Position", liftState);
    telemetry.addData("Status", "Initialized");
  }

  @Override
  public void start() {
    runtime.reset();
  }

  @Override
  public void loop() {

    //helper variables that store current state of gamepad values this loop.
    //It takes more time to access the gamepad than to access a variable, so these help us not need to reread the gamepad more than once.
    boolean UP_PRESSED = gamepad1.dpad_up;
    boolean DOWN_PRESSED = gamepad1.dpad_down;
    double rightTrigger = gamepad1.right_trigger;
    double leftTrigger = gamepad1.left_trigger;
    boolean RIGHT_TRIGGER_HELD = rightTrigger > TRIGGER_LIMIT;
    boolean LEFT_TRIGGER_HELD = leftTrigger > TRIGGER_LIMIT;

    //a "switch" statement is like a series of if statements.  We use them for state machines often for what to do in each state.
    switch(liftState){
      case LOW:
        if (target != LOW_POSITION) {target = LOW_POSITION;}  //makes sure that the target position is correctly set if in this state.

        if (UP_PRESSED) {//transitions the lift to "high" state if up on dpad is pressed.  Note that it doesn't actually do the moving here.
          liftState = LiftState.HIGH;
        }
        else if (RIGHT_TRIGGER_HELD || LEFT_TRIGGER_HELD){//transitions the lift to the "float" state if either trigger is pressed
          liftState = LiftState.FLOAT;
        }
        break;  //necessary for a switch statement for us to use "break".
                //Not using "break" will automatically run through each case rather than only the one we want

      case HIGH:
        if (target != HIGH_POSITION){target = HIGH_POSITION;}

        if (DOWN_PRESSED){liftState = LiftState.LOW;}
        else if (RIGHT_TRIGGER_HELD || LEFT_TRIGGER_HELD){liftState = LiftState.FLOAT;}
        break;

      case FLOAT:
        if (target < LOW_POSITION){//Some checks to make sure we don't try to float our way passed the limits of the lift.
          target = LOW_POSITION;
          liftState = LiftState.LOW;
        }
        else if (target > HIGH_POSITION){
          target = HIGH_POSITION;
          liftState = LiftState.HIGH;
        }

        if (UP_PRESSED){liftState = LiftState.HIGH;}
        else if (DOWN_PRESSED){liftState = LiftState.LOW;}
        else {target = target + (int) (rightTrigger*FLOAT_SPEED) - (int)(leftTrigger*FLOAT_SPEED);}
        //this above takes the current target value and either
        //  adds a number if you pressed the right trigger or
        //  subtracts a number if you pressed the left trigger
        //  to raise or lower the lift.  It does not change the state though of the lift.
        break;
    }


    //This code is an example for what the code would look like if we kept accessing the gamepad rather than use helper variables.

    /*
    switch(liftState){
      case LOW:
        if (target != LOW_POSITION) {target = LOW_POSITION;}
        if (gamepad1.dpad_up){liftState = LiftState.HIGH;}
        if (gamepad1.right_trigger > TRIGGER_LIMIT || gamepad1.left_trigger > TRIGGER_LIMIT){liftState = LiftState.FLOAT;}
        break;

      case HIGH:
        if (target != HIGH_POSITION){target = HIGH_POSITION;}
        if (gamepad1.dpad_down){liftState = LiftState.LOW;}
        if (gamepad1.right_trigger > TRIGGER_LIMIT || gamepad1.left_trigger > TRIGGER_LIMIT){liftState = LiftState.FLOAT;}
        break;

      case FLOAT:
        if (target < LOW_POSITION){
          liftState = LiftState.LOW;
          target = LOW_POSITION;
        }
        else if (target > HIGH_POSITION){
          liftState = LiftState.HIGH;
          target = HIGH_POSITION;
        }
        else if (gamepad1.dpad_up){liftState = LiftState.HIGH;}
        else if (gamepad1.dpad_down){liftState = LiftState.LOW;}
        else {target = target + (int) (gamepad1.right_trigger*FLOAT_SPEED) - (int)(gamepad1.left_trigger*FLOAT_SPEED);}
        break;
    }
     */

    //Note how the target position is set once here rather than inside of the switch statement.  The switch statement is there to determine what to do
    //While after the decision is made once this loop, we can then tell the computer to now do it, separating the decision from the action.
    lift.setTargetPosition(target);

    telemetry.addData("Status", "Run Time: " + runtime.toString());
    telemetry.addData("Lift State", liftState);
    telemetry.addData("Lift Position", lift.getCurrentPosition());
  }

}
