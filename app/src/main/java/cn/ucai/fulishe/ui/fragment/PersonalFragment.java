package cn.ucai.fulishe.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.data.bean.MessageBean;
import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.net.IUserModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.net.UserModel;
import cn.ucai.fulishe.data.utils.ImageLoader;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.ui.activity.SettingActivity;


public class PersonalFragment extends Fragment {
    User user;
    @BindView(R.id.ivUserAvaratorImg)
    ImageView ivUserAvaratorImg;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    Unbinder unbinder;
    IUserModel model;
    @BindView(R.id.collectCount)
    TextView tvcollectCount;
    int collectCount=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_personal, null);
        unbinder = ButterKnife.bind(this, view);
        model = new UserModel();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        tvcollectCount.setText(String.valueOf(collectCount));
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user != null) {
            tvUserName.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), getContext(), ivUserAvaratorImg);
            initCount();
        }
    }

    private void initCount() {
        model.upCollectsCount(getContext(), user.getMuserName(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    collectCount = Integer.parseInt(result.getMsg());
                }else {
                    collectCount = 0;
                }
                tvcollectCount.setText(String.valueOf(collectCount));
            }

            @Override
            public void onError(String error) {
                collectCount = 0;
                tvcollectCount.setText(String.valueOf(collectCount));
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.Setting)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), SettingActivity.class));
    }
}
