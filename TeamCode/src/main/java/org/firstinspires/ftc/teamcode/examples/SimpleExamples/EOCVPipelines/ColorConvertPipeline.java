package org.firstinspires.ftc.teamcode.examples.SimpleExamples.EOCVPipelines;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

/***
 * This pipeline shows how to do different color conversions to different spaces to see what happens and to play with them.
 */

public class ColorConvertPipeline extends OpenCvPipeline {
    Telemetry telemetry;
    public ColorConvertPipeline(Telemetry telemetry){this.telemetry = telemetry;}

    //Variables to be used in EOCV Simulator as a slider
    public Scalar lower = new Scalar(0, 0, 0);
    public Scalar upper = new Scalar(255, 255, 255);

    Mat HSV = new Mat();
    Mat HSVInvert = new Mat();
    Mat GRAY = new Mat();
    Mat R = new Mat();
    Mat G = new Mat();
    Mat B = new Mat();
    Mat YCrCb = new Mat();
    Mat YCrCbInvert = new Mat();
    Mat Y = new Mat();
    Mat Cr = new Mat();
    Mat Cb = new Mat();

    @Override
    public void init(Mat input) {
        // Executed before the first call to processFrame for subframes and other presistent frames.
        //inputGray = input; //needed?
        //inputHSV = input;
        //inputHSV_Range = input;
    }

    @Override
    public Mat processFrame(Mat input) {
        // Executed every time a new frame is dispatched
        Imgproc.cvtColor(input,GRAY,Imgproc.COLOR_RGB2GRAY);
        Imgproc.cvtColor(input,HSV,Imgproc.COLOR_RGB2HSV);
        Imgproc.cvtColor(input,YCrCb,Imgproc.COLOR_RGB2YCrCb);
        Core.invert(HSV,HSVInvert);
        Core.invert(YCrCb,YCrCbInvert);
        Core.extractChannel(YCrCb,Y,0);
        Core.extractChannel(YCrCb,Cr,1);
        Core.extractChannel(YCrCb,Cb,2);
        Core.extractChannel(input,R,0);
        Core.extractChannel(input,G,1);
        Core.extractChannel(input,B,2);


        telemetry.addData("Mean R Value", Core.mean(R));
        telemetry.addData("Mean G Value", Core.mean(G));
        telemetry.addData("Mean B Value", Core.mean(B));
        telemetry.addData("Mean Y Value:", Core.mean(Y));
        telemetry.addData("Mean Cr Value", Core.mean(Cr));
        telemetry.addData("Mean Cb Value", Core.mean(Cb));
        telemetry.update();

                                //Uncomment which image you want to view or return.
                                //Typically you would want to return a particular frame and not have so many to choose from.
        //return input;
        //return HSV
        //return HSVInvert
        return YCrCb;
        //return Y;
        //return Cr;
        //return Cb;
        //return R;
        //return G;
        //return B;

    }



    @Override
    public void onViewportTapped() {
        // Executed when the image display is clicked by the mouse or tapped
        // This method is executed from the UI thread, so be careful to not
        // perform any sort heavy processing here! Your app might hang otherwise
    }

}

