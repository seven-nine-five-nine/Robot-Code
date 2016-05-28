package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.CommandList;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Joseph on 5/26/2016.
 */
public class OpModeCougarWoodsTest extends OpMode{
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor golfClub;

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
        int br = 0;
        int bl = 0;
        int fr = 0;
        int fl = 0;
        double gc = 0;
        if (gamepad1.dpad_up && !gamepad1.b) {
            fr = 1;
        } else if (gamepad1.dpad_up) {
            fr = -1;
        }
        if (gamepad1.dpad_right && gamepad1.b) {
            br = 1;
        } else if (gamepad1.dpad_right) {
            br = -1;
        }
        if (gamepad1.dpad_down && gamepad1.b) {
            bl = 1;
        } else if (gamepad1.dpad_down) {
            bl = -1;
        }
        if (gamepad1.dpad_left && gamepad1.b) {
            fl = 1;
        } else if (gamepad1.dpad_left) {
            fl = -1;
        }
        if (gamepad1.a && gamepad1.b) {
            gc = 0.2;
        } else if (gamepad1.a) {
            gc = 0.2;
        }
        frontRight.setPower(fr);
        frontLeft.setPower(fl);
        backRight.setPower(br);
        backLeft.setPower(bl);
        golfClub.setPower(gc);
    }
}