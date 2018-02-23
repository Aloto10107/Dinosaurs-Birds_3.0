package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor

/**
 * Created by Walker on 2/19/2018.
 */
@TeleOp(name = "TeleOp")
class TeleOpMain : OpMode() {
    private val frontLeftMotor: DcMotor by lazy {
        hardwareMap.get("FRONTLEFT") as DcMotor
    }
    private val backLeftMotor: DcMotor by lazy {
        hardwareMap.get("BACKLEFT") as DcMotor
    }
    private val frontRightMotor: DcMotor by lazy {
        hardwareMap.get("FRONTRIGHT") as DcMotor
    }
    private val backRightMotor: DcMotor by lazy {
        hardwareMap.get("BACKRIGHT") as DcMotor
    }

    override fun init() {

    }

    override fun loop() {

    }
}
