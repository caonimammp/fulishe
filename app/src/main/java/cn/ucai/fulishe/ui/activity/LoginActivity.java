package cn.ucai.fulishe.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.MD5;
import cn.ucai.fulishe.data.utils.ResultUtils;
import cn.ucai.fulishe.data.utils.SharePrefrenceUtils;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    String username,password;
    IUserModel model;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ivBack, R.id.btnLogin, R.id.btnRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnRegister:
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 0);
                break;
        }
    }

    private void login() {
        initDialog();
        if (checkInput()){
            model = new UserModer();
            model.login(LoginActivity.this, username, MD5.getMessageDigest(password), new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if(s!=null){
                        Result<User> result = ResultUtils.getResultFromJson(s, User.class);
                        if(result!=null){
                            if(result.getRetCode()== I.MSG_LOGIN_UNKNOW_USER){
                                setUserNameMsg(R.string.login_fail_unknow_user);
                            }else if(result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                                setUserNameMsg(R.string.login_fail_error_password);
                            }else {
                                User user = result.getRetData();
                                loginSuccess(user);
                            }

                        }
                        dismissDialog();
                    }
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

    private void loginSuccess(User user) {
        FuLiCenterApplication.getInstance().setCurrentUser(user);
        SharePrefrenceUtils.getInstance().setUserName(username);
        UserDao dao=new UserDao(LoginActivity.this);
        dao.saveUser(user);
        finish();
    }

    private void initDialog(){
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage(getString(R.string.logining));
        pd.show();
    }
    private void dismissDialog(){
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }
    private void setUserNameMsg(int MsgId){
        etUserName.requestFocus();
        etUserName.setError(getString(MsgId));
    }

    private boolean checkInput() {
        username = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            setUserNameMsg(R.string.user_name_connot_be_empty);
            return false;
        }
        if(!username.matches("[a-zA-Z]\\w{5,15}")){
            etUserName.requestFocus();
            etUserName.setError(getString(R.string.illegal_user_name));
            return false;
        }
        if(TextUtils.isEmpty(password)){
            etPassword.requestFocus();
            etPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String username = data.getStringExtra(I.User.USER_NAME);
            etUserName.setText(username);
        }
    }
}
