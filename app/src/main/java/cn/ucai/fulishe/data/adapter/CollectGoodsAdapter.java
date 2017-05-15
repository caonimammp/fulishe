package cn.ucai.fulishe.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.CollectBean;
import cn.ucai.fulishe.data.bean.NewGoodsBean;
import cn.ucai.fulishe.data.utils.ImageLoader;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.ui.activity.GoodsDetailActivity;

/**
 * Created by Administrator on 2017/5/4.
 */

public class CollectGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CollectBean> list1;
    Context context;
    boolean isMore = true;
    int ListType;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public CollectGoodsAdapter(ArrayList<CollectBean> list, Context context) {
        this.list1 = list;
        this.context = context;
        L.e("main","11111111111111111111....");

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        L.e("main","11111111111111111111");
        if(viewType== I.TYPE_FOOTER){
            return new FooterViewHolder(View.inflate(context, R.layout.item_footer, null));
        }else {
            return new CollectGoodsViewHolder(View.inflate(context, R.layout.item_collectgoods, null));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1){
            return I.TYPE_FOOTER;
        }else {
            return I.TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==I.TYPE_FOOTER){
            ((FooterViewHolder)holder).tvFooter.setText(getFooter());
        }else{
                L.e("main","GoodsAdapter.list"+list1);
                final CollectBean bean = list1.get(position);
                ((CollectGoodsViewHolder)holder).tvGoodsName.setText(bean.getGoodsName());
                ImageLoader.downloadImg(context, ((CollectGoodsViewHolder)holder).ivNewGoods, bean.getGoodsThumb());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context,GoodsDetailActivity.class)
                                .putExtra(I.GoodsDetails.KEY_GOODS_ID,bean.getGoodsId()));
                    }
                });
            }
        }
    @Override
    public int getItemCount() {
            return list1 != null ? list1.size() + 1 : 1;
    }

    public String getFooter() {
        return isMore ? "加载更多数据" : "没有更多数据";
    }


    public void addData1(ArrayList<CollectBean> list) {
        this.list1.addAll(list);
        notifyDataSetChanged();
    }

    public void initData1(ArrayList<CollectBean> list) {
        if(this.list1!=null){
            this.list1.clear();
        }
          this.list1.addAll(list);
    }



    class CollectGoodsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivNewGoods)
        ImageView ivNewGoods;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.layout_goods)
        LinearLayout layoutGoods;

        CollectGoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}