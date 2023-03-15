package com.koonny.wheelview.annotation

@Retention(AnnotationRetention.SOURCE)
annotation class ScrollState {
    companion object {
        var IDLE = 0
        var DRAGGING = 1
        var SCROLLING = 2
    }
}