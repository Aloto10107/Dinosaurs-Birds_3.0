package org.firstinspires.ftc.robotcontroller.teamcode

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.GridLayout
import android.widget.SeekBar
import android.widget.TextView
import com.qualcomm.ftcrobotcontroller.R
import kotlinx.android.synthetic.main.activity_variable_control.*

class VariableControlActivity : Activity() {
    val preferences by lazy {
        getSharedPreferences(Variables.VARIABLE_PREFRENCES_TAG, Context.MODE_PRIVATE)
    }

    var selectedVariable: Variable? = null

    var selectedVariableRange: Range? = null

    var initialVariableValue: Double? = null

    val scrollBarRange = Range(0.0, 100.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_variable_control)

        scrollBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, currentProgress: Int, fromUser: Boolean) {
                if (fromUser) {
                    selectedVariable!!.num = scrollBarRange.mapFrom(currentProgress.toDouble(), selectedVariableRange!!)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        scrollBarLeftButton.setOnClickListener {
            if (scrollBar.progress == 0) {

            } else {
                scrollBar.progress -= 1
                selectedVariable!!.num = scrollBarRange.mapTo(scrollBar.progress.toDouble(), selectedVariableRange!!)
            }
        }

        scrollBarRightButton.setOnClickListener {
            if (scrollBar.progress == 100) {

            } else {
                scrollBar.progress += 1
                selectedVariable!!.num = scrollBarRange.mapTo(scrollBar.progress.toDouble(), selectedVariableRange!!)
            }
        }

        Variables.values.asIterable().forEachIndexed { index, variable ->
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
        updatePreferences()
    }

    fun updatePreferences() {
        Variables.values.forEach {
            preferences.edit().putString(it.key, it.value.num.toString()).apply()
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
                    scrollBar.progress = 50
                    selectedVariable = variable
                    selectedVariableRange = variable.getRange()
                    initialVariableValue = variable.num
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

class Range(var min: Double, var max: Double) {
    fun mapTo(num: Double, start: Double, end: Double): Double {
        return ((num - min) / (max - min)) * (end - start) + start;
    }

    fun mapTo(num: Double, range: Range): Double {
        return mapTo(num, range.min, range.max)
    }

    fun mapFrom(num: Double, start: Double, end: Double): Double {
        return ((num - start) / (end - start)) * (max - min) + min;
    }

    fun mapFrom(num: Double, range: Range): Double {
        return mapFrom(num, range.min, range.max)
    }
}