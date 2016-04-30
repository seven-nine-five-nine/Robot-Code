package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Mary on 1/8/16.
 */
public class OpModeAirplaneRobot extends OpMode{

    //To create a proper variable(?) just program it to actually do something.
    //Then it'll turn purple like the rest.

    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor tapeMeasure1;
    private DcMotor tapeMeasure2;

    private double launchSpeed = 0;

    @Override
    public void init() {
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        tapeMeasure1 = hardwareMap.dcMotor.get("tapeMeasure1");
        tapeMeasure2 = hardwareMap.dcMotor.get("tapeMeasure2");

        tapeMeasure1.setDirection(DcMotor.Direction.FORWARD);
        tapeMeasure2.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        frontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        frontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);


        //some of the motors may need to be reversed, if they're spinning the wrong way call:
        //WHATEVER_MOTOR.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;
        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.

        left =  (float)scaleInput(left);
        right = (float)scaleInput(right);

        // write the values to the motors
        frontRight.setPower(right);
        backRight.setPower(right);
        backLeft.setPower(left);
        frontLeft.setPower(left);

        // update the position of the arm.

        telemetry.addData("MOTOR POWER", launchSpeed);

        if (gamepad1.dpad_down)  { //tape measure
            launchSpeed = -1;
        } else if (gamepad1.dpad_up) {
            //If the motor runs backwards, just reverse the direction
            // or reverse numbers.
            launchSpeed = 1;
        } else {
            launchSpeed = 0;
        }
        tapeMeasure1.setPower(launchSpeed);
        tapeMeasure2.setPower(launchSpeed);
    }


    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }


}

