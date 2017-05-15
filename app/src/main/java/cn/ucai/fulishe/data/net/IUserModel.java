package cn.ucai.fulishe.data.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulishe.data.bean.CartBean;
import cn.ucai.fulishe.data.bean.CollectBean;
import cn.ucai.fulishe.data.bean.MessageBean;
import cn.ucai.fulishe.data.bean.NewGoodsBean;
import cn.ucai.fulishe.data.bean.User;

/**
 * Created by Administrator on 2017/5/10.
 */

public interface IUserModel {
    void login(Context context,String username,String password,OnCompleteListener<String> listener);
    void register(Context context,String username,String nick,String password,OnCompleteListener<String> listener);
    void upDataNick(Context context,String username,String nick,OnCompleteListener<String> listener);
    void upDataAvatar(Context context, String username, String avatarType, File file,OnCompleteListener<String> listener);
    void upCollectsCount(Context context, String username, OnCompleteListener<MessageBean> listener);
    void addColltct(Context context,String Id,String username,OnCompleteListener<MessageBean> listener);
    void removeCollect(Context context,String Id,String username,OnCompleteListener<MessageBean> listener);
    void isCollect(Context context,String Id,String username,OnCompleteListener<MessageBean> listener);
    void upDataCollectGoods(Context context, String username, int pageId, int pageSize, OnCompleteListener<CollectBean[]> listener);
    void addCart(Context context,int goodId,String username,int count,boolean isChecked,OnCompleteListener<MessageBean> listener);
    void removeCart(Context context,int cartId,OnCompleteListener<MessageBean> listener);
    void updateCart(Context context, int cartId,int count, boolean isChecked, OnCompleteListener<MessageBean> listener);
    void loadCart(Context context , String username, OnCompleteListener<CartBean[]> listener);

}
