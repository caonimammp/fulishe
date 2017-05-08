package cn.ucai.fulishe.data.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

import cn.ucai.fulishe.data.bean.CategoryChildBean;
import cn.ucai.fulishe.data.bean.CategoryGroupBean;

/**
 * Created by Administrator on 2017/5/8.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    List<CategoryGroupBean> groupList;
    List<List<CategoryChildBean>> childList;
    @Override
    public int getGroupCount() {
        return groupList==null?0:groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
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
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
