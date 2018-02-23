package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcontroller.teamcode.HardwareNames.*

/**
 * Created by Walker on 2/19/2018.
 */
@TeleOp(name = "TeleOp")
class TeleOpMain : OpMode() {

    private val nav by lazy {
        Navigation(hardwareMap);
    }

    override fun init() {

    }

    override fun loop() {

    }


}
