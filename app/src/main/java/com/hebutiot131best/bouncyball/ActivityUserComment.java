package com.hebutiot131best.bouncyball;

/**收到评论
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityUserComment extends AppCompatActivity implements AbsListView.OnScrollListener {
    private ListView listView;//声明listview
    private SimpleAdapter simple_adapter;
    private List<Map<String,Object>> dataList;
    String url;
    Typeface iconfont;
    Button button;
    HeballtConnect heballtCollectConnect;
    List<String> uid;
    String nuid;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dataList = new ArrayList<Map<String, Object>>();
            listView = (ListView) findViewById(R.id.list_comment);
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            List<String> datas = heballtCollectConnect.getConnectData("Nickname");
                            List<String> datac = heballtCollectConnect.getConnectData("Content");
                            List<String> datat = heballtCollectConnect.getConnectData("CTime");

                            for (int i = 0; i < datas.size(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("img", R.drawable.user);
                                map.put("ActivityFictionContent", datas.get(i));
                                map.put("text1", datat.get(i));
                                map.put("text2", datac.get(i));
                                dataList.add(map);
                            }
                            simple_adapter = new SimpleAdapter(ActivityUserComment.this, dataList, R.layout.item2, new String[]{"img", "ActivityFictionContent", "text1", "text2"}, new int[]{R.id.id_img_item2, R.id.id_txt_item2, R.id.id_time_item2, R.id.id_txt1_item2});
                            uid = heballtCollectConnect.getConnectData("UID");
                            listView.setAdapter(simple_adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    Intent intent;
                                    if (!nuid.equals(uid.get(arg2))) {
                                        intent = new Intent(ActivityUserComment.this, ActivityAccountOther.class);
                                        intent.putExtra("UID", uid.get(arg2));
                                    } else {
                                        intent = new Intent(ActivityUserComment.this, ActivityAccount.class);
                                    }
                                    startActivity(intent);
                                }
                            });
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityUserComment.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp myApp = (MyApp)getApplication();
        String username = myApp.getUserName();
        MyApp.getInstance().addActivity(this);
            nuid = username;
            setContentView(R.layout.comment);
            heballtCollectConnect = new HeballtConnect();
            button = (Button) findViewById(R.id.id_Bu_lef3);
            iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
            button.setTypeface(iconfont);
            url = getResources().getString(R.string.ip)+"/usercomment.php?uid=";
            url += username;
            new Thread(update_thread).start();
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_Bu_lef3:
                finish();
                break;
        }
    }
}
