package com.hebutiot131best.bouncyball;

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

/**查看其他用户作品
 * Created by 朱晓南 on 2016/6/3.
 */
public class ActivityUserWorksOther extends AppCompatActivity implements AbsListView.OnScrollListener {
    private ListView listView;//声明listview
    private SimpleAdapter simple_adapter;
    private List<Map<String,Object>> dataList;
    Button button;
    Typeface iconfont;
    String url;
    String username;
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
                            List<String> datat = heballtCollectConnect.getConnectData("Title");
                            List<String> datas = heballtCollectConnect.getConnectData("ChID");
                            for (int i = 0; i < datas.size(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("ActivityFictionContent", "《" + datat.get(i) + "》第" + datas.get(i) + "章");
                                dataList.add(map);
                            }
                            simple_adapter = new SimpleAdapter(ActivityUserWorksOther.this, dataList, R.layout.item, new String[]{"ActivityFictionContent"}, new int[]{R.id.id_txt_item});
                            final List<String> numb = heballtCollectConnect.getConnectData("CtID");
                            listView.setAdapter(simple_adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    Intent intent = new Intent(ActivityUserWorksOther.this, ActivityFictionContent.class);
                                    intent.putExtra("CtID", numb.get(arg2));
                                    startActivity(intent);
                                }
                            });
                            break;
                        default:
                            break;
                    }
                } else {
                new AlertDialog.Builder(ActivityUserWorksOther.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        setContentView(R.layout.user_works);
        heballtCollectConnect = new HeballtConnect();
        MyApp.getInstance().addActivity(this);
        Intent intent1 = getIntent();
        // 用intent1.getStringExtra()来得到上一个ACTIVITY发过来的字符串。
        username = intent1.getStringExtra("UID");
        button = (Button) findViewById(R.id.id_Bu_lef8);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        listView = (ListView) findViewById(R.id.list_works);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/
        button.setTypeface(iconfont);
        url = getResources().getString(R.string.ip)+"/userwork.php?uid=";
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
            case R.id.id_Bu_lef8:
                finish();
                break;
        }
    }
}
