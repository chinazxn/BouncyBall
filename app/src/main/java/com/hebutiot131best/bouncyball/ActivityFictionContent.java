package com.hebutiot131best.bouncyball;

/**小说内容界面
 * Created by ShyLock-kai on 2016/5/15.
 * Edited by zxn 132764 on 5/27
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ActivityFictionContent extends AppCompatActivity implements View.OnClickListener{
    private Button collectImg;
    private Button praiseImg;
    private Button commentImg;
    private LinearLayout collectTab;
    private LinearLayout praiseTab;
    private LinearLayout commentTab;
    private TextView textNick;
    private TextView textLike;
    private TextView textView;
    boolean isCollected;
    boolean isLicked;
    String one;
    Typeface iconfont;
    Button button;
    Button userButton;
    Button likeButton;
    HeballtConnect heballtFictionConnect;
    HeballtConnect heballtLikeConnect;
    HeballtConnect heballtCollectConnect;
    String url;
    String urlc;
    String urll;
    String surlc;
    String surll;
    List<String> datal;
    String username;
    int count;
    String uid;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            List<String> datas = heballtFictionConnect.getConnectData("Content");
                            List<String> datau = heballtFictionConnect.getConnectData("UID");
                            List<String> datan = heballtFictionConnect.getConnectData("Nickname");
                            datal = heballtFictionConnect.getConnectData("LikeCount");
                            textView.setText(datas.get(0));
                            textLike.setText(datal.get(0));
                            textNick.setText(datan.get(0));
                            count = Integer.parseInt(datal.get(0));
                            uid = datau.get(0);
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityFictionContent.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    Handler handleric = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            isCollected = true;
                            collectImg.setText(getResources().getString(R.string.icon_coll));
                            collectImg.setTextColor(getResources().getColor(R.color.buttonafter));
                            break;
                        default:
                            isCollected = false;
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityFictionContent.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    Handler handlerc = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            isCollected = true;
                            collectImg.setText(getResources().getString(R.string.icon_coll));
                            collectImg.setTextColor(getResources().getColor(R.color.buttonafter));
                            Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            isCollected = false;
                            collectImg.setText(getResources().getString(R.string.icon_uncoll));
                            collectImg.setTextColor(getResources().getColor(R.color.buttonbefore));
                            Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityFictionContent.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    Handler handleril = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            isLicked = true;
                            praiseImg.setText(getResources().getString(R.string.icon_like));
                            praiseImg.setTextColor(getResources().getColor(R.color.buttonafter));
                            break;
                        default:
                            isLicked = false;
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityFictionContent.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }

    };

    Handler handlerl = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            isLicked = true;
                            praiseImg.setText(getResources().getString(R.string.icon_like));
                            praiseImg.setTextColor(getResources().getColor(R.color.buttonafter));
                            Toast.makeText(getApplicationContext(), "+1", Toast.LENGTH_SHORT).show();
                            count++;
                            textLike.setText("" + count);
                            break;
                        default:
                            isLicked = false;
                            praiseImg.setText(getResources().getString(R.string.icon_unlike));
                            praiseImg.setTextColor(getResources().getColor(R.color.buttonbefore));
                            count--;
                            textLike.setText("" + count);
                            //Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityFictionContent.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
            String code = heballtFictionConnect.getConnectResult(url);
            Message msg = Message.obtain();
            msg.obj = code;
            handler.sendMessage(msg);
        }
    };
    Runnable iscollect_thread = new Runnable() {
        public void run() {
            String code = heballtCollectConnect.getConnectResult(urlc);
            Message msg = Message.obtain();
            msg.obj = code;
            handleric.sendMessage(msg);
        }
    };
    Runnable collect_thread = new Runnable() {
        public void run() {
            String code = heballtCollectConnect.getConnectResult(urlc);
            Message msg = Message.obtain();
            msg.obj = code;
            urlc = surlc;
            handlerc.sendMessage(msg);
        }
    };
    Runnable isliked_thread = new Runnable() {
        public void run() {
            String code = heballtLikeConnect.getConnectResult(urll);
            Message msg = Message.obtain();
            msg.obj = code;
            handleril.sendMessage(msg);
        }
    };
    Runnable like_thread = new Runnable() {
        public void run() {
            String code = heballtLikeConnect.getConnectResult(urll);
            Message msg = Message.obtain();
            msg.obj = code;
            urll = surll;
            handlerl.sendMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        heballtFictionConnect = new HeballtConnect();
        heballtLikeConnect = new HeballtConnect();
        heballtCollectConnect = new HeballtConnect();
        MyApp.getInstance().addActivity(this);
        Intent intent1 = getIntent();
        // 用intent1.getStringExtra()来得到上一个ACTIVITY发过来的字符串。
        one = intent1.getStringExtra("CtID");
        collectTab = (LinearLayout) findViewById(R.id.id_tab_collect1);
        collectImg= (Button) findViewById(R.id.id_img_collect1);
        praiseTab = (LinearLayout) findViewById(R.id.id_tab_praise1);
        praiseImg= (Button) findViewById(R.id.id_img_praise1);
        commentTab = (LinearLayout) findViewById(R.id.id_tab_comment1);
        commentImg= (Button) findViewById(R.id.id_img_comment1);
        userButton = (Button) findViewById(R.id.id_user_text);
        likeButton = (Button) findViewById(R.id.icon_like);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        collectImg.setTypeface(iconfont);
        praiseImg.setTypeface(iconfont);
        commentImg.setTypeface(iconfont);
        userButton.setTypeface(iconfont);
        likeButton.setTypeface(iconfont);
        textLike = (TextView)findViewById(R.id.id_praise_text);
        textNick = (TextView)findViewById(R.id.id_name_text);
        textView = (TextView) findViewById(R.id.id_text_text);
        textView.setMovementMethod(new ScrollingMovementMethod());
        collectTab.setOnClickListener(ActivityFictionContent.this);
        praiseTab.setOnClickListener(ActivityFictionContent.this);
        commentTab.setOnClickListener(ActivityFictionContent.this);
        button = (Button) findViewById(R.id.id_left_text);
        button.setTypeface(iconfont);
        MyApp myApp = (MyApp)getApplication();
        username = myApp.getUserName();
        url = getResources().getString(R.string.ip)+"/content.php?ctid=";
        urlc = getResources().getString(R.string.ip) + "/collect.php?uid=";
        urll = getResources().getString(R.string.ip) + "/like.php?uid=";
        if(username!=null) {
            url += one;
            urlc += username + "&ctid=" + one;
            surlc = urlc;
            urll += username + "&ctid=" + one;
            surll = urll;
            new Thread(isliked_thread).start();
            new Thread(iscollect_thread).start();
        }
        new Thread(update_thread).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_tab_collect1:
                if (username==null){
                    Intent intent= new Intent(this,ActivityLogin.class);
                    startActivityForResult(intent,0);
                }else {
                    if (!isCollected) {
                        urlc += "&operate=1";
                        new Thread(collect_thread).start();
                    } else//已经收藏取消收藏
                    {
                        urlc += "&operate=2";
                        new Thread(collect_thread).start();
                    }
                }
                break;
            case R.id.id_tab_praise1:
                if (username==null){
                    Intent intent= new Intent(this,ActivityLogin.class);
                    startActivityForResult(intent,0);
                }else {
                    if (!isLicked) {
                        urll += "&operate=1";
                        new Thread(like_thread).start();
                    } else//已经收藏取消收藏
                    {
                        urll += "&operate=2";
                        new Thread(like_thread).start();
                    }
                }
                break;
            case R.id.id_tab_comment1:
                    Intent intent2 = new Intent(this, ActivityFictionComment.class);
                    intent2.putExtra("CtID", one);
                    startActivity(intent2);
                break;
            case R.id.id_left_text:
                finish();
                break;
            case R.id.id_user_text:
                Intent intent;
                if(username==null){
                    intent= new Intent(this,ActivityLogin.class);
                    startActivityForResult(intent,0);
                }else if(!username.equals(uid)){
                    intent=new Intent(ActivityFictionContent.this,ActivityAccountOther.class);
                    intent.putExtra("UID",uid);
                }else {
                    intent= new Intent(ActivityFictionContent.this,ActivityAccount.class);
                }
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                MyApp myApp = (MyApp)getApplication();
                username = myApp.getUserName();
                urlc+=username+"&ctid="+one;
                surlc = urlc;
                urll+=username+"&ctid="+one;
                surll = urll;
                new Thread(isliked_thread).start();
                new Thread(iscollect_thread).start();
                break;
            default:
                break;
        }
    }
}
