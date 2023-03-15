package com.koonny.wheelview.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface ScrollState {
    int IDLE = 0;
    int DRAGGING = 1;
    int SCROLLING = 2;
}
