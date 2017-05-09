package cn.ucai.fulishe.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.ui.fragment.GoodsFragment;

public class Category_ChildActivity extends AppCompatActivity {
    GoodsFragment gf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category__child);
        int catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID,1);
        gf=new GoodsFragment(catId);
        String title = getIntent().getStringExtra(I.Boutique.TITLE);
        getSupportFragmentManager().beginTransaction().add(R.id.layoutContact,
                gf).commit();
    }
}
