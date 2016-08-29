package com.hebutiot131best.bouncyball;

/**关注列表界面
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


public class ActivityUserSubscribe extends AppCompatActivity implements AbsListView.OnScrollListener {
    private ListView listView;//声明listview
    private SimpleAdapter simple_adapter;
    private List<Map<String, Object>> dataList;
    String url;
    Typeface iconfont;
    Button button;
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
                            List<String> datau = heballtCollectConnect.getConnectData("Nickname");
                            List<String> datas = heballtCollectConnect.getConnectData("Signature");
                            for (int i = 0; i < datau.size(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("img", R.drawable.user);
                                map.put("ActivityFictionContent", datau.get(i));
                                map.put("text2", "——" + datas.get(i));
                                dataList.add(map);
                            }
                            simple_adapter = new SimpleAdapter(ActivityUserSubscribe.this, dataList, R.layout.item2, new String[]{"img", "ActivityFictionContent", "text1", "text2"}, new int[]{R.id.id_img_item2, R.id.id_txt_item2, R.id.id_time_item2, R.id.id_txt1_item2});
                            listView.setAdapter(simple_adapter);
                            final List<String> suid =  heballtCollectConnect.getConnectData("UID");
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    //获得选中项的对象
                                    Intent intent;
                                    intent = new Intent(ActivityUserSubscribe.this, ActivityAccountOther.class);
                                    intent.putExtra("UID", suid.get(arg2));
                                    startActivity(intent);
                                }
                            });
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityUserSubscribe.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
            setContentView(R.layout.subscribe);
            heballtCollectConnect = new HeballtConnect();
            button = (Button) findViewById(R.id.id_Bu_lef5);
            listView = (ListView) findViewById(R.id.list_subscribe);
            iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
            button.setTypeface(iconfont);
            url = getResources().getString(R.string.ip)+"/subscribe.php?fuid=";
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
            case R.id.id_Bu_lef5:
                finish();
                break;
        }
    }
}
