package org.firstinspires.ftc.teamcode.examples.SimpleExamples.VisionProcessorExamples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.examples.SimpleExamples.VisionProcessorExamples.Processors.DrawRectangleProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

@Disabled
@TeleOp(name = "Draw Rectangle OpMode", group = "Simple Examples")
public class DrawRectangleOpMode extends OpMode {
    private DrawRectangleProcessor drawRectangleProcessor;
    private VisionPortal visionPortal;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        drawRectangleProcessor = new DrawRectangleProcessor();
        WebcamName camera = hardwareMap.get(WebcamName.class,"Webcam 1");

        visionPortal = VisionPortal.easyCreateWithDefaults(camera, drawRectangleProcessor);
    }

    @Override
    public void init_loop(){

    }

    @Override
    public void loop() {

    }

    @Override
    public void stop(){

    }
}
