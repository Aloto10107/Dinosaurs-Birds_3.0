package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import java.lang.Math.abs

@TeleOp(name = "TeleOp")
class TeleOpMain : OpMode() {

    val power = 1.0

    val leftX: Float get() = gamepad1.left_stick_x
    val leftY: Float get() = gamepad1.left_stick_x
    val rightX: Float get() = gamepad1.left_stick_x
    val rightY: Float get() = gamepad1.left_stick_x


    private val nav by lazy {
        Navigation(hardwareMap)
    }

    private val vars = Variables

    override fun init() {
        vars.init(hardwareMap.appContext)
    }

    override fun loop() {
        mecanumTankDrive()
    }
    
    fun mecanumTankDrive() {
        if(abs(leftX) > abs(leftY)) {
            nav.setLeftDriveMotors(power, power)
        } else {
            nav.setLeftDriveMotors(power, -power)
        }

        if(abs(rightX) > abs(rightY)) {
            nav.setRightDriveMotors(power, power)
        } else {
            nav.setRightDriveMotors(-power, power)
        }
    }
    
    fun omniStickDrive() {
        
    }


}
