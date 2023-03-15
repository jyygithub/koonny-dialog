package com.koonny.wheelview.annotation

@Retention(AnnotationRetention.SOURCE)
annotation class CurtainCorner {
    companion object {
        var NONE = 0
        var ALL = 1
        var TOP = 2
        var BOTTOM = 3
        var LEFT = 4
        var RIGHT = 5
    }
}