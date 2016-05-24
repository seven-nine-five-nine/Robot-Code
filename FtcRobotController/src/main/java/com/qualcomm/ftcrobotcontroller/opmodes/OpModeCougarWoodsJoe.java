package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.CommandList;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Mary on 1/8/16, edited on 5/2/16.
 */
public class OpModeCougarWoodsJoe extends OpMode{
//"'I have a large baby'- Joe whatever your last name is 2016"

    private byte modeAmount = 2;
    //byte variables
    private int modeInput = modeAmount - 1;
    private int toggleMode = 0;
    //integer variables
    private long working = telemetry.getTimestamp();
    //long variables
    private double swingPower = 0.5;
    private double range = 0.05;
    //double variables
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor golfClub;
    //motor variables
    private boolean isIncreasingSwing = false;
    private boolean isDecreasingSwing = false;
    private boolean recentToggle = false;
    private boolean recentReset = false;
    private boolean recentChange = false;
    private boolean returnTo = true;
    private boolean stateMovement = true;
    //boolean variables
    private String modeName = "default";
    //string variables
    private char unitPercent = '\u0025';
    //character variables

    @Override
    public void init() {
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        golfClub = hardwareMap.dcMotor.get("golfClub");
        //Shortens variables
        golfClub.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        golfClub.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        frontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        frontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        //Activates and configures motors.
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
        double cs = 0.05;
        double sp = swingPower * 100;
        double ljsxa = Math.abs(gamepad1.left_stick_x);
        double ljsya = Math.abs(gamepad1.left_stick_y);
        double ms = (100 * (ljsya + ljsxa));
        double min = range;
        //doubles
        boolean turningOn = false;
        boolean overrideWheels;
        //booleans

        telemetry.addData("LAST LOOP", working);

        if (gamepad1.back && gamepad1.b) {
            returnTo = false;
            setRange();
        }

        if (stateMovement) {
            telemetry.addData("MOVEMENT SPEED (percent)", (ms) + unitPercent);
            stateMovement = false;
        }

        if (!gamepad1.start) {
            recentToggle = false;
        }

        if (gamepad1.start && !recentToggle) {
            toggleAbsolute();
            setMovementName();
            stateMovement = true;
        }

        if (gamepad1.start) {
            recentToggle = true;
        }

        telemetry.addData("MOVEMENT MODE", modeName);

        if (Math.abs(gamepad1.left_stick_x) > Math.abs(gamepad1.left_stick_y)) {
            if (gamepad1.left_stick_x > min) {
                frm += 1;
                flm -= 1;
                brm -= 1;
                blm += 1;
            } else if (gamepad1.left_stick_x < -min) {
                frm -= 1;
                flm += 1;
                brm += 1;
                blm -= 1;
            }
        } else if (Math.abs(gamepad1.left_stick_y) >= Math.abs(gamepad1.left_stick_x)) {
            if (gamepad1.left_stick_y > min) {
                frm += 1;
                flm += 1;
                brm += 1;
                blm += 1;
            } else if (gamepad1.left_stick_y < -min) {
                frm -= 1;
                flm -= 1;
                brm -= 1;
                blm -= 1;
            }
        }

        while (((gamepad1.left_stick_y >= min) || (gamepad1.left_stick_y <= -min)) && toggleMode == 0) {
            frm += gamepad1.left_stick_y;
            flm += gamepad1.left_stick_y;
            brm += gamepad1.left_stick_y;
            blm += gamepad1.left_stick_y;
        }

        while (((gamepad1.left_stick_x >= min) || (gamepad1.left_stick_x <= -min)) && toggleMode == 0) {
            frm += gamepad1.left_stick_x;
            flm -= gamepad1.left_stick_x;
            brm -= gamepad1.left_stick_x;
            blm += gamepad1.left_stick_x;
        }

        if (gamepad1.y && toggleMode == 0) {
            fro = 1;
            flo = 1;
            bro = 1;
            blo = 1;
            overrideWheels = true;
        } else {
            overrideWheels = false;
        }

        if (gamepad1.left_trigger >= min && gamepad1.b && toggleMode == 0) {
            frt += gamepad1.left_trigger;
            flt -= gamepad1.left_trigger;
            brt += gamepad1.left_trigger;
            blt -= gamepad1.left_trigger;
            turningOn = true;
        } else if (gamepad1.right_trigger >= min && gamepad1.b && toggleMode == 0) {
            frt -= gamepad1.right_trigger;
            flt += gamepad1.right_trigger;
            brt -= gamepad1.right_trigger;
            blt += gamepad1.right_trigger;
            turningOn = true;
        }

        if (gamepad1.left_bumper && toggleMode == 1) {
            frt += 0.5;
            flt -= 0.5;
            brt += 0.5;
            blt -= 0.5;
            turningOn = true;
        } else if (gamepad1.right_bumper && toggleMode == 1) {
            frt -= 0.5;
            flt += 0.5;
            brt -= 0.5;
            blt += 0.5;
            turningOn = true;
        }

        if (turningOn) {
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
        trimSwingPower();

        telemetry.addData("SWING POWER (percent)", (sp) + unitPercent);

        if (gamepad1.x) swing();

        if (gamepad1.left_trigger >= min && gamepad1.a) {
            golfClub.setPower(-gamepad1.left_trigger / 3);
        }

        if (gamepad1.right_trigger >= min && gamepad1.a) {
            golfClub.setPower(gamepad1.right_trigger / 3);
        }

        if (gamepad1.a && gamepad1.back && !recentReset) {
            reset();
        } else {
            recentReset = false;
        }

        timestamp();
    }

    private void timestamp() {
        working = telemetry.getTimestamp();
    }

    private void setMovementName() {
        if (toggleMode == 0) {
            modeName = "default";
        } else if (toggleMode == 1) {
            modeName = "\"dpad style\" joystick";
        } else {
            toggleMode = 0;
            modeName = "default";
        }
    }

    private void reset() {
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
        golfClub.setPower(0);
        swingPower = 0.5;
        toggleMode = 0;
        telemetry.clearData();
        stateMovement = true;
        init();
        recentReset = true;
    }

    private void setRange() {

        telemetry.clearData();

        telemetry.addData("SENSITIVITY (percent)", (100 * (1 - range)) + unitPercent);

        if (gamepad1.a && !recentChange) {
            range += 0.05;
            recentChange = true;
        } else if (gamepad1.b && !recentChange) {
            range = range -0.05;
            recentChange = true;
        } else {
            recentChange = false;
        }

        if (!gamepad1.back && returnTo) {
            setRange();
        } else if (!gamepad1.back) {
            returnTo = true;
            setRange();
        } else if (!returnTo) {
            setRange();
        } else {
            telemetry.clearData();
            recentChange = false;
            trimRange();
        }
    }

    private void trimRange() {
        if (range > 1) {
            range = 1;
        }

        if (range < 0) {
            range = 0;
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

    private void toggleAbsolute() {
        toggleMode = toggleMode + 1;
        if (toggleMode >= modeInput) {
            toggleMode = 0;
        }

        if (toggleMode < 0) {
            toggleMode = modeInput;
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