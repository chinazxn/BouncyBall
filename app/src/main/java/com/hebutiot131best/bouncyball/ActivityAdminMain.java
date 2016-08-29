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
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.List;

/**管理员管理下篇小说界面
 * Created by 朱晓南 on 2016/6/2.
 */
public class ActivityAdminMain extends Activity {
    EditText editText;
    Button buttontit;
    Button buttoncon;
    Button buttonf;
    Typeface iconfont;
    Button button;
    TextView textView;
    String url;
    String urls;
    String urlf;
    String one;
    HeballtConnect heballtCollectConnect;
    String con;
    String fid;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            List<String> datat = heballtCollectConnect.getConnectData("Title");
                            textView.setText(datat.get(0));
                            List<String> fids = heballtCollectConnect.getConnectData("FID");
                            fid = fids.get(0);
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityAdminMain.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
                            new AlertDialog.Builder(ActivityAdminMain.this).setTitle("提示信息").setMessage("修改完成").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                            new Thread(update_thread).start();
                            break;
                        default:
                            new AlertDialog.Builder(ActivityAdminMain.this).setTitle("提示信息").setMessage("修改失败").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityAdminMain.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
            Message msg = Message.obtain();
            msg.obj = code;
            url = urls;
            handlere.sendMessage(msg);
        }
    };
    Handler handlef = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            new AlertDialog.Builder(ActivityAdminMain.this).setTitle("提示信息").setMessage("更新完成").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                            break;
                        default:
                            new AlertDialog.Builder(ActivityAdminMain.this).setTitle("提示信息").setMessage("未知错误").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityAdminMain.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    //要用handler来处理多线程可以使用runnable接口，这里先定义该接口
    //线程中运行该接口的run函数
    Runnable force_thread = new Runnable() {
        public void run() {
            String code = heballtCollectConnect.getConnectResult(urlf);
            Message msg = Message.obtain();
            msg.obj = code;
            handlef.sendMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);
        MyApp.getInstance().addActivity(this);
        heballtCollectConnect = new HeballtConnect();
        editText = (EditText)findViewById(R.id.edit_admin_tit);
        buttontit = (Button)findViewById(R.id.admin_tit);
        buttoncon = (Button)findViewById(R.id.admin_con);
        buttonf = (Button)findViewById(R.id.force);
        button = (Button) findViewById(R.id.id_Bu_lef10);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        textView = (TextView)findViewById(R.id.text_admin_tit);
        url = getResources().getString(R.string.ip)+"/changeFiction.php";
        urlf = getResources().getString(R.string.ip)+"/forceTimeTask.php";
        urls = url;
        new Thread(update_thread).start();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_Bu_lef10:
                finish();
                break;
            case R.id.admin_con:
                if(fid!=null) {
                    Intent intent = new Intent(this, ActivityAdminContent.class);
                    intent.putExtra("FID", fid);
                    startActivity(intent);
                }else {
                    new AlertDialog.Builder(this).setTitle("提示信息").setMessage("请先设置标题").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
                break;
            case R.id.admin_tit:
                con=editText.getText().toString();
                if(con.length()!=0) {
                    new AlertDialog.Builder(this).setTitle("提示信息").setMessage("确定进行修改吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            url += "?operate=1&title="+ URLEncoder.encode(con);
                            new Thread(save_thread).start();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }else{
                    new AlertDialog.Builder(this).setTitle("提示信息").setMessage("标题不可以为空").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
                break;
            case R.id.force:
                    new AlertDialog.Builder(this).setTitle("提示信息").setMessage("确定进行手动章节更新吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(force_thread).start();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                break;
        }
    }
}