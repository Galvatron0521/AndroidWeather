package com.zhiyuan3g.androidweather.base;

import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2017/8/23.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
