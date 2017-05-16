package cn.ucai.fulishe.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.ui.adapter.BoutiqueAdapter;
import cn.ucai.fulishe.data.bean.BoutiqueBean;
import cn.ucai.fulishe.data.net.GoodsModel;
import cn.ucai.fulishe.data.net.IGoodsModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.ResultUtils;


public class BoutiqueFragment extends Fragment {
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.srf)
    SwipeRefreshLayout srf;
    @BindView(R.id.tv_nomore)
    TextView tvNomore;
    Unbinder unbinder;
    IGoodsModel model;
    LinearLayoutManager llm;
    List<BoutiqueBean> list;
    BoutiqueAdapter adapter;
    ProgressDialog pd;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadData();
        initDialog();
        setDownLoadListener();
    }
    private void initDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }
    private void setDownLoadListener() {
        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srf.setRefreshing(true);
                srf.setVisibility(View.VISIBLE);
                loadData();
            }
        });
    }
    private void setLayoutVisibility(boolean visibility) {
        srf.setRefreshing(visibility);
        tvRefresh.setVisibility(visibility?View.VISIBLE:View.GONE);
    }
    private void setListVisibility(boolean visibility) {
        srf.setVisibility(visibility?View.VISIBLE:View.GONE);
        tvNomore.setVisibility(visibility?View.GONE:View.VISIBLE);
    }
    private void loadData() {
        model.loadBoutiqueData(getContext(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                L.e("main","result="+result);
                pd.dismiss();
                setLayoutVisibility(false);
                setListVisibility(true);
                if(result!=null){
                    L.e("main","result.length="+result.length);
                    ArrayList<BoutiqueBean> list = ResultUtils.array2List(result);
                    upDataUI(list);
                }else{
                    if(adapter!=null||adapter.getItemCount()==1){
                        setListVisibility(false);
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                setListVisibility(false);
                L.e("mian","error"+error);
            }
        });
    }

    private void upDataUI(ArrayList<BoutiqueBean> list) {
        if(adapter==null){
            adapter=new BoutiqueAdapter(list,getContext());
            rvGoods.setAdapter(adapter);
        }else{
            adapter.initData(list);
        }
    }

    private void initView() {
        model = new GoodsModel();
        llm = new LinearLayoutManager(getContext());
        rvGoods.setLayoutManager(llm);
        list = new ArrayList<>();
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
}
