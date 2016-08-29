package com.hebutiot131best.bouncyball;

/**当前投票中章节列表界面
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityFictionNewChapter extends AppCompatActivity implements AbsListView.OnScrollListener {
    private ListView listView;//声明listview
    private SimpleAdapter simple_adapter;
    private List<Map<String,Object>> dataList;
    String one;
    Typeface iconfont;
    Button button;
    Button buttone;
    String chap;
    String url;
    TextView textView;
    HeballtConnect heballtCollectConnect;
    String username;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dataList = new ArrayList<Map<String, Object>>();
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
//                            List<String> datas = json.getData("Nickname", person.getJSONArray("data"));
//                            List<String> like = json.getData("LikeCount", person.getJSONArray("data"));
                            List<String> datas = heballtCollectConnect.getConnectData("Nickname");
                            List<String> like = heballtCollectConnect.getConnectData("LikeCount");
                            int i;
                            for (i = 0; i < datas.size(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("text", datas.get(i));
                                map.put("text1", like.get(i));
                                dataList.add(map);
                            }
                            simple_adapter = new SimpleAdapter(ActivityFictionNewChapter.this, dataList, R.layout.item3, new String[]{"text", "text1"}, new int[]{R.id.id_txt_item3, R.id.id_txt1_item3});
                            final List<String> numb = heballtCollectConnect.getConnectData("CtID");
                            listView.setAdapter(simple_adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    Intent intent;
                                    intent = new Intent(ActivityFictionNewChapter.this, ActivityFictionContent.class);
                                    intent.putExtra("CtID", numb.get(arg2));
                                    startActivity(intent);
                                }
                            });
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityFictionNewChapter.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        setContentView(R.layout.newchapter);
        heballtCollectConnect = new HeballtConnect();
        MyApp.getInstance().addActivity(this);
        Intent intent1 = getIntent();
        // 用intent1.getStringExtra()来得到上一个ACTIVITY发过来的字符串。
        one = intent1.getStringExtra("FID");
        chap = intent1.getStringExtra("ChID");
        button = (Button) findViewById(R.id.id_Bu_lef10);
        buttone = (Button) findViewById(R.id.id_Button_add);
        listView = (ListView) findViewById(R.id.list_newchapter);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        buttone.setTypeface(iconfont);
        url = getResources().getString(R.string.ip)+"/continue.php?fid=";
        url += one+"&chid="+chap;
    }
    protected void onResume(){
        super.onResume();
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
            case R.id.id_Button_add:
                MyApp myApp = (MyApp)getApplication();
                username = myApp.getUserName();
                if (username==null){
                    Intent intent= new Intent(this,ActivityLogin.class);
                    startActivityForResult(intent,0);
                }else {
                    Intent intent = new Intent(this, ActivityCreate.class);
                    intent.putExtra("ChID", chap);
                    intent.putExtra("FID", one);
                    startActivity(intent);
                }
                break;
            case R.id.id_Bu_lef10:
                finish();
                break;
            default:
                break;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                MyApp myApp = (MyApp)getApplication();
                username = myApp.getUserName();
                break;
            default:
                break;
        }
    }
}
