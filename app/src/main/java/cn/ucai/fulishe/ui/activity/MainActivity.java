package cn.ucai.fulishe.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ucai.fulishe.R;
import cn.ucai.fulishe.ui.fragment.BoutiqueFragment;
import cn.ucai.fulishe.ui.fragment.GoodsFragment;

public class MainActivity extends AppCompatActivity {
    GoodsFragment gf;
    BoutiqueFragment bf;
    Fragment[] mFragments;
    int index,currentIndex;
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
        mFragments = new Fragment[5];
        mFragments[0]=gf;
        mFragments[1]=bf;
    }

    public void showFragment(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layoutContact,gf)
                .add(R.id.layoutContact,bf)
                .show(gf)
                .hide(bf)
                .commit();

    }
    public void onCheckedChange(View view){
        switch(view.getId()){
            case R.id.rbNewGoods:
                index=0;
                break;
            case R.id.rbBoutique:
                index=1;
                break;
        }
        setFragment();
    }

    private void setFragment() {
        if(index!=currentIndex){
            getSupportFragmentManager().beginTransaction().replace(R.id.layoutContact,mFragments[index])
                    .commit();
            currentIndex=index;
        }
    }


}
