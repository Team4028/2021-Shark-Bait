// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Date;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Chassis extends SubsystemBase {
  /** Creates a new Chassis. */
  TalonSRX _FL;
  TalonSRX _FR;
  TalonSRX _BL;
  TalonSRX _BR;
  TalonSRX motor1;
  TalonSRX motor2;
  TalonSRX motor4;
  TalonSRX motor5;
  TalonSRX motor6;

  RobotController _controller;

  public Chassis() {
    _controller = RobotController.getInstance();

    _FL = new TalonSRX(11);
    _FL.configFactoryDefault();

    _BL = new TalonSRX(12);
    _BL.configFactoryDefault();
    _BL.follow(_FL);

    _FR = new TalonSRX(9);
    _FR.configFactoryDefault();
    _FR.setInverted(true);

    _BR = new TalonSRX(10);
    _BR.configFactoryDefault();
    _BR.follow(_FR);
    _BR.setInverted(true);

    motor1 = new TalonSRX(6);
    motor1.configFactoryDefault();

    motor2 = new TalonSRX(5);
    motor2.configFactoryDefault();

    motor4 = new TalonSRX(3);
    motor4.configFactoryDefault();

    motor5 = new TalonSRX(1);
    motor5.configFactoryDefault();

    motor6 = new TalonSRX(2);
    motor6.configFactoryDefault();

  }

  public void controllerDrive() {
    if (_controller.getBButton()) {
      System.out.println(_controller.getBButtonPressed());
    }

    if (_controller.getBButton()) {
      drive(-0.3, 0);
    } else {
      drive(-_controller.getLeftYAxis(), _controller.getRightXAxis());
    }
  }

  public void drive(double throttle, double turn) {
    _FL.set(ControlMode.PercentOutput, 0.7 * throttle - 0.7 * turn);
    _FR.set(ControlMode.PercentOutput, 0.7 * throttle + 0.7 * turn);
  }

  public void stop() {
    _FL.set(ControlMode.PercentOutput, 0.);
    _FR.set(ControlMode.PercentOutput, 0.);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
