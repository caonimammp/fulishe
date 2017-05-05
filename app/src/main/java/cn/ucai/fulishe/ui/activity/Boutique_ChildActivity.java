package cn.ucai.fulishe.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.ui.fragment.GoodsFragment;


public class Boutique_ChildActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvBoutiqueCate)
    TextView tvBoutiqueCate;
    @BindView(R.id.layoutContact)
    FrameLayout layoutContact;
    @BindView(R.id.activity_boutique_child)
    RelativeLayout activityBoutiqueChild;
    GoodsFragment gf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique__child);
        ButterKnife.bind(this);
        int catId=getIntent().getIntExtra(I.NewAndBoutiqueGoods.CAT_ID,I.CAT_ID);
        gf=new GoodsFragment(catId);
        String title = getIntent().getStringExtra(I.Boutique.TITLE);
        tvBoutiqueCate.setText(title);
        getSupportFragmentManager().beginTransaction().add(R.id.layoutContact,
                gf).commit();
    }
    @OnClick(R.id.ivBack)
    public void onClick(View view){
        finish();
    }
}
