package cn.ucai.fulishe.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.data.bean.CategoryChildBean;
import cn.ucai.fulishe.data.bean.CategoryGroupBean;
import cn.ucai.fulishe.data.utils.ImageLoader;

/**
 * Created by Administrator on 2017/5/9.
 */
public class CatFiterAdapter extends BaseAdapter {
    Context context;
    List<CategoryChildBean> list;

    public CatFiterAdapter(Context context, List<CategoryChildBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
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
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_catfiter, null);
            holder=new CatFiterViewHolder(convertView);
            convertView.setTag(holder);
        }else {
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

        CatFiterViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            ImageLoader.downloadImg(context,ivCatFiterImg,list.get(position).getImageUrl());
        }
    }
}
