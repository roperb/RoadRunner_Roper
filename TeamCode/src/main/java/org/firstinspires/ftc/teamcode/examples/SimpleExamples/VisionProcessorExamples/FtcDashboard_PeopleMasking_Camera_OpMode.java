package org.firstinspires.ftc.teamcode.examples.SimpleExamples.VisionProcessorExamples;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.examples.SimpleExamples.VisionProcessorExamples.Processors.FtcDashboard_PeopleMaskingProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;

/**
 * Example for using the Vision Processor with FTC Dashboard found on YouTube
 * Creates a mask using Scalar values and allows you to activate the mask visually using Dashboard.
 * https://www.youtube.com/watch?v=vJhmmyvNqC0
 */

@Disabled
@Config
@TeleOp(name="FtcDashboard PeopleMasking Camera Opmode", group="Simple Examples")
public class FtcDashboard_PeopleMasking_Camera_OpMode extends OpMode {
    public static boolean MASK_TOGGLE = false;
    public static Scalar RANGE_LOW = new Scalar(50,50,50,0);
    public static Scalar RANGE_HIGH = new Scalar(140,140,140,255);

    //processor used.
    FtcDashboard_PeopleMaskingProcessor processor = new FtcDashboard_PeopleMaskingProcessor();

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        new VisionPortal.Builder()
                .addProcessor(processor)
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")) //short hand way to set camera.
                .build();
    }

    @Override
    public void loop() {
        if(MASK_TOGGLE){
            FtcDashboard.getInstance().sendImage(processor.getMaskedFrameBitmap());
        } else{
            FtcDashboard.getInstance().sendImage(processor.getLastFrame());
        }
        telemetry.addData("AverageColor: ", processor.getMeanColor().toString());
    }
}
