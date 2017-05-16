package cn.ucai.fulishe.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.CategoryChildBean;
import cn.ucai.fulishe.data.utils.ImageLoader;
import cn.ucai.fulishe.ui.activity.Category_ChildActivity;

/**
 * Created by Administrator on 2017/5/9.
 */
public class CatFiterAdapter extends BaseAdapter {
    Context context;
    ArrayList<CategoryChildBean> list;
    String groupName;

    public CatFiterAdapter(Context context, ArrayList<CategoryChildBean> list,String groupName) {
        this.context = context;
        this.list = list;
        this.groupName = groupName;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CatFiterViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_catfiter, null);
            holder = new CatFiterViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CatFiterViewHolder) convertView.getTag();
        }
        holder.bind(position);
        return convertView;
    }

    class CatFiterViewHolder {
        @BindView(R.id.ivCatFiterImg)
        ImageView ivCatFiterImg;
        @BindView(R.id.tvCatFiterName)
        TextView tvCatFiterName;
        @BindView(R.id.layout_catfiter)
        RelativeLayout layoutCatfiter;
        CatFiterViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(final int position) {
            ImageLoader.downloadImg(context, ivCatFiterImg, list.get(position).getImageUrl());
            tvCatFiterName.setText(list.get(position).getName());
            layoutCatfiter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,Category_ChildActivity.class)
                            .putExtra(I.CategoryChild.CAT_ID,list.get(position).getId())
                            .putExtra(I.CategoryGroup.NAME,groupName)
                            .putExtra(I.CategoryChild.ID,list));
                    ((Category_ChildActivity)context).finish();
                }
            });
        }
    }



}
