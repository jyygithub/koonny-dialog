package com.koonny.wheelview.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 滚轮条目文本对齐方式
 */
@Retention(RetentionPolicy.SOURCE)
public @interface ItemTextAlign {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
}
