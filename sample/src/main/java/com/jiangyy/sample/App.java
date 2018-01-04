package com.jiangyy.sample;

import android.app.Application;

import com.jiangyy.easydialog.utils.DialogUtils;

/**
 * Created by Administrator on 2017/8/29 0029.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        DialogUtils.initStyle(R.style.Theme_AppCompat_DayNight_Dialog_Alert);
    }
}
