package com.hebutiot131best.bouncyball;

/**小说章节列表界面
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


public class ActivityFictionChapter extends AppCompatActivity implements AbsListView.OnScrollListener{
    private ListView listView;//声明listview
    private SimpleAdapter simple_adapter;
    private List<Map<String,Object>> dataList;
    boolean isNew;
    int newChapter = 999;
    Typeface iconfont;
    Button button;
    String one;
    String chap;
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
//                            List<String> datas = json.getData("ChID", person.getJSONArray("data"));
//                            List<String> status = json.getData("Status", person.getJSONArray("data"));
                            List<String> datas = heballtCollectConnect.getConnectData("ChID");
                            List<String> status = heballtCollectConnect.getConnectData("Status");
                            int i;
                            for (i = 0; i < datas.size() - 1; i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("ActivityFictionContent", "第" + datas.get(i) + "章");
                                dataList.add(map);
                            }
                            switch (status.get(i)) {
                                case "2":
                                    isNew = true;
                                    newChapter = datas.size() - 1;
                                    chap = datas.get(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("ActivityFictionContent", "第" + chap + "章（New!）");
                                    dataList.add(map);
                                    break;
                                default:
                                    Map<String, Object> map2 = new HashMap<String, Object>();
                                    map2.put("ActivityFictionContent", "第" + datas.get(i) + "章");
                                    dataList.add(map2);
                                    break;
                            }
                            simple_adapter = new SimpleAdapter(ActivityFictionChapter.this, dataList, R.layout.item, new String[]{"ActivityFictionContent"}, new int[]{R.id.id_txt_item});
                            final List<String> numb = heballtCollectConnect.getConnectData("CtID");
                            listView.setAdapter(simple_adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    Intent intent;
                                    if (arg2 != newChapter) {
                                        intent = new Intent(ActivityFictionChapter.this, ActivityFictionContent.class);
                                        intent.putExtra("CtID", numb.get(arg2));
                                    } else {
                                        intent = new Intent(ActivityFictionChapter.this, ActivityFictionNewChapter.class);
                                        intent.putExtra("FID", one);
                                        intent.putExtra("ChID", chap);
                                    }
                                    startActivity(intent);
                                }
                            });
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityFictionChapter.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        setContentView(R.layout.mulu);
        heballtCollectConnect = new HeballtConnect();
        Intent intent1 = getIntent();
        // 用intent1.getStringExtra()来得到上一个ACTIVITY发过来的字符串。
        one = intent1.getStringExtra("FID");
        listView = (ListView) findViewById(R.id.listView3);
        button = (Button) findViewById(R.id.id_left_mulu);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        url = getResources().getString(R.string.ip)+"/chapter.php?fid=";
        url += one;
        new Thread(update_thread).start();
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_left_mulu:
                finish();
                break;
        }
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
