package cn.ucai.fulishe.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.NewGoodsBean;
import cn.ucai.fulishe.data.utils.ImageLoader;
import cn.ucai.fulishe.ui.activity.GoodsDetailActivity;

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
            final NewGoodsBean bean = list.get(position);
            ((GoodsViewHolder)holder).tvGoodsName.setText(bean.getGoodsName());
            ((GoodsViewHolder)holder).tvGoodsPrice.setText(bean.getCurrencyPrice());
            ImageLoader.downloadImg(context, ((GoodsViewHolder)holder).ivNewGoods, bean.getGoodsThumb());
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
    public void sortGoods(final int sortBy){
        Collections.sort(list, new Comparator<NewGoodsBean>() {
            int result = 0;
            @Override
            public int compare(NewGoodsBean left, NewGoodsBean right) {
                switch (sortBy){
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(left.getCurrencyPrice())-getPrice(right.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(right.getCurrencyPrice())-getPrice(left.getCurrencyPrice());
                        break;
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (left.getAddTime()-right.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (right.getAddTime()-left.getAddTime());
                        break;
                }
                return result;
            }
        });
        notifyDataSetChanged();
    }

    private int getPrice(String currencyPrice) {
        String price = currencyPrice.substring(currencyPrice.indexOf("￥")+1);
        return Integer.parseInt(price);
    }
}
