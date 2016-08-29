package com.hebutiot131best.bouncyball;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.LinkedList;
import java.util.List;

/**自定义Application，用于存储当前用户标识于内存中
 * Created by 朱晓南 on 2016/5/15.
 */
public class MyApp extends Application{
    //当前登录用户标识
    private String userName;
    /**
     * 注销和重新登录后更改当前标识
     */
    protected void setUserName(String userName1){
        this.userName = userName1;
    }
    /**
     * 获取当前标识
     */
    protected String getUserName(){
        return this.userName;
    }
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        User user = new User(this);
        String usname = user.getAccount();
        if (usname!="")
            setUserName(usname); //初始化全局变量
    }
    private List<Activity> mList = new LinkedList<Activity>();
    private static MyApp instance;

    public synchronized static MyApp getInstance() {
        if (null == instance) {
            instance = new MyApp();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
    /**
     * 获取版本号
     * @return 当前应用的版本名
     */
    public String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return context.getString(R.string.version_name) + version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public Integer getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            Integer versionCode = info.versionCode;
            return  versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
