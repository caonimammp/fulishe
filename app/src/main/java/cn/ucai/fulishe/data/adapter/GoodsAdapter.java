package cn.ucai.fulishe.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.data.bean.NewGoodsBean;
import cn.ucai.fulishe.data.utils.ImageLoader;

/**
 * Created by Administrator on 2017/5/4.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder> {
    List<NewGoodsBean> list;
    Context context;

    public GoodsAdapter(List<NewGoodsBean> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsViewHolder(View.inflate(context,R.layout.item_goods,null));
    }

    @Override
    public void onBindViewHolder(GoodsViewHolder holder, int position) {
        NewGoodsBean bean =list.get(position);
        holder.tvGoodsName.setText(bean.getGoodsName());
        holder.tvGoodsPrice.setText(bean.getGoodsBrief());
        ImageLoader.downloadImg(context,holder.ivNewGoods,bean.getGoodsThumb());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder{
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
}
