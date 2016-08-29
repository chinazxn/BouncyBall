package com.hebutiot131best.bouncyball;

/**当前用户详细信息页面
 * Created by ShyLock-kai on 2016/5/15.
 * Edited by ZxNan on 2016/6/4
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ActivityAccount extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    private LinearLayout layout;
    private ImageView imageView;
    Typeface iconfont;
    private Button button1;
    private TextView textView0;
    private TextView textView1;
    private TextView textView2;
    private TextView next1;
    private TextView next2;
    private String url;
    private String urlu;
    private String fileName;
    private String username;
    HeballtConnect heballtCollectConnect;
    HeballtConnect heballtCollectConnect2;
    private ProgressDialog pd;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                switch (code) {
                    case "200":
                        List<String> nick = heballtCollectConnect.getConnectData("Nickname");
                        List<String> sign = heballtCollectConnect.getConnectData("Signature");
                        List<String> phon = heballtCollectConnect.getConnectData("PhoneNumber");
                        List<String> icon = heballtCollectConnect.getConnectData("HeadURL");
                        textView2.setText(phon.get(0));
                        textView0.setText(nick.get(0));
                        textView1.setText(sign.get(0));
                        Bitmap bitmap= new User(getApplicationContext()).getBitmapFromSharedPreferences();
                        if(bitmap==null) {
                            new ImageLoder().showImage(getApplicationContext(), imageView, getResources().getString(R.string.ip) + icon.get(0),username,username);
                        }else{
                            imageView.setImageBitmap(bitmap);
                        }
                        break;
                    default:
                        break;
                }
            } else {
                new AlertDialog.Builder(ActivityAccount.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    //要用handler来处理多线程可以使用runnable接口，这里先定义该接口
    //线程中运行该接口的run函数
    Runnable update_thread = new Runnable() {
        public void run() {
            String code = heballtCollectConnect.getConnectResult(url);
            Message msg = Message.obtain();
            msg.obj = code;
            handler.sendMessage(msg);
        }
    };
    Handler handleri = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                switch (code) {
                    case "200":
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
                        break;
                }
            }else {
                new AlertDialog.Builder(ActivityAccount.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    //更新用户表
    Runnable icon_thread = new Runnable() {
        public void run() {
            String urli = getResources().getString(R.string.ip)+"/icon.php?uid="+username;
            String code = heballtCollectConnect2.getConnectResult(urli);
            Message msg = Message.obtain();
            msg.obj = code;
            handleri.sendMessage(msg);
        }
    };
    //上传图片
    Runnable upload_thread = new Runnable() {
        public void run() {
            urlu = getResources().getString(R.string.ip)+"/doAction.php";
            String upload = Environment.getExternalStorageDirectory()+"/myicon.jpg";
            fileName = "icon"+username+".jpg";
            String code = new UploadThread().post(fileName, urlu, upload);
            switch (code){
                case "200":
                    new Thread(icon_thread).start();
                    break;
                case "400":
                    Toast.makeText(getApplicationContext(), "上传失败，请检查网络", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp myApp = (MyApp)getApplication();
        username = myApp.getUserName();
        MyApp.getInstance().addActivity(this);
        setContentView(R.layout.account);
        heballtCollectConnect = new HeballtConnect();
        heballtCollectConnect2 = new HeballtConnect();
        textView0 = (TextView) findViewById(R.id.id_name_account);
        textView1 = (TextView) findViewById(R.id.id_text_account);
        textView2 = (TextView) findViewById(R.id.id_uname_account);
        next1 = (TextView) findViewById(R.id.account_next1);
        next2 = (TextView) findViewById(R.id.account_next2);
        imageView = (ImageView)findViewById(R.id.id_img1_account);
        button = (Button) findViewById(R.id.id_Button_exit);
        button1 = (Button) findViewById(R.id.id_Bu_lef6);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button1.setTypeface(iconfont);
        next1.setTypeface(iconfont);
        next2.setTypeface(iconfont);
        url = getResources().getString(R.string.ip)+"/userinfo.php?uid=";
        url += username;
        button.setOnClickListener(this);
    }
    protected void onResume(){
        super.onResume();
        new Thread(update_thread).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_Button_exit:
                //删除缓存信息，跳转主界面
                User user = new User(getApplicationContext());
                user.destory();
                MyApp myApp = (MyApp)getApplication();
                myApp.setUserName(null);
                Intent intent= new Intent(this,ActivityMain.class);
                startActivity(intent);
                break;
            case R.id.id_signtab_account:
                Intent intent1= new Intent(this,ActivityUserSignature.class);
                startActivity(intent1);
                break;
            case R.id.id_img1_account:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
                break;
            case R.id.id_nametab_account:
                Intent intent3= new Intent(this,ActivityUserNickname.class);
                startActivity(intent3);
                break;
            case R.id.id_Bu_lef6:
                finish();
                break;
            default:
                break;
        }

    }
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int RESIZE_REQUEST_CODE = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    resizeImage(data.getData());
                    break;
                case RESIZE_REQUEST_CODE:
                    if (data != null) {
                        showResizeImage(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //裁剪头像
    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }
    //显示并上传头像
    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = new ImageLoder().toRoundCorner(photo, 250);//裁剪成圆形
            User user = new User(getApplicationContext());
            user.saveBitmapToSharedPreferences(photo);
            // 上传图片
            new Thread(upload_thread).start();
            pd = ProgressDialog.show(ActivityAccount.this, null, "正在上传图片，请稍候...");
            imageView.setImageBitmap(photo);
        }
    }
}

