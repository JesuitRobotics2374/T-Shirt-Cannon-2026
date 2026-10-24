// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/** Add your docs here. */
public class Constants {

    public static final int CONTROLLER_USB_PORT_DRIVER = 0; // Drivers Controller
    public static final double SPEED_MULTIPLIER_LEFT = .6;
    public static final double SPEED_MULTIPLIER_RIGHT = .55; // Adjustment for DT incons
    public static final double DEADBAND = 0.1;
    // Drivetrain Motor IDs
    public static final int FRONT_LEFT_DRIVE_ID = 1;
    public static final int FRONT_RIGHT_DRIVE_ID = 4;
    public static final int BACK_LEFT_DRIVE_ID = 2;
    public static final int BACK_RIGHT_DRIVE_ID = 3;

    // Pivot Subsystem
    public static final int PIVOT_MOTOR_ID = 6;
    public static final int PIVOT_ENCODER_ID = 11; // TBD
    public static final double pivotControllerMaxVelocity = 4.5;
    public static final double pivotControllerMaxAcceleration = 4.5;
    public static final double FEED_FORWARD_VOLTAGE = 0.0;
    //public static final double UPPER_SOFT_STOP = .083;  //ACCURATE
    public static final double UPPER_SOFT_STOP = .07;  //Equals about 0.07*360=25.2 degrees
    public static final double LOWER_SOFT_STOP = 0;  //Equals about 0.07*360=25.2 degrees
    
    public static final double PIVOT_PID_P = 9.0;
    public static final double PIVOT_PID_I = 0.0;
    public static final double PIVOT_PID_D = 0.0;

    // Shooter Subsystem
    public static final int PCM_ID = 30; // TBD
    public static final int numShooters = 6;
    public static final int solenoidAChannel = 6; // TBD
    public static final int solenoidBChannel = 1; // TBD
    public static final int solenoidCChannel = 2; // TBD
    public static final int solenoidDChannel = 3; // TBD
    public static final int solenoidEChannel = 4; // TBD
    public static final int solenoidFChannel = 5; // TBD
    public static final String DRIVER_READOUT_TAB_NAME = "Info";
    public static double waitTime = 1.0; // TBD
    public static double requiredPressure = 0.0; // TBD
    public static double ANGLE_OFFSET = 38.5;
}
