package org.firstinspires.ftc.teamcode.examples.SimpleExamples.EOCVPipelines;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public class EmptyPipeline extends OpenCvPipeline {
    Telemetry telemetry;
    public EmptyPipeline(Telemetry telemetry){this.telemetry = telemetry;}

    @Override
    public Mat processFrame(Mat input) {
        // Executed every time a new frame is dispatched

        return input; // Return the image that will be displayed in the viewport
        // (In this case the input mat directly)
    }

}

