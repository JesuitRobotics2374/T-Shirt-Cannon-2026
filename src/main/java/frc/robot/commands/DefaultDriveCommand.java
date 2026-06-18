package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveTrainSubsystem;

public class DefaultDriveCommand extends Command {
    DriveTrainSubsystem m_DriveTrainSubsystem;
    DoubleSupplier leftSupplier;
    DoubleSupplier rightSupplier;

    public DefaultDriveCommand(DoubleSupplier left, DoubleSupplier right, DriveTrainSubsystem subsystem) {
        m_DriveTrainSubsystem = subsystem;
        leftSupplier = left;
        rightSupplier = right;
        addRequirements(subsystem);
    }

    @Override
    public void execute() {
        m_DriveTrainSubsystem.drive(leftSupplier.getAsDouble(), rightSupplier.getAsDouble());
    }
}
