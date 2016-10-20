package com.caoyang.tapon;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.caoyang.tapon.singletan.Constant;
import com.orhanobut.logger.Logger;


/**
 * 主APP
 */
public class AppData extends Application {


    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        initAll();

    }

    private void initAll() {
        mContext = getApplicationContext();
        initLogger();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initMultiDex();
    }



    private void initMultiDex() {
        MultiDex.install(this);//解决65535的问题
    }

    private void initLogger() {
        Logger.init(Constant.APPNAME);
    }

    //获取APP级别的上下文
    public static Context getContext() {
        return mContext;
    }


}
