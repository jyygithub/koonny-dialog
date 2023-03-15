package com.koonny.wheelview.contract;

import com.koonny.wheelview.WheelView;
import com.koonny.wheelview.annotation.ScrollState;

public interface OnWheelChangedListener {

    void onWheelScrolled(WheelView view, int offset);

    void onWheelSelected(WheelView view, int position);

    void onWheelScrollStateChanged(WheelView view, @ScrollState int state);

    void onWheelLoopFinished(WheelView view);

}
