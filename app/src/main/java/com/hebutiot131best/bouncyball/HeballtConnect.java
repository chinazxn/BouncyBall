package com.hebutiot131best.bouncyball;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**HTTP请求与JSON解析
 * Created by 朱晓南 on 2016/6/4.
 */
public class HeballtConnect {
    private String url; //JSONObject对象
    private String content;//获取服务器返回数据
    private String status;//返回状态码
    private List<String> list;
    private JSONArray data;
    private JSONObject person;//JSON对象

    /**
     *
     * @param mURL 输入URL地址
     * @return 服务器返回的状态码
     */
    public String getConnectResult(String mURL){
        url = mURL;
         //GET方法与服务器通信，获取返回数据content
        try {
            URL getURL = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) getURL.openConnection();
            httpConn.setReadTimeout(5000);
            httpConn.setConnectTimeout(10000);
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(),"utf-8");
                int i;
                content = "";
                while((i = isr.read()) != -1){
                    content = content + (char)i;
                }
                isr.close();
            }
            httpConn.disconnect();
            //分析数据，为空则说明服务器未返回数据
            if (content != null) {
                JSONTokener jsonParser = new JSONTokener(content);
                // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
                person = (JSONObject) jsonParser.nextValue();
                status = person.getString("code");
                data = person.getJSONArray("data");
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return status;
    }

    /**
     * 解析指定字段数据，返回List集合
     * @param key 字段名
     * @return list 为json数据中key为data的json数组
     */
    public List<String> getConnectData(String key){
        list = new ArrayList<>();
        try {
            //通过遍历，解析jsonArray格式的数据，data的数据以列为单位添加到当前的List集合里面方便向ListView填充。
            for(int i = 0;i<data.length();i++){
                JSONObject jsonObject = data.getJSONObject(i);
                String name=jsonObject.getString(key);
                list.add(name);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

}
