package cn.ucai.fulishe.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.ui.fragment.GoodsFragment;

public class Category_ChildActivity extends AppCompatActivity {
    GoodsFragment gf;
    boolean priceAsc,addTimeAsc;
    int sortBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorychild);
        ButterKnife.bind(this);
        int catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 1);
        gf = new GoodsFragment(catId);
        String title = getIntent().getStringExtra(I.Boutique.TITLE);
        getSupportFragmentManager().beginTransaction().add(R.id.layoutContact,
                gf).commit();
    }

    @OnClick({R.id.tvPrice, R.id.addTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvPrice:
                priceAsc = !priceAsc;
                sortBy=priceAsc?I.SORT_BY_PRICE_DESC:I.SORT_BY_PRICE_ASC;
                break;
            case R.id.addTime:
                addTimeAsc = !addTimeAsc;
                sortBy=addTimeAsc?I.SORT_BY_ADDTIME_DESC:I.SORT_BY_ADDTIME_ASC;
                break;
        }
            gf.sortGoods(sortBy);
    }
}
