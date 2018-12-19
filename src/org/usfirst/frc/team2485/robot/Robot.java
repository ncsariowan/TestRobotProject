/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2485.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	
	private TalonSRX leftTalon, rightTalon;
	
	private VictorSPX leftVictor1, leftVictor2, leftVictor3, rightVictor1, rightVictor2, rightVictor3;
	
	private TalonSRXWrapper leftTalonWrapper, rightTalonWrapper;
	
	private SpeedControllerWrapper leftSpeedControllerWrapper, rightSpeedControllerWrapper;
	
	private Joystick xbox;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		leftTalon = new TalonSRX(1);
		rightTalon = new TalonSRX(2);
		
		leftVictor1 = new VictorSPX(3);
		leftVictor2 = new VictorSPX(4);
		leftVictor3 = new VictorSPX(5);
		rightVictor1 = new VictorSPX(6);
		rightVictor2 = new VictorSPX(7);
		rightVictor3 = new VictorSPX(8);
		
		leftVictor1.follow(leftTalon);
		leftVictor2.follow(leftTalon);
		leftVictor3.follow(leftTalon);
		rightVictor1.follow(rightTalon);
		rightVictor2.follow(rightTalon);
		rightVictor3.follow(rightTalon);
		
		leftTalonWrapper = new TalonSRXWrapper(ControlMode.PercentOutput, leftTalon);
		rightTalonWrapper = new TalonSRXWrapper(ControlMode.PercentOutput, rightTalon);
		
		leftSpeedControllerWrapper = new SpeedControllerWrapper(leftTalonWrapper);
		rightSpeedControllerWrapper = new SpeedControllerWrapper(rightTalonWrapper);
		
		xbox = new Joystick(0);
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		double throttle = ThresholdHandler.deadbandAndScale(xbox.getRawAxis(0), 0.25, -1, 1);
		double steering = ThresholdHandler.deadbandAndScale(xbox.getRawAxis(1), 0.25, -1, 1);
		boolean quickturn = xbox.getRawButton(0);
		
		double leftPWM = 0;
		double rightPWM = 0;
		
		if (quickturn) {
			leftPWM = steering;
			rightPWM = -steering;
		} else {
			
			if (throttle > 0) {

				leftPWM = throttle + steering;
				rightPWM = throttle - steering;

			} else if (throttle < 0) {

				leftPWM = throttle - steering;
				rightPWM = throttle + steering;
				
			}
			
			double scalefactor = Math.abs(leftPWM) > 1 ? Math.abs(leftPWM) : 
				Math.abs(rightPWM) > 1 ? Math.abs(rightPWM) : 1;
			
			leftPWM /= scalefactor;
			rightPWM /= scalefactor;
			
		}
		
		leftSpeedControllerWrapper.set(leftPWM);
		rightSpeedControllerWrapper.set(rightPWM);
		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
