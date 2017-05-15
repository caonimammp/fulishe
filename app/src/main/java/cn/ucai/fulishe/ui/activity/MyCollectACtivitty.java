package cn.ucai.fulishe.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.application.I;
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
import cn.ucai.fulishe.ui.view.SpaceItemDecoration;

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
    GridLayoutManager manager;
    Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect_activitty);
        bind = ButterKnife.bind(this);
        initView();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        manager = new GridLayoutManager(MyCollectActivitty.this, I.COLUM_NUM);
        String title = "我的收藏";
        tvBoutiqueCate.setText(title);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(adapter==null||position==adapter.getItemCount()-1)  {
                    return I.COLUM_NUM;
                }
                return 1;
            }
        });
        rvGoods.addItemDecoration(new SpaceItemDecoration(20));
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
                                if(adapter!=null||adapter.getItemCount()==1){
                                    setLayoutVisibility(false);
                                }
                                if(adapter!=null){
                                    if(result!=null&&result.length==pageSize){
                                        adapter.setMore(true);
                                    }else {
                                        adapter.setMore(false);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(String error) {
                                dismissDialog();
                        }
                    });
        }
    }
    private void setListener() {
        setupLoadListener();
        setDownLoadListener();
    }
    private void setDownLoadListener() {
        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srf.setRefreshing(true);
                srf.setVisibility(View.VISIBLE);
                pageId=1;
                initData();
            }
        });
    }
    private void setupLoadListener() {
        rvGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastCompletelyVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
                if(adapter!=null&&adapter.getItemCount()-1==lastCompletelyVisibleItemPosition&&newState==RecyclerView.SCROLL_STATE_IDLE&&adapter.isMore()){
                    pageId++;
                    initData();
                }

            }
        });
    }
    private void setListVisibility(boolean visibility) {
        srf.setVisibility(visibility?View.VISIBLE:View.GONE);
        tvNomore.setVisibility(visibility?View.GONE:View.VISIBLE);
    }
    private void setLayoutVisibility(boolean visibility) {
        srf.setRefreshing(visibility);
        tvRefresh.setVisibility(visibility?View.VISIBLE:View.GONE);
    }
    private void upDataUI(ArrayList<CollectBean> list) {
        if(adapter==null){
            adapter=new CollectGoodsAdapter(list,MyCollectActivitty.this);
            rvGoods.setAdapter(adapter);
            rvGoods.setLayoutManager(manager);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bind!=null){
            bind.unbind();
        }
    }
}
