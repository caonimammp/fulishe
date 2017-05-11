package cn.ucai.fulishe.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.local.UserDao;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.SharePrefrenceUtils;

public class SplashActivity extends AppCompatActivity {
    private final static int time = 5000;
    MyCountDataTimer cdt;
    @BindView(R.id.tvSkip)
    TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip);
        ButterKnife.bind(this);
        cdt = new MyCountDataTimer(time, 1000);
        cdt.start();
    }

    @OnClick(R.id.tvSkip) void setTvSkip(){
        cdt.cancel();
        cdt.onFinish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(FuLiCenterApplication.getInstance().getCurrentUser()!=null){
                    String username = SharePrefrenceUtils.getInstance().getUserName();
                    L.e("main",username+"");
                    if(username!=null){
                        UserDao dao = new UserDao(SplashActivity.this);
                        User user = dao.getUser(username);
                        L.e("main",user+"");
                        if(user!=null){
                            FuLiCenterApplication.getInstance().setCurrentUser(user);
                        }
                    }
                }
            }
        }).start();
    }

    class MyCountDataTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDataTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvSkip.setText(getString(R.string.skip) + " " + millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }

    }
}
