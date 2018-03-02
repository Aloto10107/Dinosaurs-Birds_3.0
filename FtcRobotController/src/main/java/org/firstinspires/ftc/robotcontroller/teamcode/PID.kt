package org.firstinspires.ftc.robotcontroller.teamcode

import com.qualcomm.robotcore.util.ElapsedTime
import javax.xml.transform.dom.DOMLocator
import kotlin.math.abs

/**
 * Created by walker on 2/22/18.
 */
class PID(private var Kp: Double, private var Kd: Double, private var Ki: Double, val controller: (power: Double, setPoint: Double) -> Double, val graphOn: Boolean) {

    constructor(controller: (power: Double, setPoint: Double) -> Double, testSetPoint: Double) : this(0.00000001, 0.0, 0.0, controller, true) {
        var KdNotFound = true
        var setPoint = testSetPoint
        var upperBoundFound = false
        var lowerBoundIncrementer = 0.0
        var timeout = 3.0
        while (KdNotFound) {
            gotoSetPoint(setPoint, timeout)
            val period = getPeriod()
            setPoint *= -1
            if (period == 0.0) {
                Kp *= 10
            } else {
                val Tu = period
                Kp = 0.6 * Kp
                Ki = 1.2 * Kp / Tu
                Kd = 3.0 / 40.0 * Kp * Tu
                KdNotFound = false
            }
        }
    }

    val timer = ElapsedTime()

    val errorPoints = ArrayList<Pair<Double, Double>>()

    val derivativePoints = ArrayList<Pair<Double, Double>>()

    val integralPoints = ArrayList<Pair<Double, Double>>()

    val timeInterval = 0.05

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

        val size = errorPoints.size

        val aveErrorPoints = Array<Pair<Double, Double>>(size) {
            if(it == 0) Pair(errorPoints[it].first, (errorPoints[it].second + errorPoints[it + 1].second) / 2.0)
            else if(it == size - 1) Pair(errorPoints[it].first, (errorPoints[it - 1].second + errorPoints[it].second) / 2.0)
            else Pair(errorPoints[it].first, (errorPoints[it - 1].second + errorPoints[it].second + errorPoints[it + 1].second) / 3.0)
        }

        val aveDerPoints = Array<Pair<Double, Double>>(size) {
            if(it == 0) Pair(aveErrorPoints[it].first, (aveErrorPoints[it].second - aveErrorPoints[it + 1].second) / (aveErrorPoints[it].first - aveErrorPoints[it].first))
            else if(it == size - 1) Pair(aveErrorPoints[it].first, (aveErrorPoints[it - 1].second - aveErrorPoints[it].second) / (aveErrorPoints[it - 1].first - aveErrorPoints[it].first))
            else Pair(aveErrorPoints[it].first, (aveErrorPoints[it - 1].second - aveErrorPoints[it + 1].second) / (aveErrorPoints[it - 1].first - aveErrorPoints[it + 1].first))
        }

        val zeros = ArrayList<Double>()

        val absErrorPoints = aveErrorPoints.map { Pair(it.first, abs(it.second)) }

        absErrorPoints.forEach {

        }

        return 0.0
    }

    fun gotoSetPoint(setPoint: Double, timeout: Double): Boolean {
        timer.reset()
        do {
            wait(timeInterval)
            integrate()
            addPoints()
            val power = error * Kp + errorDerivative * Kd + errorIntegral * Ki
            error = controller(power, setPoint)
            prevTime = time
            prevError = error
        } while (error > 0.01 && errorDerivative > 0.01 && time < timeout)
        return !(time < timeout)
    }

    fun wait(seconds: Double) {
        val startTime = time
        while (time - startTime < seconds);
    }
}

