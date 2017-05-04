package cn.ucai.fulishe.data.net;

import android.content.Context;

import cn.ucai.fulishe.data.bean.NewGoodsBean;
import cn.ucai.fulishe.data.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface IGoodsModel {
    void loadNewGoodsData(Context context, int catId, int pageId, int pageSize, OnCompleteListener<NewGoodsBean[]> listener);
}
