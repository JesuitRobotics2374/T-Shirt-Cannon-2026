// Taken from Season2024

package frc.robot.subsystems;

// import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
// import com.ctre.phoenix.motorcontrol.NeutralMode;
// import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.configs.MotorOutputConfigs;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import java.util.ArrayList;

public class PivotSubsystem extends SubsystemBase {
    // private final WPI_TalonSRX pivotMotor = new
    // WPI_TalonSRX(Constants.PIVOT_MOTOR_ID);
    TalonFX pivotMotor = new TalonFX(Constants.PIVOT_MOTOR_ID);
    CANcoder pivotEncoder = new CANcoder(Constants.PIVOT_ENCODER_ID);
    ProfiledPIDController pivotController = new ProfiledPIDController(Constants.PIVOT_PID_P, Constants.PIVOT_PID_I,
            Constants.PIVOT_PID_D,
            new Constraints(Constants.pivotControllerMaxVelocity, Constants.pivotControllerMaxAcceleration));

    double goal = 0.0;

    double voltage = 0.0;
    double pos_goal = 0.0;
    double speed = 0.0;

    double maxAngle = (67.24 - 38.5) / (0.187 - 0.105);

    static PivotSubsystem instance;

    public PivotSubsystem() {
        instance = this;
        pivotMotor.setNeutralMode(NeutralModeValue.Brake);
        pivotMotor.getConfigurator().apply(new MotorOutputConfigs().withInverted(InvertedValue.Clockwise_Positive));
        pivotEncoder.getConfigurator()
                .apply(new MagnetSensorConfigs().withAbsoluteSensorDiscontinuityPoint(0.5)
                        .withSensorDirection(SensorDirectionValue.CounterClockwise_Positive).withMagnetOffset(.094));
        pivotController.enableContinuousInput(-.5, .5);
        pivotController.setTolerance(0.01, 0.01);
        goal = pivotEncoder.getAbsolutePosition().getValueAsDouble();
        pivotController.reset(goal);
        pivotController.setGoal(goal);
        System.out.println("Pivot Initialized");
    }

    ArrayList<Double> atGoalCache = new ArrayList<Double>();

    @Override
    public void periodic() {
        speed = pivotController.calculate(pivotEncoder.getAbsolutePosition().getValueAsDouble());
        // speed = Math.min(speed, 3.0);
        // speed = Math.max(speed, -3.0);
        pivotMotor.set(speed);

        // Update goal cache
        // if (atGoalCache.size() < 50) {
        // atGoalCache.
        // }
    }

    public void setGoal(double degrees) {
        goal = degrees / 360;
        pivotController.setGoal(goal);
        // goal = degrees / 360;
        System.out.println("set goal");
    }

    public boolean atGoal() {
        return pivotController.atGoal();
    }

    /*
     * public void raise() {
     * System.out.println("Raise goal:" + goal);
     * double new_goal = goal + 0.005;
     * goal = Math.min(Constants.UPPER_SOFT_STOP, new_goal);
     * pivotController.setGoal(goal);
     * }
     */

    public void raise() {
        double increment = 1;
        System.out.println("Raise Goal:" + goal);
        double newGoal = goal + toAbsVal(increment);
        goal = Math.min(Constants.UPPER_SOFT_STOP, newGoal);
        pivotController.setGoal(goal);
    }

    public double getUpper() {
        return Constants.UPPER_SOFT_STOP;
    }

    /*
     * public void lower() {
     * System.out.println("Lower goal:" + goal);
     * double new_goal = goal - 0.005;
     * goal = Math.max(Constants.LOWER_SOFT_STOP, new_goal);
     * pivotController.setGoal(goal);
     * }
     */

    public void lower() {
        double increment = 1;
        System.out.println("Lower Goal:" + goal);
        double newGoal = goal - toAbsVal(increment);
        goal = Math.min(Constants.UPPER_SOFT_STOP, newGoal);
        pivotController.setGoal(goal);
    }

    public double getLower() {
        return Constants.LOWER_SOFT_STOP;
    }

    public void resetGoal() {
        System.out.println("reset goal");
        goal = pivotEncoder.getAbsolutePosition().getValueAsDouble();
        pivotController.reset(goal);
        pivotController.setGoal(goal);
    }

    public double getGoal() {
        return goal;
    }

    public double getAbsPos() {
        return pivotEncoder.getAbsolutePosition().getValueAsDouble();
    }

    public double getPos() {
        return pivotEncoder.getPosition().getValueAsDouble();
    }

    public double getAngle() {
        return pivotEncoder.getAbsolutePosition().getValueAsDouble() * 360;
    }

    public double getSetpoint() {
        return pivotController.getSetpoint().position;
    }

    public double getPIDGoal() {
        return pivotController.getGoal().position;
    }

    public static PivotSubsystem getInstance() {
        return instance;
    }

    public double getAngleNormalized() {
        return getAngle() - Constants.ANGLE_OFFSET;
    }

    private double toAbsVal(double degrees) {
        return degrees / maxAngle;
    }
}
