package cn.ucai.fulishe.application;

import android.app.Application;

import cn.ucai.fulishe.data.bean.User;

/**
 * Created by Administrator on 2017/5/3.
 */

public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;
    private static User user = null;
    public static User getCurrentUser(){
        return user;
    }
    public void setCurrentUser(User user){
        this.user = user;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static FuLiCenterApplication getInstance(){
        return instance;
    }
}
