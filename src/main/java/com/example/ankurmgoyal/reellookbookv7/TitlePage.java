package com.example.ankurmgoyal.reellookbookv7;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class TitlePage extends Activity {

    Tracker mTracker;
    Handler handler;
    Runnable runnable;

    int landingPageTracker = 0;
   // Bitmap[] landingPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_page);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        sync();
        //downloadDrawableImages();
    }


    private void wakeUp(String filter){
        Intent i = new Intent(this,MainActivity.class);
        Bundle extras = new Bundle();
        if(getIntent().getExtras()!=null) {
            extras.putString("ID",getIntent().getExtras().get("ID").toString());
        }
        extras.putString("Filter",filter);
        i.putExtras(extras);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();


        final int[] clicks = {R.id.clickHere,R.id.clickHereBlack};

        //Animation

        //DataBaseHelper db = new DataBaseHelper(getApplicationContext());


        final int[] leftMargin = {400,400,100,400};
        final int[] topMargin = {850,850,850,850};
        final int[] clickChoice = {1,0,1,1};
        final int[] drawables = {R.drawable.laceup_background,R.drawable.rompers,R.drawable.bodysuit_background, R.drawable.mesh_landing};

        final AnimationSet as = new AnimationSet(true);
        as.setInterpolator(new AccelerateInterpolator());

        Animation animation = new AlphaAnimation(0f,1f);
        animation.setDuration(500);

        Animation animation2 = new AlphaAnimation(1f,1f);
        animation2.setDuration(6000);
        animation2.setStartOffset(500);

        Animation animation3 = new AlphaAnimation(1f,0f);
        animation3.setDuration(500);
        animation3.setStartOffset(6500);

        as.addAnimation(animation);
        as.addAnimation(animation2);
        as.addAnimation(animation3);

        final Animation animation4 = new AlphaAnimation(0f,1f);
        animation4.setDuration(400);
        animation4.setRepeatCount(15);
        animation4.setRepeatMode(Animation.REVERSE);


        handler = new Handler();
        runnable = new Runnable() {

            @Override
            public void run() {
                if (landingPageTracker > drawables.length-1){
                    landingPageTracker = 0;
                }

                ImageView splash = (ImageView) findViewById(R.id.background);
                splash.setBackgroundResource(drawables[landingPageTracker]);
                    splash.startAnimation(as);
                ImageView update = (ImageView) findViewById(R.id.newUpdate);
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                int tempLayoutMargin;
                if(leftMargin[landingPageTracker]>200){
                    tempLayoutMargin = 500;
                }
                else{
                    tempLayoutMargin = 0;
                }
                lp2.setMargins(pix(tempLayoutMargin), 0, 0, 0);
                update.setLayoutParams(lp2);
                update.startAnimation(as);

                    ImageView clickHere = (ImageView) findViewById(clicks[clickChoice[landingPageTracker]]);
                    clickHere.startAnimation(animation4);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    lp.setMargins(pix(leftMargin[landingPageTracker]), pix(topMargin[landingPageTracker]), 0, 0);
                    clickHere.setLayoutParams(lp);
                    //clickHere.setLeft(leftMargin[landingPageTracker]);
                    //clickHere.setTop(topMargin[landingPageTracker]);
                    landingPageTracker++;
                handler.postDelayed(this, 7200);
            }
        };
        handler.postDelayed(runnable,0);
    }

    private int pix(int dp){
        float d = getResources().getDisplayMetrics().density;
        return (int)(dp * d);
    }

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

    private void sendButtonClickToGoogle(String category){
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Page: 0")
                .setAction(category)
                .build());
    }

    public void clickHereClick(View v){
        String[] filters = {"Laceup","Romper","Bodysuit","Mesh"};
        try{
            wakeUp(filters[landingPageTracker-1]);
            sendButtonClickToGoogle(filters[landingPageTracker-1]);
        }
        catch (Exception e){
            wakeUp(filters[0]);
            sendButtonClickToGoogle(filters[0]);
        }
    }

    protected void testing(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {

                    DataBaseHelper db = new DataBaseHelper(getApplicationContext());
                    InventoryUnit inventoryUnit = db.getInventoryUnit("22892","BLK","LARGE");
                    if(inventoryUnit != null){
                        System.out.println(inventoryUnit.getQuantity() + " Really is a duplicate somehow");
                    }

                }
                catch(Exception e){
                    System.out.println("this failed");
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    protected void sync(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try{
                    DataBaseHelper db = new DataBaseHelper(getApplicationContext());
                    //db.getWritableDatabase();
                    db.clearStyleTable();
                    db.clearPairingTable();
                    db.clearInventoryTable();
                    //db.clearLandingTable();

                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = null;
                    conn = DriverManager.getConnection("jdbc:mysql://107.180.43.9:3306/dor", "xxxx", "xxxx");
                    Statement statement = conn.createStatement();

                    //understand number of items
                    ResultSet rs = statement.executeQuery("SELECT * FROM  styleInfo");
                    int i = 0;
                    while (rs.next()){
                        String sn = rs.getString(1);
                        String co = rs.getString(2);
                        String url = rs.getString(3);
                        String type = rs.getString(4);
                        String type2 = rs.getString(5);
                        String type3 = rs.getString(6);
                        StyleUnit styleUnit = new StyleUnit(co,sn,type,type2,type3,url);
                        db.createStyleUnit(styleUnit);
                        i++;
                    }
                    rs.close();
                    System.out.println("Successfully added " + i + " items");

                    rs = statement.executeQuery("SELECT * FROM  pairings");
                    i = 0;
                    while (rs.next()){
                        String sn1 = rs.getString(1);
                        String co1 = rs.getString(2);
                        String sn2 = rs.getString(3);
                        String co2 = rs.getString(4);
                        db.createPairing(sn1,co1,sn2,co2);
                        i++;
                    }
                    rs.close();
                    System.out.println("Successfully added " + i + " items");

                    rs = statement.executeQuery("SELECT * FROM main INNER JOIN styleInfo ON main.StyleNumber = styleInfo.StyleNumber");
                    i = 0;
                    while (rs.next()){
                        String sn = rs.getString(1);
                        String co = rs.getString(2);
                        String size = rs.getString(3);
                        String type = rs.getString(4);
                        int quantity = rs.getInt(5);
                        double currentPrice = rs.getDouble(6);
                        double originalPrice = rs.getDouble(7);
                        InventoryUnit inventoryUnit = new InventoryUnit(co,currentPrice,originalPrice,quantity,size,sn,type);
                        if (db.createInventoryUnit(inventoryUnit)) {
                            i++;
                        }
                    }
                    rs.close();
                    System.out.println("Successfully added " + i + " items");

                    rs = statement.executeQuery("SELECT * FROM main INNER JOIN pairings ON main.StyleNumber = pairings.StyleNumber1");
                    i = 0;
                    while (rs.next()){
                        String sn = rs.getString(1);
                        String co = rs.getString(2);
                        String size = rs.getString(3);
                        String type = rs.getString(4);
                        int quantity = rs.getInt(5);
                        double currentPrice = rs.getDouble(6);
                        double originalPrice = rs.getDouble(7);
                        InventoryUnit inventoryUnit = new InventoryUnit(co,currentPrice,originalPrice,quantity,size,sn,type);
                        if (db.createInventoryUnit(inventoryUnit)) {
                            i++;
                        }
                    }
                    rs.close();
                    System.out.println("Successfully added " + i + " items");

                    rs = statement.executeQuery("SELECT * FROM main INNER JOIN pairings ON main.StyleNumber = pairings.StyleNumber2");
                    i = 0;
                    while (rs.next()){
                        String sn = rs.getString(1);
                        String co = rs.getString(2);
                        String size = rs.getString(3);
                        String type = rs.getString(4);
                        int quantity = rs.getInt(5);
                        double currentPrice = rs.getDouble(6);
                        double originalPrice = rs.getDouble(7);
                        InventoryUnit inventoryUnit = new InventoryUnit(co,currentPrice,originalPrice,quantity,size,sn,type);
                        if (db.createInventoryUnit(inventoryUnit)) {
                            i++;
                        }
                    }
                    rs.close();
                    System.out.println("Successfully added " + i + " items");

                    /*
                    rs = statement.executeQuery("Select * FROM landingpages");
                    i = 0;
                    while(rs.next()){
                        String filter = rs.getString(1);
                        String url = rs.getString(2);
                        int left = rs.getInt(3);
                        int top = rs.getInt(4);
                        int click = rs.getInt(5);
                        //if(db.createLandingPage(filter,url,left,top,click)){i++;}
                    }
                    rs.close();
                    System.out.println("Successfully added " + i + " items");
                    */

                    conn.close();
                }
                catch(Exception e){
                    System.out.println("sync error");
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    /*
    protected void downloadDrawableImages(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try{
                    DataBaseHelper db = new DataBaseHelper(getApplicationContext());
                    String[] images = db.getLandingPagePics();
                    landingPages = new Bitmap[images.length];
                    for(int i = 0;i<images.length;i++){
                        URL url = new URL(images[i]);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        landingPages[i] = BitmapFactory.decodeStream(input);
                    }
                }
                catch (Exception e){

                }
                return null;
            }
        }.execute();
    }

    */

    //Remove Callbacks
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }


}
