package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.examples.SimpleExamples.EOCVPipelines.ColorConvertPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

/*
 * Demonstrates an empty iterative OpMode
 */
@TeleOp(name = "Webcam Viewer Dashboard", group = "Simple Examples")
@Disabled
public class EOCVWebcamViewerDashboard extends OpMode {
  OpenCvWebcam webcam;
  ElapsedTime timer;
  //BlankPipeline pipeline;
  ColorConvertPipeline pipeline;
  //ContoursPipeline pipeline;
  //DrawShapesPipeline pipeline;
  @Override
  public void init() {
    telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    timer = new ElapsedTime();
    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

    //This allows you to see through the camera on FTC dashboard by going to 192.168.43.1:8080/dash on a webpage.
    FtcDashboard.getInstance().startCameraStream(webcam, 0);

    //pipeline = new BlankPipeline(telemetry);
    pipeline = new ColorConvertPipeline(telemetry);
    //pipeline = new ContoursPipeline(telemetry);
    //pipeline = new DrawShapesPipeline(telemetry);

    webcam.setPipeline(pipeline);       //set the first pipeline in the ArrayList called pipelines to the webcam.
    webcam.setMillisecondsPermissionTimeout(5000); // Timeout for obtaining permission is configurable. Set before opening.
    webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
    {
      @Override
      public void onOpened()
      {
        webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
      }

      @Override
      public void onError(int errorCode) {}
    });
  }

  @Override
  public void init_loop() {
  }

  @Override
  public void start() {
    timer.reset();
  }

  @Override
  public void loop() {

  }

  @Override
  public void stop() {
  }
}
