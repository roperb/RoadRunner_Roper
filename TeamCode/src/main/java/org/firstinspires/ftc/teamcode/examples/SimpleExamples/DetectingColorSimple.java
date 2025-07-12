package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import android.graphics.Color;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/*
 * OpMode showing how to detect color and activate a servo based on a measurement and turn on the BlinkinLED Light
 */

@TeleOp(name = "Detecting Color Simple", group = "Simple Examples")
@Disabled
public class DetectingColorSimple extends OpMode {
  private ElapsedTime runtime = new ElapsedTime();
  NormalizedColorSensor colorSensor;
  float gain = 2;

  @Override
  public void init() {
    telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    telemetry.addData("Status", "Initialized");

  }

  @Override
  public void init_loop() {
    telemetry.addData("Time Since INIT pressed", runtime.toString());
  }

  @Override
  public void start() {
    runtime.reset();
  }

  @Override
  public void loop() {

    // Show the gain value via telemetry
    telemetry.addData("Gain", gain);

    // Tell the sensor our desired gain value (normally you would do this during initialization,
    // not during the loop)
    colorSensor.setGain(gain);

    // Get the normalized colors from the sensor
    NormalizedRGBA colors = colorSensor.getNormalizedColors();

    telemetry.addLine()
            .addData("Red", "%.3f", colors.red)
            .addData("Green", "%.3f", colors.green)
            .addData("Blue", "%.3f", colors.blue);
    telemetry.addData("Alpha", "%.3f", colors.alpha);

    /* If this color sensor also has a distance sensor, display the measured distance.
     * Note that the reported distance is only useful at very close range, and is impacted by
     * ambient light and surface reflectivity. */
    if (colorSensor instanceof DistanceSensor) {
      telemetry.addData("Distance (cm)", "%.3f", ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM));
    }

  }

  @Override
  public void stop() {

  }
}
