package cn.ucai.fulishe.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.ucai.fulishe.R;
import cn.ucai.fulishe.data.bean.NewGoodsBean;
import cn.ucai.fulishe.data.net.GoodsModel;
import cn.ucai.fulishe.data.net.IGoodsModel;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.OkHttpUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onCheckedChange(View view){
        testDownload();
    }

    private void testDownload() {
        IGoodsModel model = new GoodsModel();
        model.loadNewGoodsData(MainActivity.this, 0, 1, 10, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                L.e("main","result="+result);
                if(result!=null){
                    L.e("main","result.length="+result.length);
                }
            }

            @Override
            public void onError(String error) {
                L.e("mian","error"+error);
            }
        });
    }
}
