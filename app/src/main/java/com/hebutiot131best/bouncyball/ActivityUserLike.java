package com.hebutiot131best.bouncyball;

/**收到的赞
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


public class ActivityUserLike extends AppCompatActivity implements AbsListView.OnScrollListener {
    private ListView listView;//声明listview
    private SimpleAdapter simple_adapter;
    private List<Map<String,Object>> dataList;
    String url;
    Typeface iconfont;
    Button button;
    String nuid;
    HeballtConnect heballtCollectConnect;
    List<String> suid;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dataList = new ArrayList<Map<String, Object>>();
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            List<String> datas = heballtCollectConnect.getConnectData("Nickname");
                            List<String> datat = heballtCollectConnect.getConnectData("LTime");
                            List<String> dataf = heballtCollectConnect.getConnectData("Title");
                            List<String> datac = heballtCollectConnect.getConnectData("ChID");
                            for (int i = 0; i < datas.size(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("img", R.drawable.user);
                                map.put("ActivityFictionContent", datas.get(i));
                                map.put("text1", datat.get(i));
                                map.put("text2", "赞了我的《" + dataf.get(i) + "》第" + datac.get(i) + "章");
                                dataList.add(map);
                            }
                            simple_adapter = new SimpleAdapter(ActivityUserLike.this, dataList, R.layout.item2, new String[]{"img", "ActivityFictionContent", "text1", "text2"}, new int[]{R.id.id_img_item2, R.id.id_txt_item2, R.id.id_time_item2, R.id.id_txt1_item2});
                            listView.setAdapter(simple_adapter);
                            suid = heballtCollectConnect.getConnectData("UID");
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    //获得选中项的对象
                                    Intent intent;
                                    if (!nuid.equals(suid.get(arg2))) {
                                        intent = new Intent(ActivityUserLike.this, ActivityAccountOther.class);
                                        intent.putExtra("UID", suid.get(arg2));
                                    } else {
                                        intent = new Intent(ActivityUserLike.this, ActivityAccount.class);
                                    }
                                    startActivity(intent);
                                }
                            });
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityUserLike.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        MyApp.getInstance().addActivity(this);
        MyApp myApp = (MyApp)getApplication();
        String username = myApp.getUserName();
            url = getResources().getString(R.string.ip)+"/userlike.php?uid=";
            nuid = username;
            setContentView(R.layout.praise);
            heballtCollectConnect = new HeballtConnect();
            button = (Button) findViewById(R.id.id_Bu_lef4);
            listView = (ListView) findViewById(R.id.list_praise);
            iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
            button.setTypeface(iconfont);
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
            case R.id.id_Bu_lef4:
                finish();
                break;
        }
    }
}
