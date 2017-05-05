package cn.ucai.fulishe.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.data.bean.BoutiqueBean;
import cn.ucai.fulishe.data.utils.ImageLoader;

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
        BoutiqueBean bean = list.get(position);
        holder.tvBoutiqueMes.setText(bean.getDescription());
        holder.tvBoutiqueName.setText(bean.getName());
        holder.tvBoutiqueTitle.setText(bean.getTitle());
        ImageLoader.downloadImg(context,holder.ivBoutiqueImg,bean.getImageurl());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
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
