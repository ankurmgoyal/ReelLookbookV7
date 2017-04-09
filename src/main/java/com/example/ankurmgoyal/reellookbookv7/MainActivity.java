package com.example.ankurmgoyal.reellookbookv7;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    int numFrags;
    FirstFragment fragments[];
    ArrayList<StyleNode> items;

    //Timer
    private long startTime;
    private long currentSpan;
    private static final long INACTIVITY_TOLERANCE = 20000;
    private Handler handler;
    private Runnable runnable;

    //Google Analytics
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragments();
    }

    ////////////////////////////////////////ON CREATE HELPERS//////////////////////////////////////
    private void setPagerAdapter(){
        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                resetTimer();
                TextView tv = (TextView) findViewById(R.id.pagecounter);
                tv.setText("Look " + (position + 1) + " of " + fragments.length);
                if(fragments[position] != null) {
                    fragments[position].popUpTags();
                    sendPageChangeToGoogle(fragments[position].styleNumber);
                }
            }
        });
        pager.setOffscreenPageLimit(3);
    }

    private void setAnalytics(){
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        //sendPageChangeToGoogle(fragments[0].name);
    }

    private void setFragments(){

        TextView loading = (TextView) findViewById(R.id.loading);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/TahomaBold.ttf");
        loading.setTextSize(25);
        loading.setTypeface(face);


        //this understands how many items meet the current filter criteria and sets up an array containing relevant style numbers
        new AsyncTask<Void, Integer, Void>() {
            @Override public Void doInBackground(Void... arg) {
                try {
                    DataBaseHelper db = new DataBaseHelper(getApplicationContext());
                    String currentcategory = getIntent().getExtras().get("Filter").toString();
                    numFrags = db.getNumberStylesOfType(currentcategory);
                    System.out.println(numFrags);
                    items = db.getAllStylesOfType(currentcategory);
                    System.out.println("success");
                }
                catch(Exception e){
                    System.out.println("nope");
                    System.out.println(e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                TextView loading = (TextView) findViewById(R.id.loading);
                loading.setVisibility(View.GONE);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);


                fragments = new FirstFragment[numFrags];
                setPagerAdapter();
                setAnalytics();
                setPageCounter();
                startTimer();
            }

            protected void onProgressUpdate(Integer... values) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setProgress(values[0]);
                super.onProgressUpdate(values);
            }

        }.execute();
    }


    private void setPageCounter(){
        TextView tv = (TextView)findViewById(R.id.pagecounter);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/TahomaBold.ttf");
        tv.setText("Look 1 of "+fragments.length);
        tv.setTypeface(face);
        tv.setTextColor(Color.WHITE);
        tv.setShadowLayer(3,3,3,Color.BLACK);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);
    }

    public void navright(View view){
        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
    }

    public void navleft(View view){
        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setCurrentItem(pager.getCurrentItem() - 1, true);
    }

    public void backToCover(View view){
        backToCover();
    }

    ////////////////////////////////////////CREATE TAGS/////////////////////////////////////////////

    private void setTag(FirstFragment curr, int dataPosition){
        String styleNumber = items.get(dataPosition).getStyleNumber();
        String color = items.get(dataPosition).getColor();
        curr.styleNumber = styleNumber;
        curr.color = color;
    }

    //////////////////////////////////////////TIMER METHODS///////////////////////////////////////

    //Returns back to cover after set period of inactivity
    private void startTimer(){
        startTime = System.currentTimeMillis();
        handler = new Handler();
        runnable = new Runnable() {

            @Override
            public void run() {
                currentSpan = System.currentTimeMillis() - startTime;
                if (currentSpan > INACTIVITY_TOLERANCE) {
                    backToCover();
                    resetTimer();
                }
                handler.postDelayed(this, INACTIVITY_TOLERANCE/2);
            }
        };
        handler.postDelayed(runnable, INACTIVITY_TOLERANCE/2);
    }

    //Resets timer to 0
    private void resetTimer(){
        startTime = System.currentTimeMillis();
    }

    //Returns back to TitlePage
    private void backToCover(){
        Intent i = new Intent(this,TitlePage.class);
        i.putExtra("ID",getIntent().getExtras().get("ID").toString());
        startActivity(i);
        finish();
    }


    //Remove Callbacks
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    //Resets Timer to 0 when motion detected
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        resetTimer();
        return super.dispatchTouchEvent(ev);
    }

    /////////////////////////////////////////ANALYTICS///////////////////////////////////////////////

    private void sendPageChangeToGoogle(String pageNumber){
        mTracker.setScreenName(pageNumber);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    //////////////////////////////////////////OTHER////////////////////////////////////////////////
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            FirstFragment curr = FirstFragment.newInstance(pos);
            setTag(curr, pos);
            fragments[pos] = curr;
            return fragments[pos];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            fragments[position] = null;
            super.destroyItem(container, position, object);
        }


        @Override
        public int getCount() {
            return fragments.length;
        }
    }


}
