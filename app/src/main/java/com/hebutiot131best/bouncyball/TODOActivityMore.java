package com.hebutiot131best.bouncyball;

/**
 * Created by ShyLock-kai on 2016/5/15.
 * Created by 朱晓南 on 2016/6/3.
 */
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class TODOActivityMore extends AppCompatActivity {
    Typeface iconfont;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getInstance().addActivity(this);
        setContentView(R.layout.programming);
        button = (Button) findViewById(R.id.id_Bu_lef10);
        iconfont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
        button.setTypeface(iconfont);
    }

    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.id_Bu_lef10:
                finish();
                break;
        }
    }
}
