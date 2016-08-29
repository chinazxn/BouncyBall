package com.hebutiot131best.bouncyball;

/**私信界面
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
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityMessage extends AppCompatActivity implements View.OnClickListener,AbsListView.OnScrollListener {
    //ViewPager
    private android.support.v4.view.ViewPager ViewPager;//底部tab
    private PagerAdapter Adapter;//home message user
    private List<View> Views = new ArrayList<View>();
    //TAB
    private LinearLayout Tabreceive;
    private LinearLayout Tabsend;

    private Button receiveImg;
    private Button sendImg;
    Typeface iconfont;
    private ListView listView1;
    private ListView listView2;
    private SimpleAdapter simple_adapter1;
    private SimpleAdapter simple_adapter2;
    private List<Map<String,Object>> dataList1;
    private List<Map<String,Object>> dataList2;

    String url;
    String surl;
    HeballtConnect heballtCollectConnect;
    HeballtConnect heballtCollectConnect2;
    List<String> datai;
    List<String> suid;
    List<String> suid2;
    Button button;
    boolean first = true;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            dataList1 = new ArrayList<Map<String, Object>>();
                            datai = heballtCollectConnect.getConnectData("Nickname");
                            List<String> datac = heballtCollectConnect.getConnectData("Content");
                            List<String> datat = heballtCollectConnect.getConnectData("MTime");
                            for (int i = 0; i < datai.size(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("img", R.drawable.user);
                                map.put("ActivityFictionContent", datai.get(i));
                                map.put("text1", datat.get(i));
                                map.put("text2", datac.get(i));
                                dataList1.add(map);
                            }
                            simple_adapter1 = new SimpleAdapter(ActivityMessage.this, dataList1, R.layout.item2, new String[]{"img", "ActivityFictionContent", "text1", "text2"}, new int[]{R.id.id_img_item2, R.id.id_txt_item2, R.id.id_time_item2, R.id.id_txt1_item2});
                            listView1 = (ListView) findViewById(R.id.list_receive);
                            listView1.setAdapter(simple_adapter1);
                            suid = heballtCollectConnect.getConnectData("FUID");
                            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    //获得选中项的对象
                                    Intent intent = new Intent(ActivityMessage.this, ActivityAccountOther.class);
                                    intent.putExtra("UID", suid.get(arg2));
                                    startActivity(intent);
                                }
                            });
                            break;
                        default:
                            break;
                    }
            }else {
                new AlertDialog.Builder(ActivityMessage.this).setTitle("提示信息").setMessage("无法连接至服务器").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
            url+="&operate=1";
            String code = heballtCollectConnect.getConnectResult(url);
            url=surl;
            Message msg = Message.obtain();
            msg.obj = code;
            handler.sendMessage(msg);
        }
    };
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                    switch (code) {
                        case "200":
                            dataList2 = new ArrayList<Map<String, Object>>();
                            datai = heballtCollectConnect2.getConnectData("Nickname");
                        List<String> datac = heballtCollectConnect2.getConnectData("Content");
                        List<String> datat = heballtCollectConnect2.getConnectData("MTime");
                        for (int i = 0; i < datai.size(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("img", R.drawable.user);
                                map.put("ActivityFictionContent", "发送给" + datai.get(i));
                                map.put("text1", datat.get(i));
                                map.put("text2", datac.get(i));
                                dataList2.add(map);
                            }
                            simple_adapter2 = new SimpleAdapter(ActivityMessage.this, dataList2, R.layout.item2, new String[]{"img", "ActivityFictionContent", "text1", "text2"}, new int[]{R.id.id_img_item2, R.id.id_txt_item2, R.id.id_time_item2, R.id.id_txt1_item2});
                            listView2 = (ListView) findViewById(R.id.list_sendmessage);
                            listView2.setAdapter(simple_adapter2);
                            suid2 = heballtCollectConnect2.getConnectData("SUID");
                            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    //获得选中项的对象
                                    Intent intent = new Intent(ActivityMessage.this, ActivityAccountOther.class);
                                    intent.putExtra("UID", suid2.get(arg2));
                                    startActivity(intent);
                                }
                            });
                            break;
                        default:
                            break;
                    }
            }
        }
    };
    //要用handler来处理多线程可以使用runnable接口，这里先定义该接口
    //线程中运行该接口的run函数
    Runnable update_thread2 = new Runnable() {
        public void run() {
            url+="&operate=2";
            String code = heballtCollectConnect2.getConnectResult(url);
            Message msg = Message.obtain();
            msg.obj = code;
            url=surl;
            handler2.sendMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getInstance().addActivity(this);
        MyApp myApp = (MyApp) getApplication();
        String username = myApp.getUserName();
        heballtCollectConnect = new HeballtConnect();
        heballtCollectConnect2 = new HeballtConnect();
            url = getResources().getString(R.string.ip)+"/message.php?suid=";
            url += username;
            surl = url;
            setContentView(R.layout.pmessage);
            initView();
            initEvents();
    }

    private void initView()//初始化views
    {
        // tabs
        button = (Button) findViewById(R.id.id_Bu_lef1);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
        Tabreceive = (LinearLayout) findViewById(R.id.id_tab_receive);
        Tabsend = (LinearLayout) findViewById(R.id.id_tab_send);
        // ImageButton
        receiveImg = (Button) findViewById(R.id.id_img_recive);
        sendImg = (Button) findViewById(R.id.id_img_send2);
        receiveImg.setTypeface(iconfont);
        sendImg.setTypeface(iconfont);
        ViewPager = (android.support.v4.view.ViewPager) findViewById(R.id.id_Viewpager1);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View tab01 = mInflater.inflate(R.layout.receive, null);
        View tab02 = mInflater.inflate(R.layout.sendmessage, null);
        Views.add(tab01);
        Views.add(tab02);

        Adapter = new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(Views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = Views.get(position);
                container.addView(view);
                if(position==0)
                {
                    if(first) {
                        new Thread(update_thread).start();
                    }
                }
                if(position==1)
                {
                    if(first) {
                        new Thread(update_thread2).start();
                        first = false;
                    }
                }
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return Views.size();
            }
        };
        ViewPager.setAdapter(Adapter);
    }

    private void initEvents() {
        Tabreceive.setOnClickListener(this);
        Tabsend.setOnClickListener(this);
        ViewPager.setOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                int currentItem = ViewPager.getCurrentItem();
                resetImg();
                switch (currentItem) {
                    case 0:
                        receiveImg.setTextColor(getResources().getColor(R.color.buttonafter));
                        break;
                    case 1:
                        sendImg.setTextColor(getResources().getColor(R.color.buttonafter));
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void resetImg() {
        sendImg.setTextColor(getResources().getColor(R.color.buttonbefore));
        receiveImg.setTextColor(getResources().getColor(R.color.buttonbefore));
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_tab_receive:
                resetImg();
                ViewPager.setCurrentItem(0);
                receiveImg.setTextColor(getResources().getColor(R.color.buttonafter));
                break;
            case R.id.id_tab_send:
                resetImg();
                ViewPager.setCurrentItem(1);
                sendImg.setTextColor(getResources().getColor(R.color.buttonafter));
                break;
            case R.id.id_Bu_lef1:
                finish();
                break;
            case R.id.id_img_item2:
                Intent intent = new Intent(this, ActivityAccountOther.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
