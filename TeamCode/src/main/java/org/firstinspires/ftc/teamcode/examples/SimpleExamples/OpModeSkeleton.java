package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * Demonstrates an empty iterative OpMode structure
 */
@TeleOp(name = "OpMode Skeleton", group = "Simple Examples")
@Disabled
public class OpModeSkeleton extends OpMode {

  private ElapsedTime runtime = new ElapsedTime();

  /**
   * This method will be called once, when the INIT button is pressed.
   */
  @Override
  public void init() {
    //This line is needed to send data to the driver station and FTC Dashboard simultaneously.
    //All other code will have it included in these examples.  It must be included in "init".
    telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    telemetry.addData("Status", "Initialized");
  }

  /**
   * This method will be called repeatedly during the period between when
   * the init button is pressed and when the play button is pressed (or the
   * OpMode is stopped).
   */
  @Override
  public void init_loop() {
    /*
     * Code to repeat
     */
    telemetry.addData("Time Since INIT pressed", runtime.toString());
  }

  /**
   * This method will be called once, when the play button is pressed.
   */
  @Override
  public void start() {
    //resets the clock
    runtime.reset();
  }

  /**
   * This method will be called repeatedly during the period between when
   * the play button is pressed and when the OpMode is stopped.
   */
  @Override
  public void loop() {
    telemetry.addData("Run Time in Loop", runtime.toString());
  }

  /**
   * This method will be called once, when this OpMode is stopped.
   * <p>
   * Your ability to control hardware from this method will be limited.
   */
  @Override
  public void stop() {

  }
}
