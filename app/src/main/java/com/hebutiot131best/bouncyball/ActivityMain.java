package com.hebutiot131best.bouncyball;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**主界面
 * Created by ShyLock-kai on 2016/5/15.
 * Edited by ZxNan on 2016/6/4
 */
public class ActivityMain extends AppCompatActivity implements OnClickListener {
    private MyViewPager ViewPager;//底部tab
    private PagerAdapter Adapter;//home message user
    private List<View> Views = new ArrayList<View>();
    //TAB
    private LinearLayout Tabhome;
    private LinearLayout Tabmessage;
    private LinearLayout Tabuser;
    private LinearLayout Tabnovel;
    private LinearLayout Tabmusic;
    private LinearLayout Tabcartoon;
    private LinearLayout Tabmore;
    //Img
    private Button homeImg;
    private Button messageImg;
    private Button userImg;
    Typeface iconfont;
    private Button novelImg;
    private Button musicImg;
    private Button cartoonImg;
    private Button moreImg;
    private Button message;
    private Button comment;
    private Button like;
    private Button follow;
    private Button account;
    private Button works;
    private Button collect;
    private Button us;
    private Button user;
    private Button icon;
    private ImageView mainPush;
    private ImageView messagePush;
    private ImageView commentPush;
    private ImageView likePush;
    boolean running = true;
    boolean first0 = true;
    boolean first1 = true;
    boolean first2 = true;
    String username;
    int red = 0;

    //ZXN

    private String url1;
    private String url2;
    private String url3;
    private boolean redmess;
    private boolean redcomm;
    private boolean redlike;
    HeballtConnect heballtCollectConnect;
    HeballtConnect heballtCollectConnect2;
    HeballtConnect heballtCollectConnect3;
    public ImageView imageView;
    public ImageView imageView2;
    public TextView text;
    public boolean juage = true;

//    public int images[] = new int[] { R.drawable.show1old, R.drawable.show2old,
//            R.drawable.show3,R.drawable.show4, R.drawable.show5,
//            R.drawable.show6};
    public int images[] = new int[] { R.drawable.show1, R.drawable.show2};
    public int count = 0;
    public int maxcount = 2;


    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
// TODO Auto-generated method stub
            if(running&&juage) {
                AnimationSet animationSet1 = new AnimationSet(true);
                AnimationSet animationSet2 = new AnimationSet(true);
                imageView2.setVisibility(View.VISIBLE);
                TranslateAnimation ta = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                        -1f, Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f);
                ta.setDuration(2000);
                animationSet1.addAnimation(ta);
                animationSet1.setFillAfter(true);
                ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                        0f, Animation.RELATIVE_TO_SELF, 0f);
                ta.setDuration(2000);
                animationSet2.addAnimation(ta);
                animationSet2.setFillAfter(true);
//iamgeView 出去 imageView2 进来
                imageView.startAnimation(animationSet1);
                imageView2.startAnimation(animationSet2);
                imageView.setBackgroundResource(images[count % maxcount]);
                count++;
                imageView2.setBackgroundResource(images[count % maxcount]);
            }
            if(juage) {
                handler.postDelayed(runnable, 4000);
                running = true;
            }
            else
                running = false;
        }
    };

    Handler handlerPushm = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                switch (code) {
                    case "200":
                        List<String> mess = heballtCollectConnect.getConnectData("MID");
                        if(mess.size()!=0) {
                            messagePush.setVisibility(View.VISIBLE);
                            mainPush.setVisibility(View.VISIBLE);
                            redmess = true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };
    Runnable push_Thread_m = new Runnable() {
        public void run() {
            url1=getResources().getString(R.string.ip)+"/checkpushmess.php?uid="+username;
            String code = heballtCollectConnect.getConnectResult(url1);
            Message msg = Message.obtain();
            msg.obj = code;
            handlerPushm.sendMessage(msg);
        }
    };
    Handler handlerPushc = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                switch (code) {
                    case "200":
                        List<String> comm = heballtCollectConnect2.getConnectData("CoID");
                        if(comm.size()!=0) {
                            commentPush.setVisibility(View.VISIBLE);
                            mainPush.setVisibility(View.VISIBLE);
                            redcomm = true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };
    Runnable push_thread_c = new Runnable() {
        public void run() {
            url2=getResources().getString(R.string.ip)+"/checkpushcomm.php?uid="+username;
            String code = heballtCollectConnect2.getConnectResult(url2);
            Message msg = Message.obtain();
            msg.obj = code;
            handlerPushc.sendMessage(msg);
        }
    };
    Handler handlerPushl = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            if (code != null) {
                switch (code) {
                    case "200":
                        List<String> like = heballtCollectConnect3.getConnectData("LID");
                        if(like.size()!=0) {
                            likePush.setVisibility(View.VISIBLE);
                            mainPush.setVisibility(View.VISIBLE);
                            redlike = true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };
    Runnable Push_Thread_l = new Runnable() {
        public void run() {
            url3=getResources().getString(R.string.ip)+"/checkpushlike.php?uid="+username;
            String code = heballtCollectConnect3.getConnectResult(url3);
            Message msg = Message.obtain();
            msg.obj = code;
            handlerPushl.sendMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉默认的title
        //将布局xml文件引入到activity当中
        setContentView(R.layout.activity_main);
        MyApp myApp = (MyApp)getApplication();
        username = myApp.getUserName();
        MyApp.getInstance().addActivity(this);
        heballtCollectConnect = new HeballtConnect();
        heballtCollectConnect2 = new HeballtConnect();
        heballtCollectConnect3 = new HeballtConnect();
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        initView();
        homeImg.setTypeface(iconfont);
        messageImg.setTypeface(iconfont);
        userImg.setTypeface(iconfont);
        user.setTypeface(iconfont);
        icon.setTypeface(iconfont);
        initEvents();
    }
    protected void onResume(){
        super.onResume();
        if(username!=null) {
            new Thread(push_Thread_m).start();
            new Thread(push_thread_c).start();
            new Thread(Push_Thread_l).start();
            MyApp myApp = (MyApp)getApplication();
            username = myApp.getUserName();
        }
    }

    public void onBackPressed(){
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void initEvents() {
        Tabhome.setOnClickListener(this);
        Tabmessage.setOnClickListener(this);
        Tabuser.setOnClickListener(this);
        ViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                int currentItem = ViewPager.getCurrentItem();
                resetImg();
                switch (currentItem) {
                    case 0:
                        juage = true;
                        if (!running) {
                            handler.postDelayed(runnable, 2000);
                            running = true;
                        }
                        homeImg.setTextColor(getResources().getColor(R.color.buttonafter));
                        break;
                    case 1:
                        messageImg.setTextColor(getResources().getColor(R.color.buttonafter));
                        break;
                    case 2:
                        userImg.setTextColor(getResources().getColor(R.color.buttonafter));
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


    private void initView()//初始化views
    {
        ViewPager = (MyViewPager) findViewById(R.id.id_Viewpager);
        // tabs
        Tabhome = (LinearLayout) findViewById(R.id.id_tab_home);
        Tabmessage = (LinearLayout) findViewById(R.id.id_tab_message);
        Tabuser = (LinearLayout) findViewById(R.id.id_tab_user);
        Tabnovel = (LinearLayout) findViewById(R.id.id_tab_novel);
        Tabmusic = (LinearLayout) findViewById(R.id.id_tab_music);
        Tabcartoon = (LinearLayout) findViewById(R.id.id_tab_cartoon);
        Tabmore = (LinearLayout) findViewById(R.id.id_tab_more);
        // ImageButton
        homeImg = (Button) findViewById(R.id.id_img_home);
        messageImg = (Button) findViewById(R.id.id_img_message);
        userImg = (Button) findViewById(R.id.id_img_user);
        user = (Button)findViewById(R.id.id_Button_user);
        icon = (Button)findViewById(R.id.id_Button_introduce);
        mainPush = (ImageView)findViewById(R.id.push);
        mainPush.setVisibility(View.INVISIBLE);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View tab01 = mInflater.inflate(R.layout.home, null);
        View tab02 = mInflater.inflate(R.layout.message, null);
        View tab03 = mInflater.inflate(R.layout.user, null);
        Views.add(tab01);
        Views.add(tab02);
        Views.add(tab03);


        Adapter = new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(Views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = Views.get(position);
                container.addView(view);
                if(position==0) {
                    if(first0) {
                        juage = true;
                        imageView = (ImageView) findViewById(R.id.imageView);
                        imageView2 = (ImageView) findViewById(R.id.imageView2);
                        imageView2.setVisibility(View.INVISIBLE);
                        handler.postDelayed(runnable, 2000);
                        novelImg = (Button) findViewById(R.id.id_img_novel);
                        musicImg = (Button) findViewById(R.id.id_img_music);
                        cartoonImg = (Button) findViewById(R.id.id_img_cartoon);
                        moreImg = (Button) findViewById(R.id.id_img_more);
                        novelImg.setTypeface(iconfont);
                        musicImg.setTypeface(iconfont);
                        cartoonImg.setTypeface(iconfont);
                        moreImg.setTypeface(iconfont);
                        first0 = false;
                    }
                }
                if(position==1){
                    if(first1) {
                        message = (Button) findViewById(R.id.id_img_pmessage);
                        comment = (Button) findViewById(R.id.id_img_comment);
                        like = (Button) findViewById(R.id.id_img_praise);
                        follow = (Button) findViewById(R.id.id_img_subscribe);
                        message.setTypeface(iconfont);
                        like.setTypeface(iconfont);
                        comment.setTypeface(iconfont);
                        follow.setTypeface(iconfont);
                        messagePush = (ImageView)findViewById(R.id.message_push);
                        messagePush.setVisibility(View.INVISIBLE);
                        commentPush = (ImageView)findViewById(R.id.comment_push);
                        commentPush.setVisibility(View.INVISIBLE);
                        likePush = (ImageView)findViewById(R.id.like_push);
                        likePush.setVisibility(View.INVISIBLE);
                    }
                }
                if(position==2){
                    if(first2) {
                        account = (Button) findViewById(R.id.id_img_account);
                        works = (Button) findViewById(R.id.id_img_works);
                        collect = (Button) findViewById(R.id.id_img_collect);
                        us = (Button) findViewById(R.id.id_img_us);
                        account.setTypeface(iconfont);
                        works.setTypeface(iconfont);
                        collect.setTypeface(iconfont);
                        us.setTypeface(iconfont);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_tab_home:
                resetImg();
                ViewPager.setCurrentItem(0);
                homeImg.setTextColor(getResources().getColor(R.color.buttonafter));
                break;
            case R.id.id_tab_message:
                resetImg();
                ViewPager.setCurrentItem(1);
                juage = false;
                messageImg.setTextColor(getResources().getColor(R.color.buttonafter));
                break;
            case R.id.id_tab_user:
                resetImg();
                ViewPager.setCurrentItem(2);
                juage = false;
                userImg.setTextColor(getResources().getColor(R.color.buttonafter));
                break;
            case R.id.id_tab_account:
                if (username==null) {
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivityForResult(intent, 0);
                }else {
                    Intent intent = new Intent(ActivityMain.this, ActivityAccount.class);
                    startActivity(intent);
                }
                break;
            case R.id.id_tab_works:
                if (username==null) {
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivityForResult(intent, 0);
                }else {
                Intent intent2 = new Intent(ActivityMain.this, ActivityUserWorks.class);
                startActivity(intent2);
                }
                break;
            case R.id.id_tab_collect:
                if (username==null) {
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivityForResult(intent, 0);
                }else {
                Intent intent3 = new Intent(ActivityMain.this, ActivityUserCollect.class);
                startActivity(intent3);
                }
                break;
            case R.id.id_tab_pmessage:
                if (username==null) {
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivityForResult(intent, 0);
                }else {
                Intent intent5 = new Intent(ActivityMain.this, ActivityMessage.class);
                startActivity(intent5);
                    redmess=false;
                    messagePush.setVisibility(View.INVISIBLE);
                    if(redmess||redcomm||redlike){
                        mainPush.setVisibility(View.VISIBLE);
                    }else
                        mainPush.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.id_tab_comment:
                if (username==null) {
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivityForResult(intent, 0);
                }else {
                Intent intent7 = new Intent(ActivityMain.this, ActivityUserComment.class);
                startActivity(intent7);
                    redcomm = false;
                    commentPush.setVisibility(View.INVISIBLE);
                    if(redmess||redcomm||redlike){
                        mainPush.setVisibility(View.VISIBLE);
                    }else
                        mainPush.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.id_tab_priase:
                if (username==null) {
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivityForResult(intent, 0);
                }else {
                Intent intent8 = new Intent(ActivityMain.this, ActivityUserLike.class);
                startActivity(intent8);
                    redlike = false;
                    likePush.setVisibility(View.INVISIBLE);
                    if(redmess||redcomm||redlike){
                        mainPush.setVisibility(View.VISIBLE);
                    }else
                        mainPush.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.id_tab_subscribe:
                if (username==null) {
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivityForResult(intent, 0);
                }else {
                Intent intent9 = new Intent(ActivityMain.this, ActivityUserSubscribe.class);
                startActivity(intent9);
                }
                break;
            case R.id.id_tab_novel:
                Intent intent10 = new Intent(ActivityMain.this, ActivityFictionList.class);
                startActivity(intent10);
                break;
            case R.id.id_tab_music:
                Intent intent11 = new Intent(ActivityMain.this, TODOActivityMusic.class);
                startActivity(intent11);
                break;
            case R.id.id_tab_cartoon:
                Intent intent12 = new Intent(ActivityMain.this, TODOActivityCartoon.class);
                startActivity(intent12);
                break;
            case R.id.id_tab_more:
                Intent intent13 = new Intent(ActivityMain.this, TODOActivityMore.class);
                startActivity(intent13);
                break;
            case R.id.id_tab_us:
                Intent intent16 = new Intent(ActivityMain.this, ActivityAboutUs.class);
                startActivity(intent16);
                break;
            case R.id.id_Button_user:
                if (username==null) {
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivityForResult(intent, 0);
                }else {
                    Intent intent14 = new Intent(this, ActivityAccount.class);
                    startActivity(intent14);
                }
                break;
            case R.id.id_Button_introduce:
                if(username!=null) {
                    if (username.equals("3")){
                        Intent intent15 = new Intent(this, ActivityAdminMain.class);
                        startActivity(intent15);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void resetImg() {
        homeImg.setTextColor(getResources().getColor(R.color.buttonbefore));
        messageImg.setTextColor(getResources().getColor(R.color.buttonbefore));
        userImg.setTextColor(getResources().getColor(R.color.buttonbefore));
    }

    @Override
    protected void onPause() {
        juage = false;
        super.onPause();
    }
    private long exitTime = 0;

//再按一次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else {
                MyApp.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                MyApp myApp = (MyApp)getApplication();
                username = myApp.getUserName();
                //检查消息提醒
                new Thread(push_Thread_m).start();
                new Thread(push_thread_c).start();
                new Thread(Push_Thread_l).start();
                break;
            default:
                break;
        }
    }
}
