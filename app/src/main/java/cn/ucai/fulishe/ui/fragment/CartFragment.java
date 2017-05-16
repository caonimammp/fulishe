package cn.ucai.fulishe.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.data.bean.CartBean;
import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.net.IUserModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.net.UserModel;
import cn.ucai.fulishe.data.utils.ResultUtils;
import cn.ucai.fulishe.ui.adapter.CartAdapter;


public class CartFragment extends Fragment {
    ProgressDialog pd;
    @BindView(R.id.jiesuan_button)
    TextView jiesuanButton;
    @BindView(R.id.srf)
    SwipeRefreshLayout srf;
    Unbinder unbinder;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.tv_nomore)
    TextView tvNomore;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    IUserModel model;
    LinearLayoutManager llm;
    ArrayList<CartBean> list;
    User user;
    CartAdapter adapter;
    @BindView(R.id.layoutCartjiesuan)
    LinearLayout layoutCartjiesuan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

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
        tvRefresh.setVisibility(visibility ? View.VISIBLE : View.GONE);

    }

    private void setListVisibility(boolean visibility) {
        srf.setVisibility(visibility ? View.VISIBLE : View.GONE);
        tvNomore.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    private void loadData() {
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        model.loadCart(getContext(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                pd.dismiss();
                setLayoutVisibility(false);
                setListVisibility(true);
                list.clear();
                if (result!=null&&result.length>0) {
                    list.addAll(ResultUtils.array2List(result));
                    upDataUI();
                } else {
                    setListVisibility(false);
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                setListVisibility(false);
                setLayoutVisibility(false);
            }
        });
    }

    private void upDataUI() {
        if (adapter == null) {
            adapter = new CartAdapter(getContext(), list);
            rvGoods.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        model = new UserModel();
        llm = new LinearLayoutManager(getContext());
        rvGoods.setLayoutManager(llm);
        list = new ArrayList<>();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
