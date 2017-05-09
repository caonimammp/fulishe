package cn.ucai.fulishe.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.ui.fragment.GoodsFragment;
import cn.ucai.fulishe.ui.view.CatFiterCategoryButton;

public class Category_ChildActivity extends AppCompatActivity {
    GoodsFragment gf;
    boolean priceAsc, addTimeAsc;
    int sortBy;
    Unbinder bind;
    @BindView(R.id.tvPrice)
    Button tvPrice;
    @BindView(R.id.addTime)
    Button addTime;
    @BindView(R.id.cfcb)
    CatFiterCategoryButton cfcb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorychild);
        bind = ButterKnife.bind(this);
        int catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 1);
        gf = new GoodsFragment(catId);
        String title = getIntent().getStringExtra(I.Boutique.TITLE);
        getSupportFragmentManager().beginTransaction().add(R.id.layoutContact,
                gf).commit();
    }

    @OnClick({R.id.tvPrice, R.id.addTime})
    public void onViewClicked(View view) {
        Drawable end;
        switch (view.getId()) {
            case R.id.tvPrice:
                priceAsc = !priceAsc;
                sortBy = priceAsc ? I.SORT_BY_PRICE_DESC : I.SORT_BY_PRICE_ASC;
                end = getDrawable(priceAsc ? R.drawable.arrow_order_down : R.drawable.arrow_order_up);
                tvPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
            case R.id.addTime:
                addTimeAsc = !addTimeAsc;
                sortBy = addTimeAsc ? I.SORT_BY_ADDTIME_DESC : I.SORT_BY_ADDTIME_ASC;
                end = getDrawable(addTimeAsc ? R.drawable.arrow_order_down : R.drawable.arrow_order_up);
                addTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
        }
        gf.sortGoods(sortBy);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }
}
