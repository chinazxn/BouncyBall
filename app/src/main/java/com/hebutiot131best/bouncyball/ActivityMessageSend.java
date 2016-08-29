package com.hebutiot131best.bouncyball;

/**发送私信
 * Created by ShyLock-kai on 2016/5/15.
 * Edited by ZxNan on 2016/6/4
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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


public class ActivityMessageSend extends AppCompatActivity implements View.OnClickListener {
    private Button sendImg;
    private EditText editText;
    Typeface iconfont;
    Button button;
    String one;
    String url;
    HeballtConnect heballtCollectConnect;

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            sendImg.setTextColor(getResources().getColor(R.color.buttonafter));
                            Toast.makeText(getApplicationContext(), "已发送", Toast.LENGTH_SHORT).show();
                            handlerf.postDelayed(finish, 1000);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityMessageSend.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        setContentView(R.layout.send);
        MyApp.getInstance().addActivity(this);
        heballtCollectConnect = new HeballtConnect();
        sendImg=(Button) findViewById(R.id.id_send_send);
        editText=(EditText)findViewById(R.id.id_text_send);
        button = (Button) findViewById(R.id.id_lef_send);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        sendImg.setTypeface(iconfont);
        MyApp myApp = (MyApp)getApplication();
        String username = myApp.getUserName();
        Intent intent1 = getIntent();
        // 用intent1.getStringExtra()来得到上一个ACTIVITY发过来的字符串。
        one = intent1.getStringExtra("SUID");
        url = getResources().getString(R.string.ip)+"/sendmessage.php?fuid=";
        url+=username+"&suid="+one;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_send_send:
                if(editText.getText().toString().length()!=0) {
                    url+="&con="+ URLEncoder.encode(editText.getText().toString());
                    new Thread(save_thread).start();
                }else{
                    new AlertDialog.Builder(this).setTitle("提示信息").setMessage("消息不可以为空").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
                break;
            case R.id.id_lef_send:
                finish();
                break;
            default:
                break;
        }

    }
}
