package cn.ucai.fulishe.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
import cn.ucai.fulishe.data.utils.ImageLoader;
import cn.ucai.fulishe.data.utils.L;
import cn.ucai.fulishe.data.utils.ResultUtils;
import cn.ucai.fulishe.ui.view.CircleImageView;


/**
 * 主界面
 */
public class UpdataAvatorActivity extends AppCompatActivity implements View.OnClickListener {
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //头像1
    private ImageView headImage;
    //头像2
    //调用照相机返回图片临时文件
    private File tempFile;
    Bitmap bitMap;
    IUserModel model;
    File avatarFile;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_avator);
        ButterKnife.bind(this);
        headImage = (ImageView) findViewById(R.id.head_image);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.Layout_UpDataAvatar);
        layout.setOnClickListener(this);
        //创建拍照存储的临时文件
        createCameraTempFile(savedInstanceState);
    }


    /**
     * 外部存储权限申请返回
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCarema();
            } else {
                // Permission Denied
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            } else {
                // Permission Denied
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Layout_UpDataAvatar:
                uploadHeadImage();
                break;
        }
    }


    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_updata_avator, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断

                if (ContextCompat.checkSelfPermission(UpdataAvatorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    L.e("main", "2222222222222222");
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(UpdataAvatorActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);

                } else {
                    //跳转到调用系统相机
                    gotoCarema();
                    L.e("main", "22222222222222221");
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(UpdataAvatorActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(UpdataAvatorActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统图库
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    bitMap = BitmapFactory.decodeFile(cropImagePath);
                    headImage.setImageBitmap(bitMap);
                }
                //此处后面可以将bitMap转为二进制上传后台网络
                //......
                avatarFile = saveBitmapFile(bitMap);
                break;
        }
    }

    private File saveBitmapFile(Bitmap bitmap) {
        if (bitmap != null) {
            String imagePath = getAvatatPath(UpdataAvatorActivity.this, I.AVATAR_TYPE) + "/" + getAvatarName() + ".jpg";
            File file = new File(imagePath);
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }

    private String getAvatatPath(Context context, String path) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File folder = new File(dir, path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder.getAbsolutePath();

    }

    String avatarName;

    private String getAvatarName() {
        avatarName = String.valueOf(System.currentTimeMillis());
        return avatarName;
    }

    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    @OnClick(R.id.btn_UpData)
    public void onViewClicked() {
        model = new UserModer();
        User user = FuLiCenterApplication.getInstance().getCurrentUser();
        if(avatarFile!=null){
            initDialog();
            model.upDataAvatar(UpdataAvatorActivity.this, user.getMuserName(), null, avatarFile,
                    new OnCompleteListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            if (s != null) {
                                Result<User> result = ResultUtils.getResultFromJson(s, User.class);
                                if (result != null) {
                                    if (result.getRetCode() == I.MSG_UPLOAD_AVATAR_FAIL) {
                                        CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                                    } else {
                                        upLoadSuccess(result.getRetData());
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
            CommonUtils.showLongToast("请选择图片");
        }

    }

    public void initDialog() {
        pd = new ProgressDialog(UpdataAvatorActivity.this);
        pd.setMessage(getString(R.string.update_user_avatar));
        pd.show();
    }

    public void dismissDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void upLoadSuccess(User user) {
        CommonUtils.showLongToast(R.string.update_user_avatar_success);
        UserDao dao = new UserDao(UpdataAvatorActivity.this);
        dao.saveUser(user);
        FuLiCenterApplication.getInstance().setCurrentUser(user);
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.ivBack)
    public void onBack() {
        finish();
    }
}