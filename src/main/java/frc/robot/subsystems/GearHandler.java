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

  private enum InfeedState {
    RUNNING,
    STOPPED
  }

  private InfeedState _infeedState = InfeedState.STOPPED;

  private enum GearTiltPidState {
    HOME,
    SCORE,
    FLOOR,
    GO
  }

  private GearTiltPidState _pidState = GearTiltPidState.HOME;
  public GearHandler() {
    _m = new TalonSRX(7);
    _m.configFactoryDefault();

    _m.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    _m.setSensorPhase(false);
    _m.setInverted(true);
    _m.setNeutralMode(NeutralMode.Coast);
    _m.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
    _m.selectProfileSlot(0, 0);

    _m.configNominalOutputForward(0.0f, 0);
		_m.configNominalOutputReverse(0.0f, 0);
		_m.configPeakOutputForward(3., 0);
		_m.configPeakOutputReverse(-6.0, 0);

    _intake = new TalonSRX(8);
    _intake.configFactoryDefault();

    //System.out.println("asd");
    //zeroSwitch();
    //System.out.println(_m.getSelectedSensorPosition());

    // velocity: ~800 units per 100ms
    _m.configMotionCruiseVelocity(800);
    _m.configMotionAcceleration(800);

    _m.config_kF(0, .25);
    _m.config_kP(0, 2.);
    _m.config_kI(0, 0.);
    _m.config_IntegralZone(0, 50);
    _m.config_kD(0, 50.);

    _m.set(ControlMode.PercentOutput, -0.2);
  }

  public void zeroSwitch() {
    //System.out.println("zeroing switch");
    while (_m.getSensorCollection().isRevLimitSwitchClosed()) {
      _m.set(ControlMode.PercentOutput, 0.25);
    }

    _m.set(ControlMode.PercentOutput, 0.);
    _m.setSelectedSensorPosition(0);
  }

  public void setPidState() {
    //System.out.println("Setting pid state");
    if (_controller.getBackButtonPressed()) {
      if (_pidState == GearTiltPidState.FLOOR) {
        _pidState = GearTiltPidState.SCORE;
      } else {
        _pidState = GearTiltPidState.FLOOR;
      }
    }
    if (_controller.getStartButtonPressed()) {
      _pidState = GearTiltPidState.HOME;
    }
    if (_controller.getBButtonPressed()) {
      _pidState = GearTiltPidState.GO;
    }
    //System.out.println(_pidState);
  }

  public void pidStateLoop() {
    //System.out.println("pid satet loop");
    //System.out.println(_m.getSelectedSensorPosition());
    switch (_pidState) {
      case HOME:
        zeroSwitch();
        break;
      case FLOOR:
        floor();
        break;
      case SCORE:
        score();
        break;
      case GO:
        go();
        break;
      default:
        break;
    }
  }

  public void go() {
    if (_m.getSelectedSensorPosition() > -1500) {
      _intake.set(ControlMode.PercentOutput, 0.5);
      floor();
    } else {
      _intake.set(ControlMode.PercentOutput, 0);
      _pidState = GearTiltPidState.HOME;
    }
  }

  public void score() {
    setTilt(-300);
  }

  public void floor() {
    //if (_m.getSelectedSensorPosition() > -1500) {
    _m.set(ControlMode.Position, -2000);
    System.out.println(_m.getSelectedSensorPosition());
    /*} else {
      System.out.println("SNEED");
      _m.set(ControlMode.PercentOutput, 0);
    }*/
  }

  public void setTilt(double pos) {
    _m.set(ControlMode.MotionMagic, pos);
  }

  public void infeedToggle() {
    if (_controller.getAButtonPressed()) {
      switch (_infeedState) {
        case RUNNING:
          _infeedState = InfeedState.STOPPED;
          _intake.set(ControlMode.PercentOutput, 0.);
          break;
        case STOPPED:
          _infeedState = InfeedState.RUNNING;
          _intake.set(ControlMode.PercentOutput, -0.5);
        default:
          break;
      }
    }

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
