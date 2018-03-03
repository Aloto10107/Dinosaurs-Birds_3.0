package org.firstinspires.ftc.robotcontroller.teamcode

import android.util.Log
import com.qualcomm.robotcore.util.ElapsedTime
import java.lang.Integer.signum
import javax.xml.transform.dom.DOMLocator
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign
import kotlin.math.sin

/**
 * Created by walker on 2/22/18.
 */
class PID(private var Kp: Double, private var Kd: Double, private var Ki: Double, val controller: (power: Double, setPoint: Double) -> Double, val graphOn: Boolean) {

    constructor(controller: (power: Double, setPoint: Double) -> Double, testSetPoint: Double) : this(0.00000001, 0.0, 0.0, controller, true) {
        var setPoint = testSetPoint
        var timeout = 3.0
        while (true) {
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
                break
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
    companion object {

        val errorPoints = ArrayList<Pair<Double, Double>>()

        fun createGraph(start: Double, end: Double, dx: Double, f: (Double) -> Double) {
            var x = start
            while (x <= end) {
                errorPoints.add(Pair(x, f(x)))
                x += dx
            }
        }

        fun getPeriod(): Double {

            val size = errorPoints.size

            val aveErrorPoints = Array<Pair<Double, Double>>(size) {
                if (it == 0) Pair(errorPoints[it].first, (errorPoints[it].second + errorPoints[it + 1].second) / 2.0)
                else if (it == size - 1) Pair(errorPoints[it].first, (errorPoints[it - 1].second + errorPoints[it].second) / 2.0)
                else Pair(errorPoints[it].first, (errorPoints[it - 1].second + errorPoints[it].second + errorPoints[it + 1].second) / 3.0)
            }

            Log.i("Error", errorPoints.toString())

            val aveDerPoints = Array<Pair<Double, Double>>(size) {
                if (it == 0) Pair(aveErrorPoints[it].first, (aveErrorPoints[it].second - aveErrorPoints[it + 1].second) / (aveErrorPoints[it].first - aveErrorPoints[it].first))
                else if (it == size - 1) Pair(aveErrorPoints[it].first, (aveErrorPoints[it - 1].second - aveErrorPoints[it].second) / (aveErrorPoints[it - 1].first - aveErrorPoints[it].first))
                else Pair(aveErrorPoints[it].first, (aveErrorPoints[it - 1].second - aveErrorPoints[it + 1].second) / (aveErrorPoints[it - 1].first - aveErrorPoints[it + 1].first))
            }

            aveDerPoints.forEach {
                Log.i("Derivate", it.toString())
                Log.i("Sin", "(" + it.first.toString() + "," + sin(it.first).toString() + ")")
            }

            val zeros = ArrayList<Pair<Double, Double>>()

            val absErrorPoints = Array<Pair<Double, Double>>(size) {
                Pair(aveErrorPoints[it].first, abs(aveErrorPoints[it].second))
            }

            absErrorPoints.forEach {
                Log.i("Abs Error", it.toString())
            }


            var derivativeSign = 0.0

            var largestErrorChange = 0.0
            for(i in 1 until size) {
                largestErrorChange = max(largestErrorChange, abs(aveErrorPoints[i].second - aveErrorPoints[i - 1].second))
            }

            Log.i("Change", largestErrorChange.toString())


            absErrorPoints.forEachIndexed { index, pair ->
                if(index != absErrorPoints.size - 1) {
                    if (pair.second <= largestErrorChange && (derivativeSign == 0.0 || derivativeSign == aveDerPoints[index].second.sign)) {
                        zeros.add(pair)
                        if(derivativeSign == 0.0) {
                            derivativeSign = -aveDerPoints[index].second.sign
                        } else {
                            derivativeSign *= -1
                        }
                    }
                }
            }

            Log.i("Zeros", zeros.toString())

            val zeroDistances = Array<Double>(zeros.size - 1) {
                zeros[it + 1].first - zeros[it].first
            }
            zeroDistances.forEach {
                Log.i("Distance", it.toString())
            }
            var period = zeroDistances.average()

            zeroDistances.forEach {
                if (period * 1.25 < it || period * 0.75 > it) period = 0.0
            }

            return period
        }

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

