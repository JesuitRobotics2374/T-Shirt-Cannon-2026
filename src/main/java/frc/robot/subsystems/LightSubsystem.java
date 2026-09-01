package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.CANdleConfiguration;
import com.ctre.phoenix6.controls.*;
import com.ctre.phoenix6.controls.ColorFlowAnimation;
import com.ctre.phoenix.led.*;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.signals.AnimationDirectionValue;
import com.ctre.phoenix6.signals.RGBWColor;
import com.ctre.phoenix6.signals.StatusLedWhenActiveValue;
import com.ctre.phoenix6.signals.StripTypeValue;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LightSubsystem extends SubsystemBase {

    private final CANdle led;

    private CANdleConfiguration config;

    private int ledCount = 380; // ADJUST THIS

    public LightSubsystem() {

        led = new CANdle(27);

        config = new CANdleConfiguration();
        config.LED.BrightnessScalar = 0.5; // Adjust brightness (0.0 to 1.0)
        config.LED.StripType = StripTypeValue.GRB; // Set the type

        led.clearAllAnimations();
        led.getConfigurator().apply(config);

    }

    public void defaultPattern() {
        led.setControl(new SolidColor(0, ledCount).withColor(new RGBWColor(0, 255, 0, 0)));
        
    }

    public void disable() {
        led.setControl(new SolidColor(0, ledCount).withColor(new RGBWColor(0, 0, 0, 0)));
        led.setControl(new EmptyAnimation(0));
    }

    public void playColorFlowAnimation(int r, int g, int b, int w) {
        ColorFlowAnimation colorFlowAnimation = new ColorFlowAnimation(0, ledCount)
            .withColor(new RGBWColor(r, g, b, w));
        led.setControl(colorFlowAnimation);
    }
    // ... add others ...

    @Override
    public void periodic() {
        defaultPattern();
    }
}