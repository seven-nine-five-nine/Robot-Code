package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Mary on 1/8/16, edited on 4/29/16.
 */
public class OpModeCougarWoods3 extends OpMode{

  //To create a proper variable(?) just program it to actually do something.
  //Then it'll turn purple like the rest.


  private double swingPower = 0.5;
  private double frontRightPowerA = 0;
  private double frontLeftPowerA = 0;
  private double backRightPowerA = 0;
  private double backLeftPowerA = 0;
  private double frontRightPowerB = 0;
  private double frontLeftPowerB = 0;
  private double backRightPowerB = 0;
  private double backLeftPowerB = 0;


  private DcMotor backLeft;
  private DcMotor backRight;
  private DcMotor frontLeft;
  private DcMotor frontRight;
  private DcMotor golfClub;

  private boolean isIncreasingSwing = false;

  private boolean isDecreasingSwing = false;

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

    if ((gamepad1.left_stick_y >= 0.1) || (gamepad1.left_stick_y <= -0.1)) {
      frm += gamepad1.left_stick_y;
      flm += gamepad1.left_stick_y;
      brm += gamepad1.left_stick_y;
      blm += gamepad1.left_stick_y;
    }

    if ((gamepad1.left_stick_x >= 0.1) || (gamepad1.left_stick_x <= -0.1)) {
      frm += gamepad1.left_stick_x;
      flm -= gamepad1.left_stick_x;
      brm -= gamepad1.left_stick_x;
      blm += gamepad1.left_stick_x;
    }

    if ((gamepad1.left_bumper) && (gamepad1.right_bumper)) {
      frontRight.setPower(1);
      frontLeft.setPower(1);
      backRight.setPower(1);
      backLeft.setPower(1);
    }

    if (gamepad1.left_trigger >= 0.1){
      frt += gamepad1.left_trigger;
      flt -= gamepad1.left_trigger;
      brt += gamepad1.left_trigger;
      blt -= gamepad1.left_trigger;
    }

    if (gamepad1.right_trigger >= 0.1) {
      frt -= gamepad1.right_trigger;
      flt += gamepad1.right_trigger;
      brt -= gamepad1.right_trigger;
      blt += gamepad1.right_trigger;
    }

    frontRightPowerA = frm + frt;
    frontLeftPowerA = flm + flt;
    backRightPowerA = brm + brt;
    backLeftPowerA = blm + blt;

    trimWheelPower(); //keeps wheels below 1 and above -1 power

    frontRight.setPower(frontRightPowerB);
    frontLeft.setPower(frontLeftPowerB);
    backRight.setPower(backRightPowerB);
    backLeft.setPower(backLeftPowerB);
    // f/b refer to front/back, l/r refer to left/right, m/t/f refer to direction/turning/final.


    if (gamepad1.dpad_up && !isIncreasingSwing) {
      isIncreasingSwing = true;
      swingPower += 0.01;
    } else {
      isIncreasingSwing = false;
    }

    if (gamepad1.dpad_down && !isDecreasingSwing) {
      isDecreasingSwing = true;
      swingPower -= 0.01;
    } else {
      isDecreasingSwing = false;
    }
    trimSwingPower(); //make sure the power is an acceptable value

    telemetry.addData("SWING POWER", swingPower);

    if (gamepad1.a) swing();


  }

  private void swing() {
    golfClub.setPower(-1 * swingPower);

    sleep(2000);

    golfClub.setPower(swingPower);

    sleep(3000);

    golfClub.setPower(0);
  }

  private void sleep(int ms) {
    try{
      Thread.sleep(ms);
    } catch (InterruptedException e){

    }

  }

  private void trimWheelPower() {
    if (frontRightPowerA > 1) {
      frontRightPowerB = 1;
    } else if (frontRightPowerA < -1) {
      frontRightPowerB = -1;
    } else {
      frontRightPowerB = frontRightPowerA;
    }

    if (frontLeftPowerA > 1) {
      frontLeftPowerB = 1;
    } else if (frontLeftPowerA < -1) {
      frontLeftPowerB = -1;
    } else {
      frontLeftPowerB = frontLeftPowerA;
    }

    if (backRightPowerA > 1) {
      backRightPowerB = 1;
    } else if (backLeftPowerA < -1) {
      backRightPowerB = -1;
    } else {
      backRightPowerB = backRightPowerA;
    }

    if (backLeftPowerA > 1) {
      backLeftPowerB = 1;
    } else if (backLeftPowerA < -1) {
      backLeftPowerB = -1;
    } else {
      backLeftPowerB = backLeftPowerA;
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

