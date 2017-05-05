package cn.ucai.fulishe.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishe.R;


public class Boutique_ChildActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvBoutiqueCate)
    TextView tvBoutiqueCate;
    @BindView(R.id.layoutContact)
    FrameLayout layoutContact;
    @BindView(R.id.activity_boutique_child)
    RelativeLayout activityBoutiqueChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique__child);
        ButterKnife.bind(this);
    }
}
