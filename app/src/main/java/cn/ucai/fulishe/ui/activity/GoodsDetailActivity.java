package cn.ucai.fulishe.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.AlbumsBean;
import cn.ucai.fulishe.data.bean.GoodsDetailsBean;
import cn.ucai.fulishe.data.bean.MessageBean;
import cn.ucai.fulishe.data.bean.PropertiesBean;
import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.net.GoodsModel;
import cn.ucai.fulishe.data.net.IGoodsModel;
import cn.ucai.fulishe.data.net.IUserModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.net.UserModel;
import cn.ucai.fulishe.data.utils.CommonUtils;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.ui.view.FlowIndicator;
import cn.ucai.fulishe.ui.view.SlideAutoLoopView;

/**
 * Created by Administrator on 2017/5/8.
 */

public class GoodsDetailActivity extends AppCompatActivity {
    int goodsId;
    IGoodsModel model;
    IUserModel userModel;
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
    @BindView(R.id.iv_good_collect)
    ImageView ivGoodCollect;
    User user;
    boolean isCollect = false;
    @BindView(R.id.tvBoutiqueCate)
    TextView tvBoutiqueCate;
    @BindView(R.id.iv_good_cart)
    ImageView ivGoodCart;
    GoodsDetailsBean mGoodsDrtailsBean;

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
            userModel = new UserModel();
            loadData();
        }
    }

    private void loadData() {
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        model.loadGoodsDetail(GoodsDetailActivity.this, goodsId, new OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                mGoodsDrtailsBean = result;
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
        isCollect();
    }

    private void isCollect() {
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user != null) {
            userModel.isCollect(GoodsDetailActivity.this, String.valueOf(goodsId), user.getMuserName(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            isCollect = result != null && result.isSuccess() ? true : false;
                            upCollectUI();
                        }

                        @Override
                        public void onError(String error) {
                            isCollect = false;
                            upCollectUI();
                        }
                    });
        }
    }

    private void upCollectUI() {
        ivGoodCollect.setImageResource(isCollect ? R.mipmap.bg_collect_out : R.mipmap.bg_collect_in);
    }

    private void showData(GoodsDetailsBean bean) {
        tvGoodNameEnglish.setText(bean.getGoodsEnglishName());
        tvGoodName.setText(bean.getGoodsName());
        tvGoodPriceCurrent.setText(bean.getCurrencyPrice());
        wvGoodBrief.loadDataWithBaseURL(null, bean.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
        tvGoodPriceShop.setText(bean.getShopPrice());
        salv.startPlayLoop(indicator, getAlbumImgUrl(bean), getAlbumImgCount(bean));
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
        if (bean.getPromotePrice() != null && bean.getProperties().length > 0) {
            PropertiesBean propertiesBean = bean.getProperties()[0];
            if (propertiesBean != null && propertiesBean.getAlbums() != null) {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
        if (salv != null) {
            salv.stopPlayLoop();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK, new Intent().putExtra(I.Goods.KEY_GOODS_ID, goodsId)
                .putExtra(I.Goods.KEY_ISCOLLECT, isCollect));
    }

    @OnClick({R.id.ivBack, R.id.iv_good_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                setResult(RESULT_OK, new Intent().putExtra(I.Goods.KEY_GOODS_ID, goodsId)
                        .putExtra(I.Goods.KEY_ISCOLLECT, isCollect));
                finish();
                break;
            case R.id.iv_good_collect:
                user = FuLiCenterApplication.getInstance().getCurrentUser();
                if (user == null) {
                    startActivityForResult(new Intent(GoodsDetailActivity.this, LoginActivity.class), 0);
                } else {
                    if (isCollect) {
                        userModel.removeCollect(GoodsDetailActivity.this, String.valueOf(goodsId), user.getMuserName(),
                                new OnCompleteListener<MessageBean>() {
                                    @Override
                                    public void onSuccess(MessageBean result) {
                                        if (result != null && result.isSuccess()) {
                                            CommonUtils.showLongToast(result.getMsg());
                                            isCollect = false;
                                            upCollectUI();
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        upCollectUI();
                                    }
                                });
                    } else {
                        userModel.addColltct(GoodsDetailActivity.this, String.valueOf(goodsId), user.getMuserName(),
                                new OnCompleteListener<MessageBean>() {
                                    @Override
                                    public void onSuccess(MessageBean result) {
                                        if (result != null && result.isSuccess()) {
                                            isCollect = true;
                                            CommonUtils.showLongToast(result.getMsg());
                                            upCollectUI();
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        CommonUtils.showLongToast(error);
                                        upCollectUI();
                                    }
                                });
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            upCollectUI();
        }
    }

    @OnClick({R.id.iv_good_cart})
    public void onCartClick(View view) {
        if (user != null) {
            addCart();
        } else {
            startActivityForResult(new Intent(GoodsDetailActivity.this, LoginActivity.class), 0);
        }
    }

    private void addCart() {
        userModel.addCart(GoodsDetailActivity.this, goodsId, user.getMuserName(), I.ACTION_CART_ADD, false, new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    CommonUtils.showLongToast(R.string.add_goods_success);
                    sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART)
                            .putExtra(I.Cart.class.toString(), mGoodsDrtailsBean));
                } else {
                    CommonUtils.showLongToast("add goods fail");
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(R.string.add_goods_fail);
            }
        });
    }

    @OnClick(R.id.iv_good_share)
    public void onViewClicked() {
        showShare();
    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}

