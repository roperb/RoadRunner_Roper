package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import android.graphics.Color;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/*
 * OpMode showing how to detect color and activate a servo based on a measurement and turn on the BlinkinLED Light
 */

@TeleOp(name = "Detecting Color", group = "Simple Examples")
@Disabled
public class DetectingColor extends OpMode {
  private ElapsedTime runtime = new ElapsedTime();
  NormalizedColorSensor colorSensor;

  boolean xButtonPreviouslyPressed = false;
  boolean xButtonCurrentlyPressed = false;
  float gain = 2;
  // Once per loop, we will update this hsvValues array. The first element (0) will contain the
  // hue, the second element (1) will contain the saturation, and the third element (2) will
  // contain the value. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
  // for an explanation of HSV color.
  final float[] hsvValues = new float[3];

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
    telemetry.addData("Run Time in Loop", runtime.toString());
    telemetry.addLine("Hold the A button on gamepad 1 to increase gain, or B to decrease it.\n");
    telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value\n");

    // Update the gain value if either of the A or B gamepad buttons is being held
    if (gamepad1.a) {
      // Only increase the gain by a small amount, since this loop will occur multiple times per second.
      gain += 0.005;
    } else if (gamepad1.b && gain > 1) { // A gain of less than 1 will make the values smaller, which is not helpful.
      gain -= 0.005;
    }

    // Show the gain value via telemetry
    telemetry.addData("Gain", gain);

    // Tell the sensor our desired gain value (normally you would do this during initialization,
    // not during the loop)
    colorSensor.setGain(gain);

    // Check the status of the X button on the gamepad
    xButtonCurrentlyPressed = gamepad1.x;

    // If the button state is different than what it was, then act (makes the button like a toggle)
    if (xButtonCurrentlyPressed != xButtonPreviouslyPressed) {
      // If the button is (now) down, then toggle the light
      if (xButtonCurrentlyPressed) {
        if (colorSensor instanceof SwitchableLight) {
          SwitchableLight light = (SwitchableLight)colorSensor;
          light.enableLight(!light.isLightOn());
        }
      }
    }
    xButtonPreviouslyPressed = xButtonCurrentlyPressed; //store current value for next loop to compare

    // Get the normalized colors from the sensor
    NormalizedRGBA colors = colorSensor.getNormalizedColors();

    /* Use telemetry to display feedback on the driver station. We show the red, green, and blue
     * normalized values from the sensor (in the range of 0 to 1), as well as the equivalent
     * HSV (hue, saturation and value) values. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
     * for an explanation of HSV color. */

    // Update the hsvValues array by passing it to Color.colorToHSV()
    Color.colorToHSV(colors.toColor(), hsvValues);

    telemetry.addLine()
            .addData("Red", "%.3f", colors.red)
            .addData("Green", "%.3f", colors.green)
            .addData("Blue", "%.3f", colors.blue);
    telemetry.addLine()
            .addData("Hue", "%.3f", hsvValues[0])
            .addData("Saturation", "%.3f", hsvValues[1])
            .addData("Value", "%.3f", hsvValues[2]);
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
