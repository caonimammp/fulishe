package cn.ucai.fulishe.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import cn.ucai.fulishe.R;
import cn.ucai.fulishe.ui.adapter.CatFiterAdapter;
import cn.ucai.fulishe.data.bean.CategoryChildBean;
import cn.ucai.fulishe.data.utils.CommonUtils;

/**
 * Created by Administrator on 2017/5/9.
 */

public class CatFiterCategoryButton extends Button {
    PopupWindow mPopupWindow;
    GridView gv;
    Context context;
    CatFiterAdapter adapter;
    boolean isExpan=false;
    public CatFiterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setPopWindowListener();
    }

    private void setPopWindowListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExpan){
                    if(mPopupWindow!=null&&mPopupWindow.isShowing()){
                        mPopupWindow.dismiss();
                    }
                }else {
                    initPopwin();
                }
                setArrow();
            }
        });
    }

    private void setArrow() {
        Drawable end = context.getDrawable(isExpan ? R.drawable.arrow2_down : R.drawable.arrow2_up);
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
        isExpan = !isExpan;
    }

    private void initPopwin() {
        if(mPopupWindow==null){
            mPopupWindow = new PopupWindow(context);
            mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
            mPopupWindow.setContentView(gv);
        }
        mPopupWindow.showAsDropDown(this);
    }
        public void initView(String groupName, ArrayList<CategoryChildBean> list){
            if(groupName==null || list == null || list.size()==0){
                CommonUtils.showLongToast("数据获取异常，请重试！");
                return;
            }
            this.setText(groupName);
            adapter = new CatFiterAdapter(context,list,groupName);
            gv = new GridView(context);
            gv.setNumColumns(2);
            gv.setHorizontalSpacing(10);
            gv.setVerticalSpacing(10);
            gv.setAdapter(adapter);
        }
    public void release(){
        if(mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }

}
