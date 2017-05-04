package cn.ucai.fulishe.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import cn.ucai.fulishe.R;
import cn.ucai.fulishe.data.bean.NewGoodsBean;
import cn.ucai.fulishe.data.net.GoodsModel;
import cn.ucai.fulishe.data.net.IGoodsModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.OkHttpUtils;
import cn.ucai.fulishe.ui.fragment.GoodsFragment;

public class MainActivity extends AppCompatActivity {
    GoodsFragment gf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gf = new GoodsFragment();
    }
    public void onCheckedChange(View view){
        getSupportFragmentManager().beginTransaction().add(R.id.layoutContact,gf).commit();

    }


}
