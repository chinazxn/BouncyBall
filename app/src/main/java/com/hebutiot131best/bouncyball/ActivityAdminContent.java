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

import java.net.URLEncoder;
import java.util.List;

/**管理员管理下一篇小说内容界面
 * Created by 朱晓南 on 2016/5/29.
 */
public class ActivityAdminContent extends Activity {
    EditText editText;
    Button buttonet;
    String url;
    Typeface iconfont;
    Button button;
    String surl;
    String one;
    HeballtConnect heballtCollectConnect;
    String con;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            List<String> datat = heballtCollectConnect.getConnectData("Content");
                            editText.setText(datat.get(0));
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityAdminContent.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        Handler handlere = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String code = (String) msg.obj;
                if (code != null) {
                    switch (code) {
                        case "200":
                            new AlertDialog.Builder(ActivityAdminContent.this).setTitle("提示信息").setMessage("修改完成").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                            break;
                        default:
                            new AlertDialog.Builder(ActivityAdminContent.this).setTitle("提示信息").setMessage("修改失败").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                            break;
                    }
            }else {
                    new AlertDialog.Builder(ActivityAdminContent.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
        }
    };
    //要用handler来处理多线程可以使用runnable接口，这里先定义该接口
    //线程中运行该接口的run函数
    Runnable save_thread = new Runnable() {
        public void run() {
            String code = heballtCollectConnect.getConnectResult(url);
            url = surl;
            Message msg = Message.obtain();
            msg.obj = code;
            handlere.sendMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_nomain);
        MyApp.getInstance().addActivity(this);
        heballtCollectConnect = new HeballtConnect();
        editText = (EditText)findViewById(R.id.edit_admin_con);
        buttonet = (Button)findViewById(R.id.save_admin_con);
        button = (Button) findViewById(R.id.id_Bu_lef10);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        Intent intent1 = getIntent();
        // 用intent1.getStringExtra()来得到上一个ACTIVITY发过来的字符串。
        one = intent1.getStringExtra("FID");
        url = getResources().getString(R.string.ip)+"/changeFictionCon.php?fid="+one;
        surl = url;
    }
    protected void onResume(){
        super.onResume();
        new Thread(update_thread).start();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_Bu_lef10:
                finish();
                break;
            case R.id.save_admin_con:
                con=editText.getText().toString();
                if(con.length()!=0) {
                    new AlertDialog.Builder(this).setTitle("提示信息").setMessage("确定进行修改吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            url += "&operate=1&con="+ URLEncoder.encode(con);
                            new Thread(save_thread).start();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }else{
                    new AlertDialog.Builder(this).setTitle("提示信息").setMessage("内容不可以为空").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
                break;
        }
    }
}
