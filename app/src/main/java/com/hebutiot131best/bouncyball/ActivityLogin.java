package com.hebutiot131best.bouncyball;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.List;

/**登录界面
 * Created by 朱晓南 on 2016/5/21.
 */
public class ActivityLogin extends Activity{
    String surl;
    String username;
    Typeface iconfont;
    Button button;
    Button buttonu;
    Button buttonp;
    String password;
    HeballtConnect heballtCollectConnect;
    Button buttonL;
    Button buttonR;
    ImageView imageView;
    ScrollView scrollView;
    EditText editTextName;
    EditText editTextPass;
    LinearLayout linearLayout2;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            User user = new User(getApplicationContext());
                            List<String> uid = heballtCollectConnect.getConnectData("UID");
                            user.saveAccount(uid.get(0));
                            MyApp myApp = (MyApp) getApplication();
                            myApp.setUserName(uid.get(0));
                            List<String> nick = heballtCollectConnect.getConnectData("Nickname");
                            Toast.makeText(getApplicationContext(), "欢迎你，" + nick.get(0), Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            finish();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "登录失败，请检查用户信息", Toast.LENGTH_LONG).show();
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityLogin.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    Runnable update_thread = new Runnable() {
        public void run() {
            username = editTextName.getText().toString();
            password = editTextPass.getText().toString();
            String url = surl;
            url += username+"&password="+password;
            String code = heballtCollectConnect.getConnectResult(url);
            Message msg = Message.obtain();
            msg.obj = code;
            handler.sendMessage(msg);
        }
    };
    Handler handlers = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            scrollView.smoothScrollTo(0, linearLayout2.getTop());
        }
    };
    Runnable sroll = new Runnable() {
        public void run() {
            handlers.sendEmptyMessage(1);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        heballtCollectConnect = new HeballtConnect();
        MyApp.getInstance().addActivity(this);
        buttonL = (Button)findViewById(R.id.id_Button_login);
        buttonR = (Button)findViewById(R.id.id_Button_register);
        button = (Button) findViewById(R.id.id_lef_login);
        buttonu = (Button) findViewById(R.id.id_Bu_user);
        buttonp = (Button) findViewById(R.id.id_Bu_pass);
        scrollView =(ScrollView)findViewById(R.id.scroll);
        imageView = (ImageView)findViewById(R.id.img_login);
        linearLayout2 =(LinearLayout)findViewById(R.id.layout_ll);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        buttonu.setTypeface(iconfont);
        buttonp.setTypeface(iconfont);
        buttonu.requestFocus();
        editTextName = (EditText)findViewById(R.id.id_Edtext5);
        editTextName.clearFocus();
        editTextPass = (EditText)findViewById(R.id.id_Edtext6);
        surl = getResources().getString(R.string.ip)+"/login.php?username=";
        buttonL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 发送HTTP请求验证身份，通过后本地保存
                new Thread(update_thread).start();
            }
        });
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivityForResult(intent, 0);
            }
        });
        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editTextName.postDelayed(sroll, 200);
                }
            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_lef_login:
                finish();
                break;
        }
    }
    public void onBackPressed(){
        finish();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                finish();
                break;
            default:
                break;
        }
    }
}
