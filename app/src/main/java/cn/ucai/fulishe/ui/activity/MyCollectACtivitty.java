package cn.ucai.fulishe.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.data.adapter.CollectGoodsAdapter;
import cn.ucai.fulishe.data.adapter.GoodsAdapter;
import cn.ucai.fulishe.data.bean.CollectBean;
import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.net.IUserModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.net.UserModel;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.ResultUtils;
import cn.ucai.fulishe.ui.fragment.GoodsFragment;

public class MyCollectActivitty extends AppCompatActivity {
    User user;
    IUserModel model;
    int pageId = 1;
    int pageSize = 10;
    GoodsFragment gf;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvBoutiqueCate)
    TextView tvBoutiqueCate;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.srf)
    SwipeRefreshLayout srf;
    @BindView(R.id.tv_nomore)
    TextView tvNomore;
    CollectGoodsAdapter adapter;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect_activitty);
        ButterKnife.bind(this);
        initData();
        String title = "我的收藏";
        tvBoutiqueCate.setText(title);
    }
    private void initDialog() {
        pd = new ProgressDialog(MyCollectActivitty.this);
        pd.setMessage(getString(R.string.updata_collect));
        pd.show();
    }
    private void dismissDialog(){
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }
    private void initData() {
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        model = new UserModel();
        if (user != null) {
            initDialog();
            model.upDataCollectGoods(MyCollectActivitty.this, user.getMuserName(), pageId, pageSize,
                    new OnCompleteListener<CollectBean[]>() {
                        @Override
                        public void onSuccess(CollectBean[] result) {
                            if (result != null) {
                                ArrayList<CollectBean> list = ResultUtils.array2List(result);
                                Log.i("main","MyCollecActivity.list:"+list);
                                upDataUI(list);
                                dismissDialog();
                            }
                        }

                        @Override
                        public void onError(String error) {
                                dismissDialog();
                        }
                    });
        }
    }

    private void upDataUI(ArrayList<CollectBean> list) {
        if(adapter==null){
            adapter=new CollectGoodsAdapter(list,MyCollectActivitty.this);
            rvGoods.setAdapter(adapter);
        }else{
            if(pageId==1){
                adapter.initData1(list);
            }else {

                adapter.addData1(list);
            }
        }
    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }
}
