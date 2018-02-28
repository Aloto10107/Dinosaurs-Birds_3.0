package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcontroller.teamcode.StringNames.*

class Navigation(val hardwareMap: HardwareMap) {

    val frontLeftMotor = hardwareMap[FRONT_LEFT_MOTOR] as DcMotor

    val backLeftMotor = hardwareMap[BACK_LEFT_MOTOR] as DcMotor

    val frontRightMotor = hardwareMap[FRONT_RIGHT_MOTOR] as DcMotor

    val backRightMotor = hardwareMap[BACK_RIGHT_MOTOR] as DcMotor

    init {
        Variables.init(hardwareMap.appContext)
    }

    val imu = {
        val params = BNO055IMU.Parameters()
        params.mode = BNO055IMU.SensorMode.IMU
        params.angleUnit = BNO055IMU.AngleUnit.DEGREES
        params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        params.loggingEnabled = false
        hardwareMap.get(BNO055IMU::class.java, "imu")
    }

    fun getHeading() {

    }

    fun drive(distance: Double) {

    }

    fun rotate(angle: Double) {

    }

    fun turn(power: Double) {
        setRightDriveMotors(Direction.Forward, power)
        setLeftDriveMotors(Direction.Backward, power)
    }

    fun setDriveMotors(direction: Direction, power: Double) {
        setLeftDriveMotors(direction, power)
        setRightDriveMotors(direction, power)
    }

    fun setLeftDriveMotors(direction: Direction, power: Double) {
        when (direction) {
            Direction.Forward, Direction.FrontToBack -> setLeftDriveMotors(power, power)
            Direction.Backward -> setLeftDriveMotors(-power, -power)
            Direction.Right, Direction.SideToSide -> setLeftDriveMotors(power, -power)
            Direction.Left -> setLeftDriveMotors(-power, power)
        }
    }

    fun setRightDriveMotors(direction: Direction, power: Double) {
        when (direction) {
            Direction.Forward, Direction.FrontToBack-> setRightDriveMotors(power, power)
            Direction.Backward -> setRightDriveMotors(-power, -power)
            Direction.Right, Direction.SideToSide -> setRightDriveMotors(power, -power)
            Direction.Left -> setRightDriveMotors(-power, power)
        }
    }

    fun setDriveMotors(frontLeft: Double, backLeft: Double, frontRight: Double, backRight: Double) {
        setLeftDriveMotors(frontLeft, backLeft)
        setRightDriveMotors(frontRight, backRight)
    }

    fun setLeftDriveMotors(front: Double, back: Double) {
        frontLeftMotor.power = front
        backLeftMotor.power = back
    }

    fun setRightDriveMotors(front: Double, back: Double) {
        frontRightMotor.power = front
        backRightMotor.power = back
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
        Right,
        Left,
        FrontToBack,
        SideToSide
    }
}
