package com.hebutiot131best.bouncyball;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.net.URLEncoder;
import java.util.List;

/**注册界面
 * Created by 朱晓南 on 2016/5/21.
 */
public class ActivityRegister extends Activity {
    String surl ;
    String username;
    String password;
    String phone;
    String getPassword1;
    Typeface iconfont;
    Button button;
    HeballtConnect heballtCollectConnect;
    Button buttonR;
    EditText editTextName;
    EditText editTextPhone;
    EditText editTextPass;
    EditText editTextPass1;
    ImageView imageView;
    LinearLayout linearLayout;
    ScrollView scrollView;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            new Thread(save_thread).start();
                            break;
                        case "402":
                            Toast.makeText(getApplicationContext(), "该号码已注册", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "请求失败，请检查网络", Toast.LENGTH_LONG).show();
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityRegister.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    Handler handlers = new Handler(){
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
                            Toast.makeText(getApplicationContext(), "请求失败，请检查网络", Toast.LENGTH_LONG).show();
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityRegister.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    Runnable check_thread = new Runnable() {
        public void run() {
            String url = surl;
            url += phone+"&operate=1";
            String code = heballtCollectConnect.getConnectResult(url);
            Message msg = Message.obtain();
            msg.obj = code;
            handler.sendMessage(msg);
        }
    };
    Runnable save_thread = new Runnable() {
        public void run() {
            String url = surl;
            url += phone+"&nick="+ URLEncoder.encode(username) + "&password=" + password + "&operate=2";
            String code = heballtCollectConnect.getConnectResult(url);
            Message msg = Message.obtain();
            msg.obj = code;
            handlers.sendMessage(msg);
        }
    };
    Handler handlersc1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            scrollView.smoothScrollTo(0, linearLayout.getTop());
        }
    };
    Runnable sroll1 = new Runnable() {
        public void run() {
            handlersc1.sendEmptyMessage(1);
        }
    };
    Handler handlersc2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            scrollView.smoothScrollTo(0, linearLayout.getTop()+300);
        }
    };
    Runnable sroll2 = new Runnable() {
        public void run() {
            handlersc2.sendEmptyMessage(1);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        MyApp.getInstance().addActivity(this);
        heballtCollectConnect = new HeballtConnect();
        buttonR = (Button) findViewById(R.id.id_Button_register1);
        editTextName = (EditText) findViewById(R.id.id_Edtext1);
        editTextPhone = (EditText) findViewById(R.id.id_Edtext2);
        editTextPass = (EditText) findViewById(R.id.id_Edtext3);
        editTextPass1 = (EditText) findViewById(R.id.id_Edtext4);
        button = (Button) findViewById(R.id.id_lef_register);
        imageView = (ImageView)findViewById(R.id.img_reg);
        linearLayout =(LinearLayout)findViewById(R.id.layout_l);
        scrollView =(ScrollView)findViewById(R.id.scroll2);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        surl = getResources().getString(R.string.ip)+"/sign.php?username=";
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 发送HTTP请求注册信息，通过后本地保存
                phone = editTextPhone.getText().toString();
                username = editTextName.getText().toString();
                password = editTextPass.getText().toString();
                getPassword1 = editTextPass1.getText().toString();
                if(phone.length()!=11){
                    Toast.makeText(getApplicationContext(),"请输入正确的手机号",Toast.LENGTH_LONG).show();
                } else {
                    if (password.equals(getPassword1)) {
                        new Thread(check_thread).start();
                    } else {
                        Toast.makeText(getApplicationContext(), "两次输入密码不相同", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    handlersc1.postDelayed(sroll1, 200);
                }
            }
        });
        editTextPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    handlersc2.postDelayed(sroll2, 200);
                }
            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_lef_register:
                finish();
                break;
        }
    }
}
