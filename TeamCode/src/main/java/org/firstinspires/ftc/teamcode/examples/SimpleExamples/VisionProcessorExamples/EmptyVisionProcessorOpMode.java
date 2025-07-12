package org.firstinspires.ftc.teamcode.examples.SimpleExamples.VisionProcessorExamples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.examples.SimpleExamples.VisionProcessorExamples.Processors.EmptyVisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

@Disabled
@TeleOp(name = "Empty Vision Dashboard Processor OpMode", group = "Simple Examples")
public class EmptyVisionProcessorOpMode extends OpMode {

    private VisionPortal visionPortal;
    private EmptyVisionProcessor visionProcessor;
    private WebcamName camera;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        visionProcessor = new EmptyVisionProcessor();
        camera = hardwareMap.get(WebcamName.class,"Webcam 1");

        //visionPortal = VisionPortal.easyCreateWithDefaults(camera , visionProcessor);

        visionPortal = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessor(visionProcessor)
                .build();

        //Streams the basic frames to dashboard.
        FtcDashboard.getInstance().startCameraStream(visionProcessor, 0);
    }

    @Override
    public void init_loop(){
        telemetry.addData("Camera State", visionPortal.getCameraState());
        telemetry.addData("Camera FPS", visionPortal.getFps());

    }

    public void start(){
        // shuts down the camera once the match starts, we dont need to look any more
        if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
            visionPortal.stopLiveView();
            visionPortal.stopStreaming();
        }
    }

    @Override
    public void loop() {

    }

}
