package com.hebutiot131best.bouncyball;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**上传头像类
 * Created by 朱晓南 on 2016/6/5.
 */
public class UploadThread {
    String result;
    String urli;
    HeballtConnect heballtCollectConnect;
    private String username;
    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param uploadUrl Service net address
     * @param newName text toStore
     * @param uploadFile pictures
     * @return String result of Service response
     * @throws IOException
     */
    public String post(final String newName,final String uploadUrl,final String uploadFile) {
                try {
                    String end = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "******";
                    URL url = new URL(uploadUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url
                            .openConnection();
                    httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
                    // 允许输入输出流
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);
                    // 使用POST方法
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    httpURLConnection.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);

                    DataOutputStream dos = new DataOutputStream(
                            httpURLConnection.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + end);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
                            + newName + "\""+ end);
                    dos.writeBytes(end);

                    FileInputStream fis = new FileInputStream(uploadFile);
                    byte[] buffer = new byte[8192]; // 8k
                    int count = 0;
                    // 读取文件
                    while ((count = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, count);
                    }
                    fis.close();
                    dos.writeBytes(end);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    dos.flush();
                    InputStream is = httpURLConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    result = br.readLine();
                    dos.close();
                    is.close();
                }catch (Exception e) {
                }
        return result;
    }
}
