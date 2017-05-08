package cn.ucai.fulishe.data.net;

import android.content.Context;

import cn.ucai.fulishe.data.bean.BoutiqueBean;
import cn.ucai.fulishe.data.bean.CategoryChildBean;
import cn.ucai.fulishe.data.bean.CategoryGroupBean;
import cn.ucai.fulishe.data.bean.GoodsDetailsBean;
import cn.ucai.fulishe.data.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface IGoodsModel {
    void loadNewGoodsData(Context context, int catId, int pageId, int pageSize, OnCompleteListener<NewGoodsBean[]> listener);
    void loadBoutiqueData(Context context, OnCompleteListener<BoutiqueBean[]> listener);
    void loadGoodsDetail(Context context, int goodsId, OnCompleteListener<GoodsDetailsBean> listener);
    void loadCategoryGroup(Context context, OnCompleteListener<CategoryGroupBean[]> listener);
    void loadCateChild(Context context,int parentId,OnCompleteListener<CategoryChildBean[]> listener);
}
