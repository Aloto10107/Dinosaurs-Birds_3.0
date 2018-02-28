package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import java.lang.Math.abs

@TeleOp(name = "TeleOp")
class TeleOpMain : OpMode() {

    val leftX: Double get() = gamepad1.left_stick_x.toDouble()
    val leftY: Double get() = gamepad1.left_stick_x.toDouble()
    val rightX: Double get() = gamepad1.left_stick_x.toDouble()
    val rightY: Double get() = gamepad1.left_stick_x.toDouble()

    var x = true
    var y = false

    private val nav by lazy {
        Navigation(hardwareMap)
    }

    private val vars = Variables

    override fun init() {
        nav
    }

    override fun loop() {
        if(x) {
            mecanumTankDrive()
        } else {
            omniStickDrive()
        }
        if(gamepad1.x) {
            x = true
            y = false
        }

        if(gamepad1.y) {
            y = true
            x = false
        }
    }

    fun mecanumTankDrive() {
        if (abs(leftX) > abs(leftY)) {
            nav.setLeftDriveMotors(Navigation.Direction.FrontToBack, leftX)
        } else {
            nav.setLeftDriveMotors(Navigation.Direction.SideToSide, leftY)
        }

        if (abs(rightX) > abs(rightY)) {
            nav.setRightDriveMotors(Navigation.Direction.FrontToBack, rightX)
        } else {
            nav.setRightDriveMotors(Navigation.Direction.SideToSide, rightY)
        }
    }

    fun omniStickDrive() {
        if (abs(leftX) > abs(leftY)) {
            nav.setDriveMotors(Navigation.Direction.FrontToBack, leftX)
        } else {
            nav.setDriveMotors(Navigation.Direction.SideToSide, leftY)
        }
        nav.turn(rightX)
    }
}
