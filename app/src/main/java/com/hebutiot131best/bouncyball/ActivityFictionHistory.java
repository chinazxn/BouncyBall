package com.hebutiot131best.bouncyball;

/**往期小说列表界面
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


public class ActivityFictionHistory extends AppCompatActivity implements AbsListView.OnScrollListener{
    private ListView listView;//声明listview
    private SimpleAdapter simple_adapter;
    private List<Map<String,Object>> dataList;
    Typeface iconfont;
    Button button;
    String url;
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
                            List<String> datas = heballtCollectConnect.getConnectData("Title");
                            for (int i = 0; i < datas.size(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("ActivityFictionContent", datas.get(i));
                                dataList.add(map);
                            }
                            simple_adapter = new SimpleAdapter(ActivityFictionHistory.this, dataList, R.layout.item, new String[]{"ActivityFictionContent"}, new int[]{R.id.id_txt_item});
                            final List<String> numb = heballtCollectConnect.getConnectData("FID");
                            listView.setAdapter(simple_adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    //获得选中项的对象
                                    Intent intent = new Intent(ActivityFictionHistory.this, ActivityFictionChapter.class);
                                    intent.putExtra("FID", numb.get(arg2));
                                    startActivity(intent);
                                }
                            });
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityFictionHistory.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        setContentView(R.layout.history);
        heballtCollectConnect = new HeballtConnect();
        MyApp.getInstance().addActivity(this);
        button = (Button) findViewById(R.id.id_left_history);
        listView = (ListView) findViewById(R.id.listView2);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        url = getResources().getString(R.string.ip)+"/history.php?page=1&pagesize=12";
        new Thread(update_thread).start();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_left_history:
                finish();
                break;
        }
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
