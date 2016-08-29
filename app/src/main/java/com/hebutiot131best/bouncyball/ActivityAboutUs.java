package com.hebutiot131best.bouncyball;

/**关于我们页面
 * Created by ShyLock-kai on 2016/5/15.
 * Edited by ZxNan on 2016/6/4
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ActivityAboutUs extends AppCompatActivity implements View.OnClickListener{
    Typeface iconfont;
    Button button;
    TextView textView;
    TextView textViewv;
    String url;
    List<String> urld;
    HeballtConnect heballtCollectConnect;
    Integer version;

    //检查更新
    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                switch (code) {
                    case "200":
                        List<String> Latest = heballtCollectConnect.getConnectData("LatestCode");
                        if(Latest.get(0).equals(version.toString()))
                        {
                            Toast.makeText(getApplicationContext(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
                        }else {
                            List<String> Intro = heballtCollectConnect.getConnectData("Instruction");
                            new AlertDialog.Builder(ActivityAboutUs.this).setTitle("发现新版本").setMessage(Intro.get(0)).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    urld = heballtCollectConnect.getConnectData("URL");
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(getResources().getString(R.string.ip)+urld.get(0)));
                                    startActivity(intent);
                                }
                            }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                                }).show();
                        }
                        break;
                    default:
                        break;
                }
            }else {
                new AlertDialog.Builder(ActivityAboutUs.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        }
    };
    Runnable check_thread = new Runnable() {
        public void run() {
            String code = heballtCollectConnect.getConnectResult(url);
            Message msg = Message.obtain();
            msg.obj = code;
            handler.sendMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.our);
        MyApp.getInstance().addActivity(this);
        button = (Button) findViewById(R.id.id_lef_our);
        textView = (TextView)findViewById(R.id.id_icon_our);
        textViewv = (TextView)findViewById(R.id.version);
        heballtCollectConnect = new HeballtConnect();
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        textView.setTypeface(iconfont);
        textViewv.setText(MyApp.getInstance().getVersionName(this));
        version = MyApp.getInstance().getVersionCode(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.check_update:
                url = getResources().getString(R.string.ip)+"/checkupdate.php";
                new Thread(check_thread).start();
                break;
            case R.id.id_lef_our:
                finish();
                break;
            case R.id.id_yan_our:
                Toast.makeText(getApplicationContext(), "这是我们的总顾问——闫老师", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_pu_our:
                Toast.makeText(getApplicationContext(), "这是我们的组长大人——蒲帮主", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_wang_our:
                Toast.makeText(getApplicationContext(), "这是我们的副组长——集姐", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_zhu_our:
                Toast.makeText(getApplicationContext(), "这是我们的技术主管——南哥", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_zhang_our:
                Toast.makeText(getApplicationContext(), "这是我们的艺术总监——琴兽", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_feng_our:
                Toast.makeText(getApplicationContext(), "这是我们的技术人员——凯哥", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
