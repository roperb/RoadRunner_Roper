package org.firstinspires.ftc.teamcode.examples.SimpleExamples.VisionProcessorExamples;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.examples.SimpleExamples.VisionProcessorExamples.Processors.FtcDashboard_BasicProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

/**
 * Basic OpMode Example for using the Vision Processor with FTC Dashboard found on YouTube
 * https://www.youtube.com/watch?v=vJhmmyvNqC0
 *
 * Translated example to an Iterative OpMode
 */

@TeleOp(name="FtcDashboard Basic Camera OpMode", group="Simple Examples")
public class FtcDashboard_Camera_Iterative_Basic extends OpMode {
    FtcDashboard_BasicProcessor processor = new FtcDashboard_BasicProcessor();
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        new VisionPortal.Builder().
                addProcessor(processor)
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();
        FtcDashboard.getInstance().startCameraStream(processor, 0);
    }

    @Override
    public void init_loop(){

    }

    @Override
    public void start(){

    }

    @Override
    public void loop() {
        telemetry.addData("Data", "data");

    }

}
