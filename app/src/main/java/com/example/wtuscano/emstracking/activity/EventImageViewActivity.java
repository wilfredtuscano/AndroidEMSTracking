package com.example.wtuscano.emstracking.activity;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.adapter.ImageSlideAdapter;
import com.example.wtuscano.emstracking.jpa.Event;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class EventImageViewActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private CircleIndicator indicator;

    ArrayList<String> imageURLs = new ArrayList<>();
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_image_view);

        intitializeComponents();

        setTitle("Documents");
    }

    private void intitializeComponents() {
        imageURLs =  getIntent().getStringArrayListExtra("URLs");
        currentPage = getIntent().getIntExtra("POSITION", 0);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ImageSlideAdapter(EventImageViewActivity.this, imageURLs));
        mPager.setCurrentItem(currentPage);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == imageURLs.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        /*Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);*/
    }
}
