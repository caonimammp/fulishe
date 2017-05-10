package cn.ucai.fulishe.data.net;

import android.content.Context;

import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/5/10.
 */

public class UserModer implements IUserModel {
    @Override
    public void login(Context context, String username, String password, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.PASSWORD,password)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void register(Context context, String username, String nick, String password, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.PASSWORD,password)
                .post()
                .targetClass(String.class)
                .execute(listener);
    }
}
