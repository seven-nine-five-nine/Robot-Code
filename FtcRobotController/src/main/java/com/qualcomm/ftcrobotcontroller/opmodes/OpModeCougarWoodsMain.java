package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Mary on 1/8/16, edited on 5/2/16.
 */
public class OpModeCougarWoodsMain extends OpMode{

    //To create a proper variable(?) just program it to actually do something.
    //Then it'll turn purple like the rest.


    private double swingPower = 0.5;
    private boolean overrideWheels = false;
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
        double frm = 0;
        double flm = 0;
        double brm = 0;
        double blm = 0;
        double frt = 0;
        double flt = 0;
        double brt = 0;
        double blt = 0;
        double fro = 0;
        double flo = 0;
        double bro = 0;
        double blo = 0;
        boolean yox;
        boolean xoy;
        boolean triggersOn;
        // for movement
        double cs = 0.05;
        double min = 0.05;
        double minm = 0.95;
        // double floats for easy changes
        double sp = swingPower * 100;
        // double for display

        if (gamepad1.left_stick_x >= minm || gamepad1.left_stick_x >= -minm) {
            xoy = true;
        } else {
            xoy = false;
        }

        while (((gamepad1.left_stick_y >= min) || (gamepad1.left_stick_y <= -min)) && !xoy) {
            frm += gamepad1.left_stick_y;
            flm += gamepad1.left_stick_y;
            brm += gamepad1.left_stick_y;
            blm += gamepad1.left_stick_y;
        }

        if (gamepad1.left_stick_y >= minm || gamepad1.left_stick_y <= -minm) {
            yox = true;
        } else {
            yox = false;
        }

        while (((gamepad1.left_stick_x >= min) || (gamepad1.left_stick_x <= -min)) && !yox) {
            frm += gamepad1.left_stick_x;
            flm -= gamepad1.left_stick_x;
            brm -= gamepad1.left_stick_x;
            blm += gamepad1.left_stick_x;
        }

        if (gamepad1.left_bumper && !gamepad1.right_bumper && !gamepad1.y) {
            fro = 1;
            flo = 0;
            bro = 0;
            blo = 1;
            overrideWheels = true;
        }

        if (gamepad1.right_bumper && !gamepad1.left_bumper && !gamepad1.y) {
            fro = 0;
            flo = 1;
            bro = 1;
            blo = 0;
            overrideWheels = true;
        }

        if (gamepad1.y && !(gamepad1.right_bumper) && !(gamepad1.left_bumper)) {
            fro = -1;
            flo = -1;
            bro = -1;
            blo = -1;
            overrideWheels = true;
        }

        if (gamepad1.left_trigger >= min) {
            frt += gamepad1.left_trigger;
            flt -= gamepad1.left_trigger;
            brt += gamepad1.left_trigger;
            blt -= gamepad1.left_trigger;
            triggersOn = true;
        } else if (gamepad1.right_trigger >= min){
            frt -= gamepad1.right_trigger;
            flt += gamepad1.right_trigger;
            brt -= gamepad1.right_trigger;
            blt += gamepad1.right_trigger;
            triggersOn = true;
        } else {
            triggersOn = false;
        }

        if ((!(gamepad1.y) && !(gamepad1.right_bumper) && !(gamepad1.left_bumper))) {
            overrideWheels = false;
        }

        if (triggersOn) {
            frontRight.setPower(frt);
            frontLeft.setPower(flt);
            backRight.setPower(brt);
            backLeft.setPower(blt);
        } else if (overrideWheels) {
            frontRight.setPower(fro);
            frontLeft.setPower(flo);
            backRight.setPower(bro);
            backLeft.setPower(blo);
        } else {
            frontRight.setPower(frm);
            frontLeft.setPower(flm);
            backRight.setPower(brm);
            backLeft.setPower(blm);
        }

        // f/b refer to front/back, l/r refer to left/right, m/t/o refer to direction/turning/override

        if (gamepad1.dpad_up && !isIncreasingSwing) {
            isIncreasingSwing = true;
            swingPower += cs;
        } else {
            isIncreasingSwing = false;
        }

        if (gamepad1.dpad_down && !isDecreasingSwing) {
            isDecreasingSwing = true;
            swingPower -= cs;
        } else {
            isDecreasingSwing = false;
        }
        trimSwingPower(); //make sure the power is an acceptable value

        telemetry.addData("SWING POWER (percent)", (sp) + "%");

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
