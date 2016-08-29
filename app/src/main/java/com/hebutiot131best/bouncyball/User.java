package com.hebutiot131best.bouncyball;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**持久性保存的用户标识
 * Created by 朱晓南 on 2016/5/15.
 */
public class User{
    private String phoneNum;
    private String PREFERENCES = "login_info";
    SharedPreferences preference;
    SharedPreferences.Editor editor;
    /**
     * 将此次登陆的账户信息存储下来
     */
    public User(Context context){
        preference = context.getSharedPreferences(PREFERENCES,
                0);
        editor = preference.edit();
    }
    public void saveAccount(String phoneNum) {
        // 存入数据
        editor.putString("phoneNum", phoneNum);
        // 提交存入文件中
        editor.commit();
    }
    /**
     * 获取存入SharedPreference中的账户
     *
     */
    public String getAccount() {
        phoneNum = preference.getString("phoneNum", "");
        return phoneNum;
    }
    protected void destory(){
        editor.clear();
        editor.commit();
    }
    public void saveBitmapToSharedPreferences(Bitmap bitmap){
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray=byteArrayOutputStream.toByteArray();
        String imageString=new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        editor.putString("image", imageString);
        editor.commit();
    }
    public Bitmap getBitmapFromSharedPreferences(){
        //第一步:取出字符串形式的Bitmap
        String imageString=preference.getString("image", "");
        //第二步:利用Base64将字符串转换为ByteArrayInputStream
        byte[] byteArray=Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
        //第三步:利用ByteArrayInputStream生成Bitmap
        Bitmap bitmap=BitmapFactory.decodeStream(byteArrayInputStream);
        return bitmap;
    }
}
