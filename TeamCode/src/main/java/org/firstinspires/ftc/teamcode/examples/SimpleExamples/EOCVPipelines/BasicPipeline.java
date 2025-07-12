package org.firstinspires.ftc.teamcode.examples.SimpleExamples.EOCVPipelines;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvPipeline;

/***
 * This pipeline is just for showing the basic structure of a pipeline
 */

public class BasicPipeline extends OpenCvPipeline {
    //Necessary lines to allow the pipeline to access the telemetry object from the OpMode.
    //In the opmode, make sure to pass the telemetry object to the pipeline!  ( EOCV Webcam viewer
    Telemetry telemetry;
    public BasicPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    //Place to declare class level variables, particularly variables to hold onto any images you make.
    Mat myRedImage = new Mat();
    Scalar meanColor;
    Scalar redValueScalar;
    double redValue;

    @Override
    public void init(Mat input) {
        // Executed before the first call to processFrame for subframes and other presistent frames.
    }

    @Override
    public Mat processFrame(Mat input) {
        // Executed every time a new frame is dispatched
        Core.extractChannel(input,myRedImage,0);

        meanColor = Core.mean(input);
        redValueScalar = Core.mean(myRedImage);
        redValue = meanColor.val[0];                //.val[0] is needed because the output of .mean(image) is a Scalar, not a double
                                                    //put all of the .val[i] quantities are doubles.

        telemetry.addData("Mean RGB color", meanColor.toString());
        telemetry.addData("Mean red value as Scalar", redValueScalar);
        telemetry.addData("Mean red value","%.1f", redValue);
        telemetry.update();

        return input; // Return the image that will be displayed in the viewport
        // (In this case the input mat directly)
    }

    @Override
    public void onViewportTapped() {
        // Executed when the image display is clicked by the mouse or tapped
        // This method is executed from the UI thread, so be careful to not
        // perform any sort heavy processing here! Your app might hang otherwise
    }

}

