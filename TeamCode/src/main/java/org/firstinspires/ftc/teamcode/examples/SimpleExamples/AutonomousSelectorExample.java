package org.firstinspires.ftc.teamcode.examples.SimpleExamples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Autonomous Selector Example", group="Simple Examples")
@Disabled
public class AutonomousSelectorExample extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    public enum AutoSelector {BLUE,RED}
    public AutoSelector autoSelector = AutoSelector.BLUE;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addData("Status", "Initialized");
        telemetry.update(); //needed specifically to do in LinearOpModes to send telemetry packet to DS and FTCDashboard.
                            //For Iterative OpModes (normal OpModes) the telemetry is updated each loop automatically.

        // Wait for the game to start (driver presses PLAY)
        while(opModeInInit()){
            if(gamepad1.dpad_left){
                autoSelector = AutoSelector.BLUE;
            }
            if(gamepad1.dpad_right){
                autoSelector = AutoSelector.RED;
            }
            telemetry.addData("Current Auto Selected", autoSelector);
            telemetry.addData("Runtime", runtime.seconds());
            telemetry.update();
        }
        runtime.reset();

        switch(autoSelector){
            case BLUE:
                //blue code goes here
                break;
            case RED:
                //red code goes here
                break;
        }
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            telemetry.addData("Running Auto for",autoSelector);
            telemetry.update();
        }
    }
}
