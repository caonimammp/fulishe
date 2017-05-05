package cn.ucai.fulishe.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.NewGoodsBean;
import cn.ucai.fulishe.data.utils.ImageLoader;

/**
 * Created by Administrator on 2017/5/4.
 */

public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<NewGoodsBean> list;
    Context context;
    boolean isMore = true;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public GoodsAdapter(List<NewGoodsBean> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType== I.TYPE_FOOTER){
            return new FooterViewHolder(View.inflate(context, R.layout.item_footer, null));
        }else {
            return new GoodsViewHolder(View.inflate(context, R.layout.item_goods, null));
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
            NewGoodsBean bean = list.get(position);
            ((GoodsViewHolder)holder).tvGoodsName.setText(bean.getGoodsName());
            ((GoodsViewHolder)holder).tvGoodsPrice.setText(bean.getCurrencyPrice());
            ImageLoader.downloadImg(context, ((GoodsViewHolder)holder).ivNewGoods, bean.getGoodsThumb());
        }

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size()+1 : 1;
    }

    public void addData(ArrayList<NewGoodsBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public String getFooter() {
        return isMore ? "加载更多数据" : "没有更多数据";
    }

    public void initData(ArrayList<NewGoodsBean> list) {
        if(this.list!=null){
            this.list.clear();
        }
          this.list.addAll(list);
    }


    class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivNewGoods)
        ImageView ivNewGoods;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.layout_goods)
        LinearLayout layoutGoods;

        GoodsViewHolder(View view) {
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
