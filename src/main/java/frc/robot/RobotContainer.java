package frc.robot;

// this is a cheeky little comment unique to this branch of code (teheehehe!)
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DefaultDriveCommand;
//import frc.robot.commands.DodgeLeftCommand;
//import frc.robot.commands.DriveForwardCommand;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.PivotSubsystem;
//import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import java.util.List;
import java.util.Arrays;

public class RobotContainer {

    private final DriveTrainSubsystem m_DriveTrainSubsystem;
    private final ShooterSubsystem m_ShooterSubsystem;
    private final PivotSubsystem m_PivotSubsystem;

    private final XboxController m_driveController = new XboxController(Constants.CONTROLLER_USB_PORT_DRIVER);
    public static final int CONTROLLER_USB_PORT_DRIVER = 0; // Drivers Controller
    private SlewRateLimiter leftLimiter = new SlewRateLimiter(5);
    private SlewRateLimiter rightLimiter = new SlewRateLimiter(5);

    public DriveTrainSubsystem getDrivetrain() {
        return m_DriveTrainSubsystem;
    }

    public RobotContainer() {
        m_DriveTrainSubsystem = new DriveTrainSubsystem();
        m_PivotSubsystem = new PivotSubsystem();
        m_ShooterSubsystem = new ShooterSubsystem();
        resetDrive();
        configureButtonBindings();
        configureShuffleBoard();
        m_ShooterSubsystem.reloadAll();
        // configureShuffleBoard();
        // System.out.println("container created");
    }

    public void resetDrive() {
        m_DriveTrainSubsystem
                .setDefaultCommand(new DefaultDriveCommand(this::getLeftY, this::getRightY, m_DriveTrainSubsystem));
    }

    public double getLeftY() {
        return Constants.SPEED_MULTIPLIER_LEFT
                * square(leftLimiter.calculate(deadband(m_driveController.getLeftY(), Constants.DEADBAND)));
    }

    public static double deadband(double value, double tolerance) {
        if (Math.abs(value) < tolerance)
            return 0.0;

        return Math.copySign(value, (value - tolerance) / (1.0 - tolerance));
    }

    /**
     * Get the adjusted Right Y axis of the main controller
     * 
     * @return The adjusted Right Y axis of the main controller
     */

    public double getRightY() {
        return Constants.SPEED_MULTIPLIER_RIGHT
                * square(rightLimiter.calculate(deadband(m_driveController.getRightY(), Constants.DEADBAND)));
    }

    public void configureShuffleBoard() {
        ShuffleboardTab tab = Shuffleboard.getTab(Constants.DRIVER_READOUT_TAB_NAME);
        tab.addInteger("Shot Pos", m_ShooterSubsystem::getShootPos);
        tab.addString("Next Barrel", () -> {
            return m_ShooterSubsystem.getNextBarrel() == -1 ? "EMPTY"
                    : String.valueOf(m_ShooterSubsystem.getNextBarrel() + 1);
        });

        tab.addBoolean("Slot 1", () -> {
            return m_ShooterSubsystem.status[0];
        });
        tab.addBoolean("Slot 2", () -> {
            return m_ShooterSubsystem.status[1];
        });
        tab.addBoolean("Slot 3", () -> {
            return m_ShooterSubsystem.status[2];
        });
        tab.addBoolean("Slot 4", () -> {
            return m_ShooterSubsystem.status[3];
        });
        tab.addBoolean("Slot 5", () -> {
            return m_ShooterSubsystem.status[4];
        });
        tab.addBoolean("Slot 6", () -> {
            return m_ShooterSubsystem.status[5];
        });

        tab.addDouble("Pivot Target Pos", m_PivotSubsystem::getGoal);
        tab.addDouble("Pivot Abs Pos", m_PivotSubsystem::getAbsPos);
        tab.addDouble("Pivot Pos", m_PivotSubsystem::getPos);
        tab.addDouble("UPPER", m_PivotSubsystem::getUpper);
        tab.addDouble("LOWER", m_PivotSubsystem::getLower);
        tab.addDouble("Angle", m_PivotSubsystem::getAngle);
        tab.addBoolean("At Goal: ", m_PivotSubsystem::atGoal);
        tab.addDouble("Setpoint", m_PivotSubsystem::getSetpoint);
        tab.addDouble("PIDGoal", m_PivotSubsystem::getPIDGoal);
        tab.addDouble("Normalized Angle", m_PivotSubsystem::getAngleNormalized);
    }

    public void configureButtonBindings() {
        new Trigger(m_driveController::getRightBumper).whileTrue(new InstantCommand(m_PivotSubsystem::raise));
        new Trigger(m_driveController::getLeftBumper).whileTrue(new InstantCommand(m_PivotSubsystem::lower));

        new Trigger(m_driveController::getBButton).onTrue(new InstantCommand(m_ShooterSubsystem::shoot));
        new Trigger(m_driveController::getXButton).onTrue(new InstantCommand(m_ShooterSubsystem::reloadAll));

        /*
         * new Trigger(() -> {
         * int d = m_driveController.getPOV();
         * return !(d == -1 || d >= 270);
         * })
         * .onTrue(new InstantCommand(() -> {
         * List<Integer> positions = Arrays.asList(0, 45, 90, 135, 180, 225);
         * m_ShooterSubsystem.shootAtPos(positions.indexOf(m_driveController.getPOV()));
         * }));
         * 
         */
    }

    // new Trigger(m_driveController::getLeftBumper).onTrue(new
    // InstantCommand(this::toggleSlowMode));
    // new Trigger(m_driveController::getRightBumper).onTrue(new
    // InstantCommand(this::toggleTurboMode));
    // new Trigger(m_driveController::getBButton).onTrue(new
    // InstantCommand(m_DriveTrainSubsystem::printSensor));
    // new Trigger(m_driveController::getBButton).onTrue(new
    // DriveForwardCommand(m_DriveTrainSubsystem));
    // new Trigger(m_driveController::getRightBumper).onTrue(new
    // InstantCommand(m_IndexerSubsystem::shoot));

    /**
     * Copy sign square
     * 
     * @param value Value to square
     * @return The copy sign square
     */
    public static double square(double value) {
        return Math.copySign(value, value);

    }

    public PivotSubsystem getPivotSubsystem() {
        return m_PivotSubsystem;
    }

}
