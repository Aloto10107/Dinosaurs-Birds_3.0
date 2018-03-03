package org.firstinspires.ftc.robotcontroller.teamcode

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.qualcomm.robotcore.hardware.DcMotor
import java.util.HashMap
import java.util.logging.Logger


object Variables {

    val VARIABLE_PREFRENCES_TAG = "Variables"

    val values = HashMap<String, Variable>()

    var preferences: SharedPreferences? = null

    @JvmStatic
    fun init(context: Context) {
        preferences = context.getSharedPreferences(VARIABLE_PREFRENCES_TAG, MODE_PRIVATE)
        Names.values().forEach {
            put(it.name)
        }

    }

    operator fun get(variable: Names): Double {
        return values[variable.name]!!.num
    }

    internal fun put(name: String) {
        var number = preferences!!.getString(name, "0.0")
        if (number == "") number = "0.0"
        values.put(name, Variable(number.toDouble()))
    }
}

enum class Names {
    Motor_Power,

}

class Variable(var num: Double)