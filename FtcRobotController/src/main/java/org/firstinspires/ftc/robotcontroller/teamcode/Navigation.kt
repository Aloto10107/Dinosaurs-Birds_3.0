package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcontroller.teamcode.VariableNames.BACK_LEFT_MOTOR
import org.firstinspires.ftc.robotcontroller.teamcode.VariableNames.BACK_RIGHT_MOTOR
import org.firstinspires.ftc.robotcontroller.teamcode.VariableNames.FRONT_LEFT_MOTOR
import org.firstinspires.ftc.robotcontroller.teamcode.VariableNames.FRONT_RIGHT_MOTOR
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder

class Navigation(val hardwareMap: HardwareMap, val telemetry: Telemetry) {

    val frontLeftMotor = hardwareMap[FRONT_LEFT_MOTOR] as DcMotor

    val backLeftMotor = hardwareMap[BACK_LEFT_MOTOR] as DcMotor

    val frontRightMotor = hardwareMap[FRONT_RIGHT_MOTOR] as DcMotor

    val backRightMotor by lazy {
        telemetry.addData("Abbdulla is gay", "")
        telemetry.update()
        hardwareMap[BACK_RIGHT_MOTOR] as DcMotor
    }

    val imu : BNO055IMU by lazy {
        val params = BNO055IMU.Parameters()
        params.mode = BNO055IMU.SensorMode.IMU
        params.angleUnit = BNO055IMU.AngleUnit.DEGREES
        params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC

        params.loggingEnabled = false
        val IMU = hardwareMap.get(BNO055IMU::class.java, "imu")
        IMU.initialize(params)
        IMU.angularOrientation.axesOrder = AxesOrder.ZXY
        IMU
    }

//    fun getHeading(): Double {
//
//    }

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
            Direction.Forward, Direction.FrontToBack -> setRightDriveMotors(power, power)
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