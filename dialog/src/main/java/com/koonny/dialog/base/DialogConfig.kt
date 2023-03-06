package com.koonny.dialog.base

import android.view.Gravity
import androidx.annotation.GravityInt
import androidx.annotation.StyleRes
import com.koonny.dialog.R

class DialogConfig(
    @StyleRes
    var style: Int = R.style.Theme_Koonny_Dialog,
    @androidx.annotation.FloatRange(from = 0.0, to = 1.0)
    var width: Float = 0f,
    @androidx.annotation.FloatRange(from = 0.0, to = 1.0)
    var height: Float = 0f,
    @GravityInt
    var gravity: Int = Gravity.CENTER,
    var dismissOnBackPressed: Boolean = true,
    var dismissOnTouchOutside: Boolean = true,
)