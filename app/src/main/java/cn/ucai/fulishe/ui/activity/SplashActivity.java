package cn.ucai.fulishe.ui.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.ucai.fulishe.R;

public class SplashActivity extends AppCompatActivity {
    TextView tvSkip;
    private final static int time = 5000;
    MyCountDataTimer cdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip);
        initView();
        cdt.start();
        setListener();
    }

    private void setListener() {
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdt.cancel();
                cdt.onFinish();
            }
        });
    }


    private void initView() {
        tvSkip = (TextView) findViewById(R.id.tvSkip);
        cdt = new MyCountDataTimer(time,1000);
    }

    class MyCountDataTimer extends CountDownTimer{

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
            tvSkip.setText(getString(R.string.skip)+" "+millisUntilFinished/1000+"s");
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }
    }
}
