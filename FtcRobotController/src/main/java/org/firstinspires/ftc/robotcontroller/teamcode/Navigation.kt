package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcontroller.teamcode.HardwareNames.*

/**
 * Created by walker on 2/22/18.
 */
class Navigation(val hardwareMap: HardwareMap) {

    val frontLeftMotor = hardwareMap[FRONT_LEFT_MOTOR] as DcMotor

    val backLeftMotor = hardwareMap[BACK_LEFT_MOTOR] as DcMotor

    val frontRightMotor = hardwareMap[FRONT_RIGHT_MOTOR] as DcMotor

    val backRightMotor = hardwareMap[BACK_RIGHT_MOTOR] as DcMotor

    val imu = {
        val params = BNO055IMU.Parameters()
        params.mode                = BNO055IMU.SensorMode.IMU
        params.angleUnit           = BNO055IMU.AngleUnit.DEGREES
        params.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        params.loggingEnabled      = false
        hardwareMap[BNO055IMU::class.java, "imu"]
    }

    fun move(direction: Direction, power : Double) {
        when(direction) {
            Direction.Forward -> setDriveMotors(power, power, power, power)
            Direction.Backward -> setDriveMotors(-power, -power, -power, -power)
            Direction.Left -> setDriveMotors(-power, power, -power, power)
            Direction.Right -> setDriveMotors(power, -power, power, power)
        }
    }

    fun turn(angle: Double) {

    }

    fun setDriveMotors(frontLeft: Double, backLeft: Double, frontRight: Double, backRight: Double) {
        frontLeftMotor.power = frontLeft
        backLeftMotor.power = backLeft
        frontRightMotor.power = frontRight
        backRightMotor.power = backRight
    }

    fun stopDriveMotors() {
        frontLeftMotor.power = 0.0
        backLeftMotor.power = 0.0
        frontRightMotor.power = 0.0
        backRightMotor.power = 0.0
    }

    enum class Direction {
        Forward,
        Backward,
        Left,
        Right
    }
}
