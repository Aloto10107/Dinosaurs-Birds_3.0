package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.robotcore.util.ElapsedTime
import javax.xml.transform.dom.DOMLocator
import kotlin.math.abs

/**
 * Created by walker on 2/22/18.
 */
class PID(private var Kp: Double, private var Kd: Double, private var Ki: Double, val controller: (power: Double, setPoint: Double) -> Double, val graphOn: Boolean) {

    constructor(controller: (power: Double, setPoint: Double) -> Double, setPoint: Double) : this(1.0, 0.0, 0.0, controller, true) {
        var KdNotFound = true
        var lowerNotBoundFound = true
        var upperBoundFound = false
        var lowerBoundIncrementer = 0.0
        var timeout = 3.0
        timer.reset()
        while (KdNotFound) {
            val timedOut = gotoSetPoint(setPoint, timeout)
            if (!timedOut) lowerNotBoundFound = false
            upperBoundFound = timedOut && lowerNotBoundFound
            if (lowerNotBoundFound) {
                Kp /= 10
            } else {
                if (lowerBoundIncrementer == 0.0) {
                    lowerBoundIncrementer = Math.pow(10.0, Math.log10(Kp).toInt().toDouble() + 1)
                } else {
                    Kp += lowerBoundIncrementer
                }
            }
            if (upperBoundFound) {
                var periodStartTime = time
                if (Math.abs(de) > 0.01) {
                    val Tu = time - periodStartTime
                    Kp = 0.6 * Kp
                    Ki = 1.2 * Kp / Tu
                    Kd = 3.0 / 40.0 * Kp * Tu
                    KdNotFound = false
                }
            }
        }
    }

    val timer = ElapsedTime()

    val errorPoints = ArrayList<Pair<Double, Double>>()

    val derivativePoints = ArrayList<Pair<Double, Double>>()

    val integralPoints = ArrayList<Pair<Double, Double>>()

    val timeInterval = 0.01

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

    fun addPoints() {
        if (graphOn) {
            val constantTime = time
            errorPoints.add(Pair(constantTime, error))
            derivativePoints.add(Pair(constantTime, errorDerivative))
            integralPoints.add(Pair(constantTime, errorDerivative))
        }
    }

    fun getPeriod(): Double {

        fun getPoint(time: Double, points: ArrayList<Pair<Double, Double>>): Pair<Double, Double> {
            for (point in points) {
                if (point.first == time) return point
            }
            throw ExceptionInInitializerError("Point not found")
        }

        val sortedErrorPoints = errorPoints.sortedBy { abs(it.second) }
        val descendingSortedErrorPoints = errorPoints.sortedByDescending { abs(it.second) }

        val firstZero: Pair<Double, Double>

        var prevPair = errorPoints.first()

        for (pair in errorPoints) {
            if (abs(prevPair.second) < abs(pair.second)) {
                firstZero = prevPair
                break
            }
            prevPair = pair
        }



        return 0.0
    }

    fun gotoSetPoint(setPoint: Double, timeout: Double): Boolean {
        timer.reset()
        error = controller(0.0, setPoint)
        while (error > 0.1 && errorDerivative > 0.1 && time < timeout) {
            wait(timeInterval)
            integrate()
            addPoints()
            val power = error * Kp + errorDerivative * Kd + errorIntegral * Ki
            error = controller(power, setPoint)
            prevTime = time
            prevError = error
        }
        return errorDerivative > 0.001
    }

    fun wait(seconds: Double) {
        val startTime = time
        while (time - startTime < seconds);
    }
}

