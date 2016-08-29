package com.hebutiot131best.bouncyball;

/**修改签名
 * Created by ShyLock-kai on 2016/5/15.
 * Edited by ZxNan on 2016/6/4
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;


public class ActivityUserSignature extends AppCompatActivity implements View.OnClickListener {
    private Button sureImg;
    private EditText editText;
    String one;
    String url;
    Typeface iconfont;
    Button button;
    HeballtConnect heballtCollectConnect;

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            sureImg.setTextColor(getResources().getColor(R.color.buttonafter));
                            Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            handlerf.postDelayed(finish, 1000);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityUserSignature.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    Runnable save_thread = new Runnable() {
        public void run() {
            String code = heballtCollectConnect.getConnectResult(url);
            Message msg = Message.obtain();
            msg.obj = code;
            handler.sendMessage(msg);
        }
    };
    Handler handlerf = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };
    Runnable finish = new Runnable() {
        public void run() {

            handlerf.sendEmptyMessage(1);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);
        MyApp.getInstance().addActivity(this);
        heballtCollectConnect = new HeballtConnect();
        MyApp myApp = (MyApp)getApplication();
        String username = myApp.getUserName();
        sureImg=(Button) findViewById(R.id.id_sure_sign);
        editText=(EditText)findViewById(R.id.id_text_sign);
        button = (Button) findViewById(R.id.id_left_sign);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        sureImg.setTypeface(iconfont);
        url = getResources().getString(R.string.ip)+"/userinfochange.php?operate=2&uid=";
        url+=username;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_sure_sign:
                    url+="&con="+ URLEncoder.encode(editText.getText().toString());
                    new Thread(save_thread).start();
                break;
            case R.id.id_left_sign:
                finish();
                break;
            default:
                break;
        }

    }
}
