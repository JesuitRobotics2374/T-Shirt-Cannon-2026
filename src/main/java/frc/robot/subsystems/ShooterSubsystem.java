package frc.robot.subsystems;

import com.ctre.phoenix.CANifier.PWMChannel;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;

public class ShooterSubsystem extends SubsystemBase {
    private Compressor compressor = new Compressor(Constants.PCM_ID, PneumaticsModuleType.CTREPCM);
    private Solenoid solenoidA = new Solenoid(Constants.PCM_ID, PneumaticsModuleType.CTREPCM,
            Constants.solenoidAChannel);
    private Solenoid solenoidB = new Solenoid(Constants.PCM_ID, PneumaticsModuleType.CTREPCM,
            Constants.solenoidBChannel);
    private Solenoid solenoidC = new Solenoid(Constants.PCM_ID, PneumaticsModuleType.CTREPCM,
            Constants.solenoidCChannel);
    private Solenoid solenoidD = new Solenoid(Constants.PCM_ID, PneumaticsModuleType.CTREPCM,
            Constants.solenoidDChannel);
    private Solenoid solenoidE = new Solenoid(Constants.PCM_ID, PneumaticsModuleType.CTREPCM,
            Constants.solenoidEChannel);
    private Solenoid solenoidF = new Solenoid(Constants.PCM_ID, PneumaticsModuleType.CTREPCM,
            Constants.solenoidFChannel);
    private Solenoid[] solenoids = { solenoidA, solenoidB, solenoidC, solenoidD, solenoidE, solenoidF };
    public Boolean[] status = new Boolean[Constants.numShooters];
    private PWMChannel channel = PWMChannel.PWMChannel0;

    int shootPos = 0;
    private boolean firing = false; // ignore B presses until the current shot finishes

    public ShooterSubsystem() {
        for (Solenoid solenoid : solenoids) {
            solenoid.set(false);
        }
    }

    public void shoot() {
        if (firing || shootPos >= Constants.numShooters) {
            return;
        }
        firing = true;
        SequentialCommandGroup command = new SequentialCommandGroup();
        command.addCommands(
                new InstantCommand(() -> {
                    solenoids[shootPos].set(true);
                    System.out.println("shootPos: " + shootPos);
                }),
                new WaitCommand(Constants.waitTime),
                new InstantCommand(() -> {
                    solenoids[shootPos].set(false);
                    status[shootPos] = false;
                    firing = false;

                    while (!status[shootPos]) {
                        shootPos++;
                        if (shootPos >= Constants.numShooters) {
                            System.out.println("Cannons empty!");
                            return;
                        }
                    }
                }));
        command.schedule();
    }

    public void toggleCompressor() {
        if (compressor.isEnabled()) {
            stopCompressor();
        } else {
            startCompressor();
        }
    }

    public void startCompressor() {
        compressor.enableDigital();
    }

    public void stopCompressor() {
        compressor.disable();
    }

    public void reloadAll() {
        for (int i = 0; i < Constants.numShooters; i++) {
            status[i] = true;
        }
        shootPos = 0;
        firing = false; // unsticks B if a shot was cancelled (e.g. robot disabled mid-shot)
    }

    public void shootAtPos(int pos) {
        System.out.println(pos);

        SequentialCommandGroup command = new SequentialCommandGroup();
        command.addCommands(
                new InstantCommand(() -> {
                    solenoids[pos].set(true);
                }),
                new WaitCommand(Constants.waitTime),
                new InstantCommand(() -> {
                    solenoids[pos].set(false);
                }));
        command.schedule();
    }

    public int getShotLeft() {
        int count = 0;
        for (boolean bool : status) {
            if (bool) {
                count++;
            }
        }
        return count;
    }

    public int getShootPos() {
        return shootPos;
    }

    public int getNextBarrel() {
        if (shootPos >= Constants.numShooters) {
            return -1;
        }
        return shootPos;
    }
}
