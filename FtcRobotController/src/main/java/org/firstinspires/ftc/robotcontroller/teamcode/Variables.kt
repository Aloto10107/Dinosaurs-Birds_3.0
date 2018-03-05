package org.firstinspires.ftc.robotcontroller.teamcode

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.text.SpannableStringBuilder
import android.widget.EditText
import java.util.*


object Variables {

    const val VARIABLE_PREFRENCES_TAG = "Variables"

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
        values.put(name, Variable(number.toDouble(), name))
    }
}

enum class Names {
    Motor_Power,

}

class Variable(num: Double, val name: String) {
    var num = num
        set(value) {
            if(field != value) {
                field = value
                if (editText != null) {
                    editText!!.text = SpannableStringBuilder(value.toString())
                }
            }
        }
    var editText: EditText? = null
    fun getRange(): Range {
        with(name) {
            if(contains("ANGLE")) return Range(num - 10, num + 10)
            if(contains("DISTANCE")) return Range(num - 15, num + 15)
        }
        return Range(0.0, num * 2)
    }
}