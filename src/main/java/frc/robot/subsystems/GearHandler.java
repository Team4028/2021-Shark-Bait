// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class GearHandler extends SubsystemBase {
  /** Creates a new GearHandler. */
  private TalonSRX _m;
  private TalonSRX _intake;
  private RobotController _controller = RobotController.getInstance();

  private enum GearTiltPidState {
    HOME,
    SCORE
  }

  private GearTiltPidState _pidState = GearTiltPidState.HOME;
  public GearHandler() {
    _m = new TalonSRX(7);
    _m.configFactoryDefault();

    _m.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    _m.setSensorPhase(false);
    _m.setNeutralMode(NeutralMode.Coast);
    _m.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
    _m.selectProfileSlot(0, 0);

    _m.configNominalOutputForward(0.0f, 0);
		_m.configNominalOutputReverse(0.0f, 0);
		_m.configPeakOutputForward(3., 0);
		_m.configPeakOutputReverse(-6.0, 0); 

    _m.config_kP(0, 1.);
    _m.config_kI(0, 0.);
    _m.config_kD(0, 50.);

    _intake = new TalonSRX(8);
    _intake.configFactoryDefault();

    System.out.println("asd");
    zeroSwitch();
    System.out.println(_m.getSelectedSensorPosition());

    _m.set(ControlMode.PercentOutput, -0.2);
  }

  public void zeroSwitch() {
    if (_m.getSensorCollection().isRevLimitSwitchClosed()) {
      _m.set(ControlMode.PercentOutput, -0.2);
    }
    //
    _m.set(ControlMode.PercentOutput, 0.);
    _m.setSelectedSensorPosition(0);
  }

  public void setPidState() {
    if (_controller.getAButtonPressed()) {
      if (_pidState == GearTiltPidState.HOME) {
        _pidState = GearTiltPidState.SCORE;
      } else {
        _pidState = GearTiltPidState.HOME;
      }
    }
    System.out.println(_pidState);
  }

  public void pidStateLoop() {
    switch (_pidState) {
      case HOME:
        _m.set(ControlMode.PercentOutput, -0.2);
        System.out.println(_m.getSensorCollection().isRevLimitSwitchClosed());
        if (!_m.getSensorCollection().isRevLimitSwitchClosed()) {
          _m.set(ControlMode.PercentOutput, 0.0);
        }
        break;
      case SCORE:
        score();
        break;
      default:
        break;
    }
  }

  public void home() {
    setTilt(0);
    //System.out.println(_m.getSelectedSensorPosition());
  }

  public void score() {
    setTilt(400);
    System.out.println(_m.getSelectedSensorPosition());
  }

  public void setTilt(double pos) {
    _m.set(ControlMode.Position, pos);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
