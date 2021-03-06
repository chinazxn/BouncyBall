package com.hebutiot131best.bouncyball;

/**小说列表主界面，包括新开篇和往期表
 * Created by ShyLock-kai.
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


public class ActivityFictionList extends AppCompatActivity implements AdapterView.OnItemClickListener ,AbsListView.OnScrollListener {
    private ListView listView0,listView1;//声明listview
    String url;
    Typeface iconfont;
    Button button;
    HeballtConnect heballtCollectConnect;

    private SimpleAdapter simple_adapter0,simple_adapter1;//绑定复杂数据
    private List<Map<String,Object>>dataList0,dataList1;//simple_adapter的数据源
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            List<String> datas = heballtCollectConnect.getConnectData("Title");
                            for (int i = 0; i < datas.size(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("ActivityFictionContent", datas.get(i));
                                if (i == 8)
                                    break;
                                if (i < 4)
                                    dataList0.add(map);
                                else
                                    dataList1.add(map);
                            }
                            simple_adapter0 = new SimpleAdapter(ActivityFictionList.this, dataList0, R.layout.item, new String[]{"ActivityFictionContent"}, new int[]{R.id.id_txt_item});
                            simple_adapter1 = new SimpleAdapter(ActivityFictionList.this, dataList1, R.layout.item, new String[]{"ActivityFictionContent"}, new int[]{R.id.id_txt_item});
                            final List<String> numb = heballtCollectConnect.getConnectData("FID");
                            listView0.setAdapter(simple_adapter0);
                            listView1.setAdapter(simple_adapter1);
                            listView0.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    Intent intent = new Intent(ActivityFictionList.this, ActivityFictionChapter.class);
                                    intent.putExtra("FID", numb.get(arg2));
                                    startActivity(intent);
                                }
                            });
                            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    Intent intent = new Intent(ActivityFictionList.this, ActivityFictionChapter.class);
                                    intent.putExtra("FID", numb.get(arg2 + 4));
                                    startActivity(intent);
                                }
                            });
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityFictionList.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        setContentView(R.layout.novel);
        heballtCollectConnect = new HeballtConnect();
        dataList0=new ArrayList<Map<String, Object>>();
        dataList1=new ArrayList<Map<String, Object>>();//新建
        listView0 =(ListView)findViewById(R.id.listView0);
        listView1=(ListView)findViewById(R.id.listView1);
        button = (Button) findViewById(R.id.id_lef_topnovel);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        url = getResources().getString(R.string.ip)+"/fiction.php";
        new Thread(update_thread).start();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_Bu_history:
                Intent intent= new Intent(this,ActivityFictionHistory.class);
                startActivity(intent);
                break;
            case R.id.id_lef_topnovel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ActivityFictionChapter.class);

        startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
