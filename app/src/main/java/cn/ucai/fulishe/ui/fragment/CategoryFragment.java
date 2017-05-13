package cn.ucai.fulishe.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.data.adapter.CategoryAdapter;
import cn.ucai.fulishe.data.bean.CategoryChildBean;
import cn.ucai.fulishe.data.bean.CategoryGroupBean;
import cn.ucai.fulishe.data.net.GoodsModel;
import cn.ucai.fulishe.data.net.IGoodsModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.ResultUtils;


public class CategoryFragment extends Fragment {
    Unbinder unbinder;
    IGoodsModel model;
    CategoryAdapter adapter;
    ProgressDialog pd;
    TextView tvNomore;
    List<CategoryGroupBean> groupList;
    List<ArrayList<CategoryChildBean>> childList;
    int groupCount = 0;
    @BindView(R.id.rvGoods)
    ExpandableListView rvGoods;
    @BindView(R.id.tv_nore)
    TextView tvNore;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initList();
        initDialog();
        model = new GoodsModel();
        loadGroupData();
    }

    private void initList() {
        childList = new ArrayList<>();
        groupList = new ArrayList<>();
    }

    private void initDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

    private void setListVisibility(boolean visibility) {
        tvNore.setVisibility(visibility ? View.GONE : View.VISIBLE);
        rvGoods.setVisibility(visibility?View.VISIBLE:View.GONE);
    }

    private void loadGroupData() {
        model.loadCategoryGroup(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                pd.dismiss();
                if (result != null) {
                    groupList = ResultUtils.array2List(result);
                    for (int i = 0; i<groupList.size();i++){
                        childList.add(new ArrayList<CategoryChildBean>());
                        loadChildData(groupList.get(i).getId(),i);
                    }
                } else {
                    setListVisibility(false);
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                setListVisibility(false);
            }
        });
    }

    private void loadChildData(int parentId, final int index) {
        model.loadCategoryChild(getContext(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                groupCount++;


                if (result != null) {
                    ArrayList<CategoryChildBean> list = ResultUtils.array2List(result);
                    childList.set(index,list);
                }
                if (groupCount == groupList.size()) {
                    upDataUI();
                    pd.dismiss();
                    setListVisibility(true);
                }
            }

            @Override
            public void onError(String error) {
                groupCount++;
                if (groupCount == groupList.size()) {
                    pd.dismiss();
                    setListVisibility(true);
                }
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    private void upDataUI() {
        if(adapter==null){
            adapter=new CategoryAdapter(groupList,childList,getContext());
            rvGoods.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
