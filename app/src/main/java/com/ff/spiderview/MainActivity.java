package com.ff.spiderview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ff.library.SpiderView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpiderView spiderView = (SpiderView) findViewById(R.id.spiderView);
        float[] values = new float[]{35, 90, 50, 70, 96, 88, 40};
        String[] titles = new String[]{"击杀", "助攻", "死亡", "金钱", "经验", "治疗", "推塔"};
        spiderView.setValues(values);
        spiderView.setTitles(titles);
    }
}
