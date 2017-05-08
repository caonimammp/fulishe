package cn.ucai.fulishe.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.AlbumsBean;
import cn.ucai.fulishe.data.bean.GoodsDetailsBean;
import cn.ucai.fulishe.data.bean.PropertiesBean;
import cn.ucai.fulishe.data.net.GoodsModel;
import cn.ucai.fulishe.data.net.IGoodsModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.ui.view.FlowIndicator;
import cn.ucai.fulishe.ui.view.SlideAutoLoopView;

/**
 * Created by Administrator on 2017/5/8.
 */

public class GoodsDetailActivity extends AppCompatActivity {
    int goodsId;
    IGoodsModel model;
    @BindView(R.id.tvBoutiqueCate)
    TextView tvBoutiqueCate;
    @BindView(R.id.tv_good_name_english)
    TextView tvGoodNameEnglish;
    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.tv_good_price_shop)
    TextView tvGoodPriceShop;
    @BindView(R.id.tv_good_price_current)
    TextView tvGoodPriceCurrent;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wv_good_brief)
    WebView wvGoodBrief;
    @BindView(R.id.layout_banner)
    RelativeLayout layoutBanner;
    @BindView(R.id.activity_goods_detail)
    RelativeLayout activityGoodsDetail;
    Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_goods_detail);
        bind = ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        initData();
    }

    private void initData() {
        if (goodsId == 0) {
            L.e("main", "goodsId=" + goodsId);
            finish();
        } else {
            model = new GoodsModel();
            loadData();
        }
    }

    private void loadData() {
        model.loadGoodsDetail(GoodsDetailActivity.this, goodsId, new OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.e("mian", "result=" + result);
                if (result != null) {
                    showData(result);
                } else {

                }

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void showData(GoodsDetailsBean bean) {
        tvGoodNameEnglish.setText(bean.getGoodsEnglishName());
        tvGoodName.setText(bean.getGoodsName());
        tvGoodPriceCurrent.setText(bean.getCurrencyPrice());
        wvGoodBrief.loadDataWithBaseURL(null, bean.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
        tvGoodPriceShop.setText(bean.getShopPrice());
        salv.startPlayLoop(indicator,getAlbumImgUrl(bean),getAlbumImgCount(bean));
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean bean) {
        AlbumsBean[] imgs = getAlbumImg(bean);
        if (imgs != null) {
            String[] urls = new String[imgs.length];
            for (int i = 0; i < imgs.length; i++) {
                urls[i] = imgs[i].getImgUrl();
            }
            return urls;
        }
        return null;
    }

    private AlbumsBean[] getAlbumImg(GoodsDetailsBean bean) {
        if(bean.getPromotePrice()!=null&&bean.getProperties().length>0){
            PropertiesBean propertiesBean = bean.getProperties()[0];
            if(propertiesBean!=null&&propertiesBean.getAlbums()!=null){
                return propertiesBean.getAlbums();
            }
        }
        return null;
    }

    private int getAlbumImgCount(GoodsDetailsBean bean) {
        AlbumsBean[] imgs = getAlbumImg(bean);
        if (imgs != null) {
            return imgs.length;
        }
        return 0;
    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bind!=null){
            bind.unbind();
        }
        if(salv!=null){
            salv.stopPlayLoop();
        }
    }
}
