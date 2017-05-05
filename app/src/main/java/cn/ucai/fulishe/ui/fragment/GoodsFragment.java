package cn.ucai.fulishe.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.adapter.GoodsAdapter;
import cn.ucai.fulishe.data.bean.NewGoodsBean;
import cn.ucai.fulishe.data.net.GoodsModel;
import cn.ucai.fulishe.data.net.IGoodsModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.ResultUtils;

/**
 * Created by Administrator on 2017/5/4.
 */

public class GoodsFragment extends Fragment {
    GoodsAdapter adapter;
    IGoodsModel model;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.srf)
    SwipeRefreshLayout srf;
    @BindView(R.id.tv_nomore)
    TextView tvNomore;
    Unbinder unbinder;
    int catId = I.CAT_ID;
    int pageId = 1;
    int pageSize =10;
    ProgressDialog pd;
    GridLayoutManager layoutManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialog();
        initView();
        loadData();
        setListener();
    }

    private void initView() {
        model = new GoodsModel();
        layoutManager = new GridLayoutManager(getContext(), I.COLUM_NUM);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemCount()-1==position?layoutManager.getSpanCount():1;
            }
        });
        rvGoods.setLayoutManager(layoutManager);
        srf.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimaryDark);

    }

    private void initDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.load_more));
        pd.show();
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
                loadData();
            }
        });
    }

    private void setupLoadListener() {
        rvGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if(adapter.getItemCount()-1==lastCompletelyVisibleItemPosition&&newState==RecyclerView.SCROLL_STATE_IDLE&&adapter.isMore()){
                    pageId++;
                    loadData();
                }

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void loadData(){
        model.loadNewGoodsData(getContext(), catId, pageId, pageSize, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                pd.dismiss();
                srf.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                srf.setVisibility(View.VISIBLE);
                tvNomore.setVisibility(View.GONE);
                L.e("main","result="+result);
                if(result!=null){
                    L.e("main","result.length="+result.length);
                    ArrayList<NewGoodsBean> list = ResultUtils.array2List(result);
                    upDataUI(list);
                }else{
                    if(adapter!=null||adapter.getItemCount()==1){
                        tvNomore.setVisibility(View.VISIBLE);
                        srf.setVisibility(View.GONE);
                    }
                }
                if(adapter!=null){
                    if(result!=null&&result.length==pageSize){
                        adapter.setMore(true);
                    }else {
                        adapter.setMore(false);
                    }
                }

            }
            @Override
            public void onError(String error) {
                pd.dismiss();
                srf.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                L.e("mian","error"+error);
            }
        });
    }
    public void upDataUI(ArrayList<NewGoodsBean> list){
        if(adapter==null){
            adapter=new GoodsAdapter(list,getContext());
            rvGoods.setAdapter(adapter);
        }else{
            adapter.addData(list);
        }
    }
}
