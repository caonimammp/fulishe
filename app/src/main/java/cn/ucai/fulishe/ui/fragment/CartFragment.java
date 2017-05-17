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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.data.bean.CartBean;
import cn.ucai.fulishe.data.bean.GoodsDetailsBean;
import cn.ucai.fulishe.data.bean.MessageBean;
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
    @BindView(R.id.tvAll_Price)
    TextView tvAllPrice;
    @BindView(R.id.tvSave_Price)
    TextView tvSavePrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
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
                if (result != null && result.length > 0) {
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
                tvNomore.setText(error);
            }
        });
    }

    private void upDataUI() {
        if (adapter == null) {
            adapter = new CartAdapter(getContext(), list);
            adapter.setCbkListener(cbkListener);
            adapter.setClickListener(clickListener);
            adapter.setClickListener1(clickListener1);
            rvGoods.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        sumPrice();
    }

    private void sumPrice() {
        int sumPrice = 0;
        int savePrice = 0;
        if (list.size() > 0) {
            for (CartBean bean : list) {
                if (bean.isChecked()) {
                    GoodsDetailsBean goods = bean.getGoods();
                    if (goods != null) {
                        sumPrice += getPrice(goods.getCurrencyPrice()) * bean.getCount();
                        savePrice += (getPrice(goods.getCurrencyPrice()) - getPrice(goods.getRankPrice())) * bean.getCount();
                    }
                }
            }
        }
        tvAllPrice.setText("合计：￥" + sumPrice);
        tvSavePrice.setText("节省：￥" + savePrice);
    }

    private int getPrice(String currencyPrice) {
        String price = currencyPrice.substring(currencyPrice.indexOf("￥") + 1);
        return Integer.parseInt(price);
    }

    CompoundButton.OnCheckedChangeListener cbkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();
            list.get(position).setChecked(isChecked);
            sumPrice();
        }
    };
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            updateCart(position, 1);
        }
    };
    View.OnClickListener clickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (list.get(position).getCount() > 1) {
                updateCart(position, -1);
            } else {
                removeCart(position);
            }

        }
    };

    private void removeCart(final int position) {
        final CartBean bean = list.get(position);
        model.removeCart(getContext(), bean.getId(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                    sumPrice();
                }
            }

            @Override
            public void onError(String error) {
            }
        });
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

    @OnClick(R.id.tv_nomore)
    public void onViewClicked() {
        pd.show();
        loadData();
    }

    private void updateCart(final int position, final int count) {
        final CartBean bean = list.get(position);
        model.updateCart(getContext(), bean.getId(), bean.getCount() + count, bean.isChecked(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    adapter.notifyDataSetChanged();
                    list.get(position).setCount(bean.getCount() + count);
                    sumPrice();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }
}



