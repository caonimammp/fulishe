package cn.ucai.fulishe.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.BoutiqueBean;
import cn.ucai.fulishe.data.utils.ImageLoader;
import cn.ucai.fulishe.ui.activity.Boutique_ChildActivity;

/**
 * Created by Administrator on 2017/5/5.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter<BoutiqueAdapter.BoutiqueViewHolder> {
    List<BoutiqueBean> list;
    Context context;

    public BoutiqueAdapter(List<BoutiqueBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public BoutiqueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoutiqueViewHolder(View.inflate(context, R.layout.fragment_boutique, null));
    }

    @Override
    public void onBindViewHolder(BoutiqueViewHolder holder, int position) {
        final BoutiqueBean bean = list.get(position);
        holder.tvBoutiqueMes.setText(bean.getDescription());
        holder.tvBoutiqueName.setText(bean.getName());
        holder.tvBoutiqueTitle.setText(bean.getTitle());
        ImageLoader.downloadImg(context,holder.ivBoutiqueImg,bean.getImageurl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Boutique_ChildActivity.class)
                        .putExtra(I.NewAndBoutiqueGoods.CAT_ID,bean.getId())
                        .putExtra(I.Boutique.TITLE,bean.getTitle()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public void initData(ArrayList<BoutiqueBean> list) {
        if(list!=null){
            this.list.clear();
        }
        this.list.addAll(list);
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivBoutiqueImg)
        ImageView ivBoutiqueImg;
        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tvBoutiqueMes)
        TextView tvBoutiqueMes;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
