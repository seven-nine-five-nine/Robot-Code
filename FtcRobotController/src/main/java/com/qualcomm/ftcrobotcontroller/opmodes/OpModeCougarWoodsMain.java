package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Mary on 1/8/16, edited on 5/2/16.
 */
public class OpModeCougarWoodsMain extends OpMode{

    //To create a proper variable(?) just program it to actually do something.
    //Then it'll turn purple like the rest.


    private double swingPower = 0.5;
    //movement variables
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor golfClub;
    //motor variables
    private boolean isIncreasingSwing = false;
    private boolean isDecreasingSwing = false;
    //misc. golfing variables

    @Override
    public void init() {
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        golfClub = hardwareMap.dcMotor.get("golfClub");

        golfClub.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        golfClub.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
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
        double cs = 0.05;
        int fr = 0;
        int fl = 0;
        int br = 0;
        int bl = 0;

        if (gamepad1.dpad_up) {
            fr += 1;
            fl += 1;
            br += 1;
            bl += 1;
        }
        if (gamepad1.dpad_down) {
            fr -= 1;
            fl -= 1;
            br -= 1;
            bl -= 1;
        }
        if (gamepad1.dpad_left) {
            fr += 1;
            fl -= 1;
            br -= 1;
            bl += 1;
        }
        if (gamepad1.dpad_right) {
            fr -= 1;
            fl += 1;
            br += 1;
            bl -= 1;
        }
        if (gamepad1.right_trigger >= 0.05) {
            fr = -1;
            fl = 1;
            br = -1;
            bl = 1;
        } else if (gamepad1.left_trigger >= 0.05) {
            fr = 1;
            fl = -1;
            br = 1;
            bl = -1;
        }
        frontRight.setPower(fr);
        frontLeft.setPower(fl);
        backRight.setPower(br);
        backLeft.setPower(bl);
        // f/b refer to front/back, l/r refer to left/right, m/t/o refer to direction/turning/override

        if (gamepad1.left_bumper && !isIncreasingSwing) {
            isIncreasingSwing = true;
            swingPower += cs;
        } else {
            isIncreasingSwing = false;
        }

        if (gamepad1.right_bumper && !isDecreasingSwing) {
            isDecreasingSwing = true;
            swingPower -= cs;
        } else {
            isDecreasingSwing = false;
        }
        trimSwingPower(); //make sure the power is an acceptable value

        telemetry.addData("SWING POWER", swingPower);

        if (gamepad1.a) swing();

        if (gamepad1.x) {
            golfClub.setPower(-0.2);
        }

        if (gamepad1.b) {
            golfClub.setPower(0.2);
        }
    }

    private void swing() {
        golfClub.setPower(-swingPower);

        sleep(2000);

        golfClub.setPower(swingPower);

        sleep(3000);

        golfClub.setPower(0);
    }

    private void sleep(int ms) {
        try{
            Thread.sleep(ms);
            // ask Ben about "empty catch"
        } catch (InterruptedException e){

        }

    }

    public void trimSwingPower() {
        if (swingPower > 1) {
            swingPower = 1;
        } else if (swingPower < 0) {
            swingPower = 0;
        }
    }


    public double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

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
