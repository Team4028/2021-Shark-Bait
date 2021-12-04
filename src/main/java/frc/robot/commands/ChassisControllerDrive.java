// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

public class ChassisControllerDrive extends CommandBase {
  /** Creates a new ChassisControllerDrive. */
  Chassis _sub = new Chassis();
  public ChassisControllerDrive() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(_sub);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    _sub.controllerDrive();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    _sub.controllerDrive();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
