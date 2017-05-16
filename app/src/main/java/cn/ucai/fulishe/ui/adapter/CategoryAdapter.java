package cn.ucai.fulishe.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.CategoryChildBean;
import cn.ucai.fulishe.data.bean.CategoryGroupBean;
import cn.ucai.fulishe.data.utils.ImageLoader;
import cn.ucai.fulishe.ui.activity.Category_ChildActivity;

/**
 * Created by Administrator on 2017/5/8.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    List<CategoryGroupBean> groupList;
    List<ArrayList<CategoryChildBean>> childList;
    Context context;
    @BindView(R.id.ivCategoryChildName)
    ImageView ivCategoryChildName;
    @BindView(R.id.tvCategoryChildName)
    TextView tvCategoryChildName;
    @BindView(R.id.ivCategoryGroupName)
    ImageView ivCategoryGroupName;
    @BindView(R.id.tvGpoupName)
    TextView tvGpoupName;
    @BindView(R.id.ivExpand)
    ImageView ivExpand;
    @BindView(R.id.Layout_child)
    LinearLayout LayoutChild;


    public CategoryAdapter(List<CategoryGroupBean> groupList, List<ArrayList<CategoryChildBean>> childList, Context context) {
        this.groupList = groupList;
        this.childList = childList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return groupList == null ? 0 : groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList != null && childList.get(groupPosition) != null ? childList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return groupList != null ? groupList.get(groupPosition) : null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return childList != null && childList.get(groupPosition) != null ? childList.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_category_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.bind(groupPosition, isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_category_child, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.bind(groupPosition, childPosition);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,Category_ChildActivity.class)
                        .putExtra(I.CategoryChild.CAT_ID,getChild(groupPosition,childPosition).getId())
                        .putExtra(I.CategoryGroup.NAME,groupList.get(groupPosition).getName())
                        .putExtra(I.CategoryChild.ID,childList.get(groupPosition)));
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivCategoryGroupName)
        ImageView ivCategoryGroupName;
        @BindView(R.id.tvGpoupName)
        TextView tvGpoupName;
        @BindView(R.id.ivExpand)
        ImageView ivExpand;

        GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int groupPosition, boolean isExpanded) {
            CategoryGroupBean bean = groupList.get(groupPosition);
            ImageLoader.downloadImg(context, ivCategoryGroupName, bean.getImageUrl());
            tvGpoupName.setText(bean.getName());
            ivExpand.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        }
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivCategoryChildName)
        ImageView ivCategoryChildName;
        @BindView(R.id.tvCategoryChildName)
        TextView tvCategoryChildName;

        ChildViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(final int groupPosition, int childPosition) {
            final CategoryChildBean bean = getChild(groupPosition, childPosition);
            if (bean != null) {
                ImageLoader.downloadImg(context, ivCategoryChildName, bean.getImageUrl());
                tvCategoryChildName.setText(bean.getName());
            }

        }
    }
}
