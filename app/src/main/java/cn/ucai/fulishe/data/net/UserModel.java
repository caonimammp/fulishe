package cn.ucai.fulishe.data.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.MessageBean;
import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.OkHttpUtils;
import okhttp3.internal.Util;

/**
 * Created by Administrator on 2017/5/10.
 */

public class UserModel implements IUserModel {
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

    @Override
    public void upDataNick(Context context, String username, String nick, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.NICK,nick)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void upDataAvatar(Context context, String username, String avatarType, File file, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,username)
                .addParam(I.AVATAR_TYPE,I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void upCollectsCount(Context context, String username, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,username)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void addColltct(Context context, String Id, String username, OnCompleteListener<MessageBean> listener) {
        collectAction(I.ACTION_ADD_COLLECT,context,Id,username,listener);
    }

    @Override
    public void removeCollect(Context context, String Id, String username, OnCompleteListener<MessageBean> listener) {
            collectAction(I.ACTION_DELETE_COLLECT,context,Id,username,listener);
    }
    private void collectAction(int action,Context context,String goodsId,String username,OnCompleteListener<MessageBean> listener){
        String url = I.REQUEST_ADD_COLLECT;
        if(action==I.ACTION_DELETE_COLLECT){
            url = I.REQUEST_DELETE_COLLECT;
        }
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(url)
                .addParam(I.Collect.USER_NAME,username)
                .addParam(I.Collect.GOODS_ID,goodsId)
                .targetClass(MessageBean.class)
                .execute(listener);
    }
}
