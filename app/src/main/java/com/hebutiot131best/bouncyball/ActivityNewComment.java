package com.hebutiot131best.bouncyball;

/**添加评论
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


public class ActivityNewComment extends AppCompatActivity implements View.OnClickListener {
    private Button sureImg;
    Typeface iconfont;
    Button button;
    private EditText editText;
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
                            sureImg.setTextColor(getResources().getColor(R.color.buttonafter));
                            Toast.makeText(getApplicationContext(), "评论成功", Toast.LENGTH_SHORT).show();
                            handlerf.postDelayed(finish, 1000);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityNewComment.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        MyApp.getInstance().addActivity(this);
        MyApp myApp = (MyApp)getApplication();
        String username = myApp.getUserName();
        Intent intent1 = getIntent();
        // 用intent1.getStringExtra()来得到上一个ACTIVITY发过来的字符串。
        one = intent1.getStringExtra("CtID");
            setContentView(R.layout.mycom);
            heballtCollectConnect = new HeballtConnect();
            button = (Button) findViewById(R.id.id_Button_lef);
            iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
            button.setTypeface(iconfont);
            sureImg = (Button) findViewById(R.id.id_img_sure);
            sureImg.setTypeface(iconfont);
            editText = (EditText) findViewById(R.id.id_text_mycom);
            url = getResources().getString(R.string.ip)+"/comment.php?uid=";
            url += username + "&ctid=" + one;
        }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_img_sure:
                if(editText.getText().toString().length()!=0) {
                    url+="&con="+ URLEncoder.encode(editText.getText().toString());
                    new Thread(save_thread).start();
                }else{
                    new AlertDialog.Builder(this).setTitle("提示信息").setMessage("评论不可以为空").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
                break;
            case R.id.id_Button_lef:
                finish();
                break;
            default:
                break;
        }

    }
}
