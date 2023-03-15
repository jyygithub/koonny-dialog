package com.koonny.wheelview.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface CurtainCorner {
    int NONE = 0;
    int ALL = 1;
    int TOP = 2;
    int BOTTOM = 3;
    int LEFT = 4;
    int RIGHT = 5;
}
