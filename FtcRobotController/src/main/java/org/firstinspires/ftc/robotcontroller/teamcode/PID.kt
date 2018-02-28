package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.robotcore.util.ElapsedTime

/**
 * Created by walker on 2/22/18.
 */
class PID(private var Kp: Double, private var Kd: Double, private var Ki: Double) {

    constructor(controller: (setPoint: Double, Ku: Double) -> Double, initialSetPoint: Double) : this(0.0,0.0,0.0){
        var Ku = 1.0
        var error = 1.0
        var KdNotFound = true
        var lowerBoundNotFound = true
        var lowerBoundAdder = 0.0
        timer.reset()
        while (KdNotFound) {
            error = controller(initialSetPoint, Ku)
            if (lowerBoundNotFound) {
                if (Math.abs(error) > 0.01) {
                    if(time > 3) {
                        Ku /= 10
                        timer.reset()
                    }
                } else {
                    lowerBoundNotFound = false
                    lowerBoundAdder = Math.pow(1.0, -Math.log10(Ku).toInt().toDouble())
                    timer.reset()
                }
            } else {
                if(time > 3) {
                    Ku += lowerBoundAdder
                    timer.reset()
                }
                if(Math.abs(error) < 0.01) {
                    var periodStartTime = time
                    if (Math.abs(de) > 0.01) {
                        val Tu = time - periodStartTime
                        Kp = 0.6 * Ku
                        Ki = 1.2 * Ku / Tu
                        Kd = 3.0 / 40.0 * Ku * Tu
                    }
                }
            }
        }
    }

    val timer by lazy {
        ElapsedTime()
    }

    var prevError = 0.0
    var prevTime = 0.0
    var error = 0.0
    val time get() = timer.time()
    val dt get() = time - prevTime
    val de get() = error - prevError
    val errorDerivative get() = de / dt
    var errorIntegral = 0.0

    fun integrate() {
        errorIntegral += error * dt
    }


    fun getPower(error: Double): Double {
        if (dt == 0.0) wait(0.1)
        this.error = error
        integrate()
        val power = error * Kp + errorDerivative * Kd + errorIntegral * Ki
        prevTime = time
        prevError = error
        return power
    }

    fun wait(seconds: Double) {
        val startTime = time
        while (time - startTime < seconds);
    }
}

