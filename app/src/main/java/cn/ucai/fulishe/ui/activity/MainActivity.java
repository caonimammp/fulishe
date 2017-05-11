package cn.ucai.fulishe.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.ui.fragment.BoutiqueFragment;
import cn.ucai.fulishe.ui.fragment.CategoryFragment;
import cn.ucai.fulishe.ui.fragment.GoodsFragment;
import cn.ucai.fulishe.ui.fragment.PersonalFragment;

public class MainActivity extends AppCompatActivity {
    GoodsFragment gf;
    BoutiqueFragment bf;
    CategoryFragment cf;
    PersonalFragment pf;
    Fragment[] mFragments;
    int index, currentIndex;
    RadioButton[] mRadioButtons;
    @BindView(R.id.rbNewGoods)
    RadioButton rbNewGoods;
    @BindView(R.id.rbBoutique)
    RadioButton rbBoutique;
    @BindView(R.id.rbCategory)
    RadioButton rbCategory;
    @BindView(R.id.rbCart)
    RadioButton rbCart;
    @BindView(R.id.rbContact)
    RadioButton rbContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        showFragment();
    }

    private void initFragment() {
        gf = new GoodsFragment();
        bf = new BoutiqueFragment();
        cf = new CategoryFragment();
        pf = new PersonalFragment();
        mFragments = new Fragment[5];
        mFragments[0] = gf;
        mFragments[1] = bf;
        mFragments[2] = cf;
        mFragments[4] = pf;
        mRadioButtons = new RadioButton[5];
        mRadioButtons[0] = rbNewGoods;
        mRadioButtons[1] = rbBoutique;
        mRadioButtons[2] = rbCategory;
        mRadioButtons[3] = rbCart;
        mRadioButtons[4] = rbContact;
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
                if (FuLiCenterApplication.getInstance().getCurrentUser() == null) {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class),I.REQUEST_CODE_LOGIN);
                } else {
                    index = 4;
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
        setRedioButton();
    }

    private void setRedioButton() {
        for (int i = 0; i < mRadioButtons.length; i++) {
            mRadioButtons[i].setChecked(i == index ? true : false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode== I.REQUEST_CODE_LOGIN){
            index=4;
            setFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(index==4&&FuLiCenterApplication.getInstance().getCurrentUser()==null){
            index=0;
            setFragment();
        }
    }
}
