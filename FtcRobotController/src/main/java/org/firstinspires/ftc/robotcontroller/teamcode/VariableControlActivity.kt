package org.firstinspires.ftc.robotcontroller.teamcode

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import com.qualcomm.ftcrobotcontroller.R
import java.util.HashMap

class VariableControlActivity : Activity() {
    val preferences = getSharedPreferences(Variables.VARIABLE_PREFRENCES_TAG, Context.MODE_PRIVATE)

    val layout = findViewById<GridLayout>(R.id.variable_control_layout)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_variable_control)

        Variables.values.asIterable().forEachIndexed() { index, variable ->
            val field = NumberField(variable.key, variable.value)
            var params = GridLayout.LayoutParams(GridLayout.spec(index), GridLayout.spec(0)).also { it.marginStart = 50 }
            field.textView.layoutParams = params
            params = GridLayout.LayoutParams(GridLayout.spec(index), GridLayout.spec(1)).also { it.marginStart = 50 }
            field.editText.layoutParams = params
            layout.addView(field.textView)
            layout.addView(field.editText)
        }
    }

    override fun onPause() {
        super.onPause()
        Variables.values.forEach() {
            preferences.edit().putString(it.key, it.value.toString()).apply()
        }
    }

    fun getContext(): Context {
        return this
    }

    inner class NumberField(val name: String, val variable: Variable) {
        val editText = EditText(getContext()).also {
            it.setText(variable.num.toString())
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(number: Editable?) {
                    try {
                        variable.num = number.toString().toDouble()
                    } catch (e: Exception) {
                    }
                }
            })
        }

        val textView = TextView(getContext()).also {
            it.text = name
        }
    }
}
