package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.robotcore.util.ElapsedTime

/**
 * Created by walker on 2/22/18.
 */
class PID(val Kp: Double, val Kd: Double, val Ki: Double) {
    val timer by lazy {
        ElapsedTime()
    }
    var prevError = 0.0
    var prevTime = 0.0
    var errorIntegral = 0.0
    fun getPower(error: Double): Double {
        if (timer.time() == 0.0) return 0.0
        val deltaError = error - prevError
        val deltaTime = timer.time() - prevTime
        prevTime = timer.time()
        val errorDerivative = deltaError / deltaTime
        errorIntegral += error * deltaTime
        return error * Kp + errorDerivative * Kd + errorIntegral * Ki
    }
}

