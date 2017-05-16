package cn.ucai.fulishe.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.CartBean;
import cn.ucai.fulishe.data.bean.GoodsDetailsBean;
import cn.ucai.fulishe.data.utils.ImageLoader;
import cn.ucai.fulishe.ui.activity.GoodsDetailActivity;
import cn.ucai.fulishe.ui.activity.MainActivity;

/**
 * Created by Administrator on 2017/5/15.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartGoodViewHolder> {
    private Context context;
    private List<CartBean> list;

    public CartAdapter(Context context, List<CartBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CartGoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CartGoodViewHolder(View.inflate(context, R.layout.item_cart_goods, null));
    }

    @Override
    public void onBindViewHolder(CartGoodViewHolder holder, int position) {
            holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size()+1:1;
    }

    class CartGoodViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_checkbox)
        CheckBox cbCheckbox;
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_shopPrice)
        TextView tvShopPrice;
        @BindView(R.id.tv_currrntPrice)
        TextView tvCurrentPrice;
        @BindView(R.id.iv_add)
        ImageView ivAdd;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.iv_reduce)
        ImageView ivReduce;

        CartGoodViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            final CartBean bean = list.get(position);
            if(bean!=null){
                GoodsDetailsBean goods = bean.getGoods();
                if(goods!=null){
                    ImageLoader.downloadImg(context,ivImage,goods.getGoodsThumb());
                    tvName.setText(goods.getGoodsName());
                    tvCurrentPrice.setText(goods.getCurrencyPrice());
                    cbCheckbox.setChecked(bean.isChecked());
                    tvCount.setText(bean.getCount());
                    ivImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity)context).startActivityForResult(new Intent(context,GoodsDetailActivity.class)
                                    .putExtra(I.Goods.KEY_GOODS_ID,bean.getGoodsId()),0);
                        }
                    });
                }
            }
        }
    }


}
