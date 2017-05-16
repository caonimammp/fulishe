package cn.ucai.fulishe.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.CollectBean;
import cn.ucai.fulishe.data.bean.MessageBean;
import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.net.IUserModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.net.UserModel;
import cn.ucai.fulishe.data.utils.ImageLoader;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.ui.activity.GoodsDetailActivity;
import cn.ucai.fulishe.ui.activity.MyCollectActivitty;

/**
 * Created by Administrator on 2017/5/4.
 */

public class CollectGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<CollectBean> list1;
    Context context;
    boolean isMore = true;
    IUserModel model;
    User user;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public CollectGoodsAdapter(ArrayList<CollectBean> list, Context context) {
        this.list1 = list;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == I.TYPE_FOOTER) {
            return new FooterViewHolder(View.inflate(context, R.layout.item_footer, null));
        } else {
            return new CollectGoodsViewHolder(View.inflate(context, R.layout.item_collectgoods, null));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            ((FooterViewHolder) holder).tvFooter.setText(getFooter());
        } else {
            final CollectBean bean = list1.get(position);
            ((CollectGoodsViewHolder) holder).tvGoodsName.setText(bean.getGoodsName());
            ImageLoader.downloadImg(context, ((CollectGoodsViewHolder) holder).ivNewGoods, bean.getGoodsThumb());
            ((CollectGoodsViewHolder) holder).ivDeleteCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model = new UserModel();
                    user = FuLiCenterApplication.getInstance().getCurrentUser();
                    model.removeCollect(context, String.valueOf(bean.getGoodsId()), user.getMuserName(), new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {

                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                    list1.remove(position);
                    notifyDataSetChanged();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MyCollectActivitty)context).startActivityForResult(new Intent(context, GoodsDetailActivity.class)
                            .putExtra(I.GoodsDetails.KEY_GOODS_ID, bean.getGoodsId())
                    ,I.REQUEST_CODE_GO_DETAILS);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list1 != null ? list1.size() + 1 : 1;
    }

    public String getFooter() {
        return isMore ? "加载更多数据" : "没有更多数据";
    }


    public void addData1(ArrayList<CollectBean> list) {
        list1.addAll(list);
        notifyDataSetChanged();
    }

    public void initData1(ArrayList<CollectBean> list) {
        list1.clear();
        list1.addAll(list);
        notifyDataSetChanged();
    }

    class CollectGoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivNewGoods)
        ImageView ivNewGoods;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.layout_goods)
        LinearLayout layoutGoods;
        @BindView(R.id.ivDeleteCollect)
        ImageView ivDeleteCollect;

        CollectGoodsViewHolder(View view) {
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
