package cn.ucai.fulishe.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishe.R;
import cn.ucai.fulishe.application.FuLiCenterApplication;
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.Result;
import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.local.UserDao;
import cn.ucai.fulishe.data.net.IUserModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.net.UserModer;
import cn.ucai.fulishe.data.utils.CommonUtils;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.ResultUtils;

public class UpDataNickActivity extends AppCompatActivity {

    @BindView(R.id.etUpDataNick)
    EditText etUpDataNick;
    IUserModel model;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_data_nick);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        model = new UserModer();
        User user = FuLiCenterApplication.getInstance().getCurrentUser();
        if(user!=null){
            etUpDataNick.setText(user.getMuserNick());
            etUpDataNick.selectAll();
        }else {
            finish();
        }
    }

    @OnClick({R.id.ivBack, R.id.btnUpDataNick})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnUpDataNick:
                upDataNick();
                break;
        }
    }
    public void initDialog(){
        pd = new ProgressDialog(UpDataNickActivity.this);
        pd.setMessage(getString(R.string.update_user_nick));
        pd.show();
    }
    public void dismissDialog(){
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }
    private void upDataNick() {
        User user = FuLiCenterApplication.getInstance().getCurrentUser();
        String newNick = etUpDataNick.getText().toString().trim();
        L.e("main","newNick"+newNick);
        initDialog();
        if(checkInput(newNick)){
            model.upDataNick(UpDataNickActivity.this, user.getMuserName(), newNick, new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if(s!=null){
                        L.e("main",s+"s");
                        Result<User> result = ResultUtils.getResultFromJson(s,User.class);
                        if(result!=null){
                            L.e("main",result+"result");
                            if(result.getRetCode()== I.MSG_USER_SAME_NICK){
                                CommonUtils.showLongToast(R.string.update_nick_fail_unmodify);
                            }else if(result.getRetCode()==I.MSG_USER_UPDATE_NICK_FAIL){
                                CommonUtils.showLongToast(R.string.update_fail);
                            }else {
                                upDataNickSuccess(result.getRetData());
                            }
                        }
                    }
                    dismissDialog();
                }

                @Override
                public void onError(String error) {
                    dismissDialog();
                }

            });
        }else {
            dismissDialog();
        }

    }

    private boolean checkInput(String newNick) {
        User user = FuLiCenterApplication.getInstance().getCurrentUser();
            if(TextUtils.isEmpty(newNick)){
                CommonUtils.showLongToast(R.string.nick_name_connot_be_empty);
                return false;
            }
            if(newNick.equals(user.getMuserNick())){
                CommonUtils.showLongToast(R.string.update_nick_fail_unmodify);
                return false;
            }
        return true;
    }

    private void upDataNickSuccess(User user) {
        L.e("main","success:"+user);
        CommonUtils.showLongToast(R.string.update_user_nick_success);
        UserDao dao = new UserDao(UpDataNickActivity.this);
        dao.saveUser(user);
        FuLiCenterApplication.getInstance().setCurrentUser(user);
        setResult(RESULT_OK);
        finish();
    }
}
