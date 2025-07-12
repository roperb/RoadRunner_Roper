package org.firstinspires.ftc.teamcode.examples.SimpleExamples.VisionProcessorExamples.Processors;

import static org.firstinspires.ftc.teamcode.examples.SimpleExamples.VisionProcessorExamples.FtcDashboard_PeopleMasking_Camera_OpMode.RANGE_HIGH;
import static org.firstinspires.ftc.teamcode.examples.SimpleExamples.VisionProcessorExamples.FtcDashboard_PeopleMasking_Camera_OpMode.RANGE_LOW;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.concurrent.atomic.AtomicReference;


/**
 * Masking Vision Processor Example for using the Vision Processor with FTC Dashboard found on YouTube
 * https://www.youtube.com/watch?v=vJhmmyvNqC0
 */
public class FtcDashboard_PeopleMaskingProcessor implements VisionProcessor, CameraStreamSource {
    Mat peopleMask = new Mat();
    private final AtomicReference<Scalar> mean = new AtomicReference<>(new Scalar(0,0,0,0));
    private final AtomicReference<Bitmap> lastFrame = new AtomicReference<>(Bitmap.createBitmap(1,1, Bitmap.Config.RGB_565));
    private final AtomicReference<Bitmap> peopleMaskedFrame = new AtomicReference<>(Bitmap.createBitmap(1,1, Bitmap.Config.RGB_565));

    @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(lastFrame.get()));
    }

    //getter functions to access the private data without being able to modify it outside of the class
    public Bitmap getMaskedFrameBitmap(){
        return peopleMaskedFrame.get();
    }
    public Bitmap getLastFrame(){
        return lastFrame.get();
    }
    public Scalar getMeanColor(){
        return mean.get();
    }

    @Override
    public void init(int width, int height, CameraCalibration cameraCalibration) {
        lastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Bitmap b = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(frame, b);
        lastFrame.set(b);
        mean.set(Core.mean(frame));
        Core.inRange(frame, RANGE_LOW, RANGE_HIGH,peopleMask);
        Bitmap b_peopleMask = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(peopleMask, b_peopleMask);
        peopleMaskedFrame.set(b_peopleMask);
        return null;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int i, int i1, float v, float v1, Object o) {

    }
}
