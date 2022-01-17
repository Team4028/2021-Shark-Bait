// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BallInfeed extends SubsystemBase {
  private Solenoid _s;
  private TalonSRX _m;

  private enum InfeedState {
    INFEED,
    STOP
  }

  private InfeedState _state = InfeedState.STOP;

  private RobotController _controller = RobotController.getInstance();
  /** Creates a new BallInfeed. */
  public BallInfeed() {
    _m = new TalonSRX(3);
    _m.setNeutralMode(NeutralMode.Coast);
		_m.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
		_m.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);

    //_s = new Solenoid(0, 5);
    _s = new Solenoid(0, 5);
  }

  public void infeed() {
    _m.set(ControlMode.PercentOutput, -0.5);
    _s.set(true);
  }

  public void stop() {
    _m.set(ControlMode.PercentOutput, 0.);
    _s.set(false);
  }

  public void setInfeedState() {
    if (_controller.getXButtonPressed()) {
      switch (_state) {
        case INFEED:
          _state = InfeedState.STOP;
          break;
        case STOP:
          _state = InfeedState.INFEED;
          break;
        default:
          break;
      }
    }
  }

  public void stateLoop() {
    switch (_state) {
      case INFEED:
        infeed();
        break;
      case STOP:
        stop();
        break;
      default:
        break;
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
