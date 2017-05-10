package cn.ucai.fulishe.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.ui.fragment.BoutiqueFragment;
import cn.ucai.fulishe.ui.fragment.CategoryFragment;
import cn.ucai.fulishe.ui.fragment.GoodsFragment;

public class MainActivity extends AppCompatActivity {
    GoodsFragment gf;
    BoutiqueFragment bf;
    Fragment[] mFragments;
    CategoryFragment cf;
    int index, currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        showFragment();
    }

    private void initFragment() {
        gf = new GoodsFragment();
        bf = new BoutiqueFragment();
        cf = new CategoryFragment();
        mFragments = new Fragment[5];
        mFragments[0] = gf;
        mFragments[1] = bf;
        mFragments[2] = cf;
    }

    public void showFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layoutContact, mFragments[0])
                .add(R.id.layoutContact, mFragments[1])
                .add(R.id.layoutContact, mFragments[2])
                .show(gf)
                .hide(bf)
                .hide(cf)
                .commit();

    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.rbNewGoods:
                index = 0;
                break;
            case R.id.rbBoutique:
                index = 1;
                break;
            case R.id.rbCategory:
                index = 2;
                break;
            case R.id.rbContact:
                if(FuLiCenterApplication.getInstance().getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }else {
                    index =4;
                }
                break;
        }
        setFragment();
    }

    private void setFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.layoutContact, mFragments[index]);
        }
        if (index != currentIndex) {
            ft.hide(mFragments[currentIndex]);
            ft.show(mFragments[index]).commit();
        }
        currentIndex = index;
    }


}
