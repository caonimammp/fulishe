package cn.ucai.fulishe.data.net;

import android.content.Context;

import cn.ucai.fulishe.data.bean.User;

/**
 * Created by Administrator on 2017/5/10.
 */

public interface IUserModel {
    void login(Context context,String username,String password,OnCompleteListener<String> listener);
    void register(Context context,String username,String nick,String password,OnCompleteListener<String> listener);
}
