package org.firstinspires.ftc.robotcontroller.teamcode

import android.app.Activity
import android.os.Bundle
import com.qualcomm.ftcrobotcontroller.R

class VariableControlActivity : Activity() {

    internal inner class Variable(var num: Double)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_variable_control)
    }


}
