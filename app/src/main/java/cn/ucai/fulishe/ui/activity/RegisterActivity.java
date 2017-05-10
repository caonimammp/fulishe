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
import cn.ucai.fulishe.application.I;
import cn.ucai.fulishe.data.bean.Result;
import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.net.IUserModel;
import cn.ucai.fulishe.data.net.OnCompleteListener;
import cn.ucai.fulishe.data.net.UserModer;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.ResultUtils;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etNick)
    EditText etNick;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etREPassword)
    EditText etREPassword;
    String username,nick,password;
    IUserModel model;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ivBack, R.id.btnRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnRegister:
                register();
                break;
        }
    }

    private void register() {
        initDialog();
        if (checkInput()){
            model = new UserModer();
            model.register(RegisterActivity.this, username, nick, password, new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if(s!=null){
                        L.e("main","SSSSSSS:"+s);
                        Result result = ResultUtils.getResultFromJson(s, User.class);
                        L.e("main","result"+result);
                        if(result!=null){
                            if(result.getRetCode()== I.MSG_REGISTER_USERNAME_EXISTS){
                                etUserName.requestFocus();
                                etUserName.setError(getString(R.string.register_fail_exists));
                            }else if(result.getRetCode()==I.MSG_CONNECTION_FAIL){
                                    etUserName.requestFocus();
                                    etUserName.setError(getString(R.string.register_fail));
                                }else {
                                    registerSuccess();
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

    private void initDialog(){
        pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage(getString(R.string.registering));
        pd.show();
    }
    private void dismissDialog(){
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }

    private void registerSuccess() {
        setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,username));
        finish();
    }

    private boolean checkInput() {
        username = etUserName.getText().toString().trim();
        nick = etNick.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        String repassword = etREPassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            etUserName.requestFocus();
            etUserName.setError(getString(R.string.user_name_connot_be_empty));
            return false;
        }
        if(!username.matches("[a-zA-Z]\\w{5,15}")){
            etUserName.requestFocus();
            etUserName.setError(getString(R.string.illegal_user_name));
            return false;
        }
        if(TextUtils.isEmpty(nick)){
            etNick.requestFocus();
            etNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if(TextUtils.isEmpty(password)){
            etPassword.requestFocus();
            etPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        if(TextUtils.isEmpty(repassword)){
            etREPassword.requestFocus();
            etREPassword.setError(getString(R.string.confirm_password_connot_be_empty));
            return false;
        }
        if(!password.equals(repassword)){
            etREPassword.requestFocus();
            etREPassword.setError(getString(R.string.two_input_password));
            return false;
        }
        return true;
    }
}
