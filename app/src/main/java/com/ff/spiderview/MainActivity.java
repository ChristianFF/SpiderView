package com.ff.spiderview;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.SeekBar;

import com.ff.library.SpiderView;

import static com.ff.spiderview.R.id.spiderView;

public class MainActivity extends AppCompatActivity {

    private SpiderView mSpiderView;
    private AppCompatSeekBar mSeek1;
    private AppCompatSeekBar mSeek2;
    private AppCompatSeekBar mSeek3;
    private AppCompatSeekBar mSeek4;
    private AppCompatSeekBar mSeek5;
    private AppCompatSeekBar mSeek6;
    private AppCompatSeekBar mSeek7;

    private int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpiderView = (SpiderView) findViewById(spiderView);
        mSeek1 = (AppCompatSeekBar) findViewById(R.id.seek1);
        mSeek2 = (AppCompatSeekBar) findViewById(R.id.seek2);
        mSeek3 = (AppCompatSeekBar) findViewById(R.id.seek3);
        mSeek4 = (AppCompatSeekBar) findViewById(R.id.seek4);
        mSeek5 = (AppCompatSeekBar) findViewById(R.id.seek5);
        mSeek6 = (AppCompatSeekBar) findViewById(R.id.seek6);
        mSeek7 = (AppCompatSeekBar) findViewById(R.id.seek7);

        mSeek1.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mSeek2.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mSeek3.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mSeek4.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mSeek5.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mSeek6.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mSeek7.setOnSeekBarChangeListener(mSeekBarChangeListener);

        mSeek1.setProgress(35);
        mSeek2.setProgress(90);
        mSeek3.setProgress(50);
        mSeek4.setProgress(70);
        mSeek5.setProgress(96);
        mSeek6.setProgress(88);
        mSeek7.setProgress(40);

        float[] values = new float[]{35, 90, 50, 70, 96, 88, 40};
        String[] titles = new String[]{"击杀", "助攻", "死亡", "金钱", "经验", "治疗", "推塔"};
        mSpiderView.setValues(values);
        mSpiderView.setTitles(titles);
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mSpiderView.setValueAtIndex(progress, mPosition);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mPosition = findPosition(seekBar.getId());
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mPosition = -1;
        }
    };

    private int findPosition(@IdRes int id) {
        switch (id) {
            case R.id.seek1:
                return 0;
            case R.id.seek2:
                return 1;
            case R.id.seek3:
                return 2;
            case R.id.seek4:
                return 3;
            case R.id.seek5:
                return 4;
            case R.id.seek6:
                return 5;
            case R.id.seek7:
                return 6;
        }
        return -1;
    }
}
