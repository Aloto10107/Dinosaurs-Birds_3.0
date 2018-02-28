package org.firstinspires.ftc.robotcontroller.teamcode

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.qualcomm.ftcrobotcontroller.R
import kotlinx.android.synthetic.main.activity_variable_control.*

class VariableControlActivity : Activity() {
    val preferences by lazy {
        getSharedPreferences(Variables.VARIABLE_PREFRENCES_TAG, Context.MODE_PRIVATE)
    }

    var selectedVariable: Variable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_variable_control)

        scrollBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        if(Variables.values.isEmpty()) {
            Variables.init(this)
        }

        Variables.values.asIterable().forEachIndexed() { index, variable ->
            val field = NumberField(variable.key, variable.value)
            var params = GridLayout.LayoutParams(GridLayout.spec(index), GridLayout.spec(0)).also { it.marginStart = 50 }
            field.textView.layoutParams = params
            params = GridLayout.LayoutParams(GridLayout.spec(index), GridLayout.spec(1)).also { it.marginStart = 50 }
            field.editText.layoutParams = params
            variable_control_layout.addView(field.textView)
            variable_control_layout.addView(field.editText)
        }
    }

    override fun onPause() {
        super.onPause()
        Variables.values.forEach() {
            preferences!!.edit().putString(it.key, it.value.toString()).apply()
        }
    }

    fun getContext(): Context {
        return this
    }

    inner class NumberField(val name: String, val variable: Variable) {
        val editText = EditText(getContext()).also {
            it.setText(variable.num.toString())
            it.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    scrollBarLayout.visibility = View.VISIBLE
                } else {
                    scrollBarLayout.visibility = View.INVISIBLE
                }
            }
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
            it.text = name.replace("_", " ")
        }
    }
}
