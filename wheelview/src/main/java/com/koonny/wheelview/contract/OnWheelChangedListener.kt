package com.koonny.wheelview.contract

import com.koonny.wheelview.widget.WheelView
import com.koonny.wheelview.annotation.ScrollState

interface OnWheelChangedListener {
    fun onWheelScrolled(view: WheelView?, offset: Int)
    fun onWheelSelected(view: WheelView?, position: Int)
    fun onWheelScrollStateChanged(view: WheelView?, @ScrollState state: Int)
    fun onWheelLoopFinished(view: WheelView?)
}