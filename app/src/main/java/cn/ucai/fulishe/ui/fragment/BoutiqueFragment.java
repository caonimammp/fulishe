package cn.ucai.fulishe.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import cn.ucai.fulishe.data.bean.BoutiqueBean;
import cn.ucai.fulishe.data.bean.NewGoodsBean;
import cn.ucai.fulishe.data.net.GoodsModel;
import cn.ucai.fulishe.data.net.IGoodsModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.OkHttpUtils;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadData();

    }

    private void loadData() {
        model.loadBoutiqueData(getContext(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                L.e("main","result="+result);
                if(result!=null){
                    L.e("main","result.length="+result.length);
                    ArrayList<BoutiqueBean> list = ResultUtils.array2List(result);
                    upDataUI(list);
                }
            }

            @Override
            public void onError(String error) {
                L.e("mian","error"+error);
            }
        });
    }

    private void upDataUI(ArrayList<BoutiqueBean> list) {
        if (list==null){

        }
    }

    private void initView() {
        model = new GoodsModel();

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
