package com.hebutiot131best.bouncyball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**账户界面加载头像
 * Created by 朱晓南 on 2016/6/5.
 */
public class ImageLoder {
    private ImageView imageView;
    private String url;
    Bitmap bitmap;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            imageView.setImageBitmap((Bitmap)msg.obj);
        }
    };

    public void showImage(final Context context,ImageView imageView1, String url1,final String uid1,final String uid2) {
        imageView = imageView1;
        url = url1;
        new Thread() {
            @Override
            public void run() {
                super.run();
                bitmap = getBitmap(url);
                bitmap = toRoundCorner(bitmap, 250);
                Message message = Message.obtain();
                message.obj = bitmap;
                if(uid1.equals(uid2)) {
                    User user = new User(context);
                    user.saveBitmapToSharedPreferences(bitmap);
                }
                handler.sendMessage(message);
            }
        }.start();
    }

    public Bitmap getBitmap(String bURL) {
        try {
            URL getURL = new URL(bURL);
            HttpURLConnection httpConn = (HttpURLConnection) getURL.openConnection();
            httpConn.setReadTimeout(5000);
            httpConn.setConnectTimeout(10000);
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedInputStream isr = new BufferedInputStream(httpConn.getInputStream());
                bitmap = BitmapFactory.decodeStream(isr);
                httpConn.disconnect();
                isr.close();
                return bitmap;
            }
        } catch (Exception e) {
            //
        }
        return null;
    }

    public Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        saveBitmap2file(output,"/myicon.jpg");
        return output;
    }
    static boolean saveBitmap2file(Bitmap bmp,String filename){
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(Environment.getExternalStorageDirectory() + filename);
        } catch (FileNotFoundException e) {
            //
        }
        return bmp.compress(format, quality, stream);
    }
}
