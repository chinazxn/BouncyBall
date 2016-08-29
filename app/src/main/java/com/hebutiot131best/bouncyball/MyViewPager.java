package com.hebutiot131best.bouncyball;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**自定义ViewPager可手动设置滑动等参数
 * Created by 朱晓南 on 2016/5/31.
 */
public class MyViewPager extends ViewPager {
    private boolean noScroll = false;

    public MyViewPager(Context context,AttributeSet attrs){
        super(context,attrs);
    }
    public MyViewPager(Context context){
        super(context);
    }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }
}
