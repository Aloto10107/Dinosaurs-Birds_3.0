package org.firstinspires.ftc.robotcontroller.teamcode

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import java.util.HashMap


object Variables {

    val VARIABLE_PREFRENCES_TAG = "Variables"

    val values = HashMap<String, Variable>()

    var preferences: SharedPreferences? = null

    fun init(context: Context) {
        preferences = context.getSharedPreferences(VARIABLE_PREFRENCES_TAG, MODE_PRIVATE)
        VariableNames.values().forEach {
            put(it.name.replace("_", " "))
        }
    }

    fun get(name: String): Double {
        return values[name]!!.num
    }

    internal fun put(name: String) {
        values.put(name, Variable(preferences!!.getString(name, "0.0").toDouble()))
    }

    enum class VariableNames {
        Motor_Power
    }
}

class Variable(var num: Double)