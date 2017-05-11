package cn.ucai.fulishe.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.net.IUserModel;
import cn.ucai.fulishe.data.utils.ImageLoader;
import cn.ucai.fulishe.data.utils.SharePrefrenceUtils;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.ivAvator)
    ImageView ivAvator;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvNick)
    TextView tvNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initData();
    }
    private void initData() {
        User user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user != null) {
            tvNick.setText(user.getMuserNick());
            tvUserName.setText(user.getMuserName());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), SettingActivity.this, ivAvator);
        }
    }

    @OnClick({R.id.ivBack, R.id.btnExit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnExit:
                exit();
                break;
        }
    }

    private void exit() {
        FuLiCenterApplication.getInstance().setCurrentUser(null);
        SharePrefrenceUtils.getInstance().removeUser();
        finish();
        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.Layout_Nick)
    public void upDataNick() {
        startActivityForResult(new Intent(SettingActivity.this,UpDataNickActivity.class), I.REQUEST_CODE_NICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==I.REQUEST_CODE_NICK&&resultCode==RESULT_OK){
            initData();
        }
    }
}
