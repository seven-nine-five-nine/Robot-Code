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
  private double frontRightPowerPre = 0;
  private double frontLeftPowerPre = 0;
  private double backRightPowerPre = 0;
  private double backLeftPowerPre = 0;
  private double frontRightPowerPost = 0;
  private double frontLeftPowerPost = 0;
  private double backRightPowerPost = 0;
  private double backLeftPowerPost = 0;
  private boolean overrideWheels = false;
  private double wheelSpeedMod = 0;
  private boolean wheelOverrideSpeedChange = false;
  private double frontRightPowerOPre = 0;
  private double frontLeftPowerOPre = 0;
  private double backRightPowerOPre = 0;
  private double backLeftPowerOPre = 0;
  private double frontRightPowerOPost = 0;
  private double frontLeftPowerOPost = 0;
  private double backRightPowerOPost = 0;
  private double backLeftPowerOPost = 0;
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
    // double floats for movement
    double cs = 0.05;
    double bpw = 0.5;
    double min = 0.05;
    // double floats for easy changes
    boolean t = false;
    // boolean for an "if" statement.
    double sp = swingPower * 100;
    // double for display

    if ((gamepad1.left_stick_y >= min) || (gamepad1.left_stick_y <= -min)) {
      frm += gamepad1.left_stick_y;
      flm += gamepad1.left_stick_y;
      brm += gamepad1.left_stick_y;
      blm += gamepad1.left_stick_y;
    }

    if ((gamepad1.left_stick_x >= min) || (gamepad1.left_stick_x <= -min)) {
      frm += gamepad1.left_stick_x;
      flm -= gamepad1.left_stick_x;
      brm -= gamepad1.left_stick_x;
      blm += gamepad1.left_stick_x;
    }

    if ((gamepad1.left_bumper) && (gamepad1.right_bumper) && !(gamepad1.y)) {
      frontRightPowerOPre = bpw;
      frontLeftPowerOPre = bpw;
      backRightPowerOPre = bpw;
      backLeftPowerOPre = bpw;
      overrideWheels = true;
    }

    if (gamepad1.left_bumper && !gamepad1.right_bumper && !gamepad1.y) {
      frontRightPowerOPre = bpw;
      frontLeftPowerOPre = 0;
      backRightPowerOPre = 0;
      backLeftPowerOPre = bpw;
      overrideWheels = true;
    }

    if (gamepad1.right_bumper && !gamepad1.left_bumper && !gamepad1.y) {
      frontRightPowerOPre = 0;
      frontLeftPowerOPre = bpw;
      backRightPowerOPre = bpw;
      backLeftPowerOPre = 0;
      overrideWheels = true;
    }

    if (gamepad1.y && !(gamepad1.right_bumper) && !(gamepad1.left_bumper)) {
      frontRightPowerOPre = -bpw;
      frontLeftPowerOPre = -bpw;
      backRightPowerOPre = -bpw;
      backRightPowerOPre = -bpw;
      overrideWheels = true;
    }
    
    if (gamepad1.dpad_left && !wheelOverrideSpeedChange) {
      wheelSpeedMod -= cs;
      wheelOverrideSpeedChange = true;
    } else {
      wheelOverrideSpeedChange = false;
    }

    if (gamepad1.dpad_right && !wheelOverrideSpeedChange) {
      wheelSpeedMod += cs;
      wheelOverrideSpeedChange = true;
    } else {
      wheelOverrideSpeedChange = false;
    }

    if (gamepad1.left_trigger >= min){
      frt += gamepad1.left_trigger;
      flt -= gamepad1.left_trigger;
      brt += gamepad1.left_trigger;
      blt -= gamepad1.left_trigger;
      t = true;
      overrideWheels = true;
    }

    if (gamepad1.right_trigger >= min) {
      frt -= gamepad1.right_trigger;
      flt += gamepad1.right_trigger;
      brt -= gamepad1.right_trigger;
      blt += gamepad1.right_trigger;
      t = true;
      overrideWheels = true;
    }

    frontRightPowerPre = frm + frt;
    frontLeftPowerPre = flm + flt;
    backRightPowerPre = brm + brt;
    backLeftPowerPre = blm + blt;

    trimWheelPower(); //keeps wheels below 1 and above -1 power
    
    trimOverridePower();
    if (overrideWheels) {
      frontRight.setPower(frontRightPowerOPost);
      frontLeft.setPower(frontLeftPowerOPost);
      backRight.setPower(backRightPowerOPost);
      backLeft.setPower(backLeftPowerOPost);
    } else {
      frontRight.setPower(frontRightPowerPost);
      frontLeft.setPower(frontLeftPowerPost);
      backRight.setPower(backRightPowerPost);
      backLeft.setPower(backLeftPowerPost);
    }

    if ((!(gamepad1.y) && !(gamepad1.right_bumper) && !(gamepad1.left_bumper)) || !(t)) {
      overrideWheels = false;
    }

    // f/b refer to front/back, l/r refer to left/right, m/t refer to direction/turning


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

    telemetry.addData("SWING POWER", (sp) + "%");

    if (gamepad1.a) swing();


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
    } catch (InterruptedException e){// Ask Ben about empty catch glitch


    }

  }

  private void trimOverridePower() {
    if (frontRightPowerOPre + wheelSpeedMod > 1) {
      frontRightPowerOPost = 1;
    } else if (frontRightPowerOPre + wheelSpeedMod < 0.05) {
      frontRightPowerOPost = 0.05;
    } else {
      frontRightPowerOPost = frontRightPowerOPre + wheelSpeedMod;
    }

    if (frontLeftPowerOPre + wheelSpeedMod > 1) {
      frontLeftPowerOPost = 1;
    } else if (frontLeftPowerOPre + wheelSpeedMod < 0.05) {
      frontLeftPowerOPost = 0.05;
    } else {
      frontLeftPowerOPost = frontLeftPowerOPre + wheelSpeedMod;
    }

    if (backRightPowerOPre + wheelSpeedMod > 1) {
      backRightPowerOPost = 1;
    } else if (backRightPowerOPre + wheelSpeedMod < 0.05) {
      backRightPowerOPost = 0.05;
    } else {
      backRightPowerOPost = backRightPowerOPre + wheelSpeedMod;
    }

    if (backLeftPowerOPre + wheelSpeedMod > 1) {
      backLeftPowerOPost = 1;
    } else if (backLeftPowerOPre + wheelSpeedMod < 0.05) {
      backLeftPowerOPost = 0.05;
    } else {
      backLeftPowerOPost = backLeftPowerOPre + wheelSpeedMod;
    }
  }
  
  private void trimWheelPower() {
    if (frontRightPowerPre > 1) {
      frontRightPowerPost = 1;
    } else if (frontRightPowerPre < -1) {
      frontRightPowerPost = -1;
    } else {
      frontRightPowerPost = frontRightPowerPre;
    }

    if (frontLeftPowerPre > 1) {
      frontLeftPowerPost = 1;
    } else if (frontLeftPowerPre < -1) {
      frontLeftPowerPost = -1;
    } else {
      frontLeftPowerPost = frontLeftPowerPre;
    }

    if (backRightPowerPre > 1) {
      backRightPowerPost = 1;
    } else if (backLeftPowerPre < -1) {
      backRightPowerPost = -1;
    } else {
      backRightPowerPost = backRightPowerPre;
    }

    if (backLeftPowerPre > 1) {
      backLeftPowerPost = 1;
    } else if (backLeftPowerPre < -1) {
      backLeftPowerPost = -1;
    } else {
      backLeftPowerPost = backLeftPowerPre;
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

