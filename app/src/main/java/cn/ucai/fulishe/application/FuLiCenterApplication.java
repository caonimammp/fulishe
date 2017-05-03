package cn.ucai.fulishe.application;

import android.app.Application;

/**
 * Created by Administrator on 2017/5/3.
 */

public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static FuLiCenterApplication getInstance(){
        return instance;
    }
}
