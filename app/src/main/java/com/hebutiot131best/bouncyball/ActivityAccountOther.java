package com.hebutiot131best.bouncyball;

/**其他用户详细信息页面
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ActivityAccountOther extends AppCompatActivity implements View.OnClickListener{
    private Button button1,button2,buttonw;
    private TextView textView0;
    private TextView textView1;
    private List<Map<String,Object>> dataList;
    ImageView imageView;
    boolean isFollowed;
    Typeface iconfont;
    Button button;
    String fuid;
    String suid;
    String url;
    String urls;
    String surl;
    HeballtConnect heballtCollectConnect;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dataList = new ArrayList<Map<String, Object>>();
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            List<String> nick = heballtCollectConnect.getConnectData("Nickname");
                            List<String> sign = heballtCollectConnect.getConnectData("Signature");
                            List<String> icon = heballtCollectConnect.getConnectData("HeadURL");
                            textView0.setText(nick.get(0));
                            textView1.setText(sign.get(0));
                            new ImageLoder().showImage(getApplicationContext(), imageView, getResources().getString(R.string.ip) + icon.get(0),fuid,suid);
                            new Thread(isFollowed_thread).start();
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityAccountOther.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    Handler handlerif = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                switch (code) {
                    case "200":
                        isFollowed = true;
                        button2.setText("已关注");
                        break;
                    default:
                        isFollowed = false;
                        break;
                }
            } else {
                new AlertDialog.Builder(ActivityAccountOther.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    Handler handlerf = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            isFollowed = true;
                            Toast.makeText(getApplicationContext(), "关注成功", Toast.LENGTH_SHORT).show();
                            button2.setText("已关注");
                            break;
                        default:
                            isFollowed = false;
                            Toast.makeText(getApplicationContext(), "取消关注", Toast.LENGTH_SHORT).show();
                            button2.setText("添加关注");
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityAccountOther.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    Runnable update_thread = new Runnable() {
        public void run() {
            String code = heballtCollectConnect.getConnectResult(url);
            Message msg = Message.obtain();
            msg.obj = code;
            handler.sendMessage(msg);
        }
    };
    Runnable isFollowed_thread = new Runnable() {
        public void run() {
            String code = heballtCollectConnect.getConnectResult(urls);
            urls=surl;
            Message msg = Message.obtain();
            msg.obj = code;
            handlerif.sendMessage(msg);
        }
    };
    Runnable follow_thread = new Runnable() {
        public void run() {
            String code = heballtCollectConnect.getConnectResult(urls);
            urls=surl;
            Message msg = Message.obtain();
            msg.obj = code;
            handlerf.sendMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account1);
        Intent intent1 = getIntent();
        // 用intent1.getStringExtra()来得到上一个ACTIVITY发过来的字符串。
        suid = intent1.getStringExtra("UID");
        MyApp myApp = (MyApp)getApplication();
        String username = myApp.getUserName();
        fuid = username;
        MyApp.getInstance().addActivity(this);
        heballtCollectConnect = new HeballtConnect();
        textView0=(TextView)findViewById(R.id.id_name_account1);
        textView1=(TextView)findViewById(R.id.id_sign_account1);
        button2=(Button)findViewById(R.id.id_action_account1);
        button1=(Button)findViewById(R.id.id_message_account1);
        imageView = (ImageView)findViewById(R.id.id_img_account2);
        buttonw=(Button)findViewById(R.id.user_work);
        button = (Button) findViewById(R.id.id_Bu_lef6);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        url = getResources().getString(R.string.ip)+"/anotheruser.php?suid=";
        urls = getResources().getString(R.string.ip)+"/subscribeadd.php?suid=";
        url+=suid;
        urls+=suid+"&fuid="+fuid;
        surl = urls;
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }
    protected void onResume(){
        super.onResume();
        new Thread(update_thread).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_message_account1:
                Intent intent= new Intent(this,ActivityMessageSend.class);
                intent.putExtra("SUID",suid);
                startActivity(intent);
                break;
            case R.id.id_Bu_lef6:
                finish();
                break;
            case R.id.id_action_account1:
                if(!isFollowed){
                    urls+="&operate=1";
                    new Thread(follow_thread).start();
                }else
                {
                    new AlertDialog.Builder(this).setTitle("提示信息").setMessage("不再关注？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            urls += "&operate=2";
                            new Thread(follow_thread).start();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
                    break;
            case R.id.user_work:
                Intent intent2= new Intent(this,ActivityUserWorksOther.class);
                intent2.putExtra("UID",suid);
                startActivity(intent2);
                break;
            default:
                break;
        }

    }
}

