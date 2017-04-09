package com.example.ankurmgoyal.reellookbookv7;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {

    //ArrayList<Tag> tags;

    Tag tag;

    int fragPosition;
    String styleNumber;
    String color;

    Drawable dr = null;

    View root;
    RelativeLayout layout;
    int tagViewID;

    private final int pinkTagMargin = 35;
    private final int priceTagWidth = 110;
    private final int priceTagVerticalBuffer = 6;
    private final int toggleMargins = 20;

    Tracker mTracker;

    /////////////////////////////////INITIALIZING///////////////////////////////////////////////////

    public FirstFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_first, container, false);
        layout = (RelativeLayout) root.findViewById(R.id.mainbackground);

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.set("&uid",getActivity().getIntent().getExtras().get("ID").toString());

        setTag();
        return root;

    }

    public static FirstFragment newInstance(int i) {
        FirstFragment f = new FirstFragment();
        f.fragPosition = i;
        return f;
    }

    /////////////////////////////////SETTING////////////////////////////////////////////////////////

    public void setTag(){

        new AsyncTask<Void, Void, Void>() {
            double price;
            int quantity;

            @Override public Void doInBackground(Void... arg) {
                try {

                    DataBaseHelper db = new DataBaseHelper(getContext());
                    InventoryUnit inventoryUnit = db.getInventoryUnit(styleNumber,color);
                    if(inventoryUnit != null) {
                        price = inventoryUnit.getCurrentPrice();
                        quantity = inventoryUnit.getQuantity();
                    }
                    else{
                        price = 0.00;
                        quantity = 0;
                    }
                }
                catch(Exception e){
                    System.out.println("Tag Error");
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                tag = new Tag(quantity > 0 ,300,180,price);
                //tags.add(tag);
                try {
                    createTags();
                }
                catch(Exception e){
                    System.out.println("Error creating tags");
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /////////////////////////////////CREATING///////////////////////////////////////////////////////


    @Override
    public void onResume() {
        super.onResume();
        createBackground();
        System.out.println(styleNumber + " "+ color);
    }

    private void createBackground(){

        TextView loading = (TextView) layout.findViewById(R.id.loading);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TahomaBold.ttf");
        loading.setTextSize(25);
        loading.setTypeface(face);

        //queries database to get image associated with styleNumer and Color
        new AsyncTask<Void, Integer, Void>() {
            @Override public Void doInBackground(Void... arg) {
                try {

                    DataBaseHelper db = new DataBaseHelper(getContext());
                    StyleUnit styleUnit = db.getStyleUnit(styleNumber,color);
                    URL url;
                    if (styleUnit != null){
                        url = new URL(styleUnit.getUrl());
                    }
                    else{
                        url = new URL("http://www.novelupdates.com/img/noimagefound.jpg");
                    }
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    dr = new BitmapDrawable(getResources(),myBitmap);
                }
                catch(Exception e){
                    System.out.println("Image Error");
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);

                TextView loading = (TextView) layout.findViewById(R.id.loading);
                loading.setVisibility(View.GONE);

                layout.setBackground(dr);
                ImageButton moreinfo = (ImageButton) layout.findViewById(R.id.moreinfo);
                moreinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendButtonClickToGoogle("More Info");
                        v.setVisibility(View.GONE);
                        TextView textView = (TextView)layout.findViewById(R.id.inventory_title);
                        TextView textView2 = (TextView)layout.findViewById(R.id.recommendation_title);
                        TextView hide = (TextView)layout.findViewById(R.id.hide);
                        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TahomaBold.ttf");

                        textView.setTypeface(face);
                        textView.setTextSize(20);
                        //textView.setVisibility(View.VISIBLE);

                        textView2.setTypeface(face);
                        textView2.setTextSize(20);
                        //textView2.setVisibility(View.VISIBLE);

                        hide.setTypeface(face);
                        hide.setTextSize(16);

                        Animation animation = new TranslateAnimation(0,0,500,0);
                        animation.setDuration(500);
                        animation.setInterpolator(new DecelerateInterpolator());

                        layout.findViewById(R.id.infopane).startAnimation(animation);
                        layout.findViewById(R.id.infopane).setVisibility(View.VISIBLE);

                        //renderSizeSquare();

                        try {
                            getSizeAvailability();
                            getRecommendations();
                        }
                        catch(Exception e){
                            System.out.println("Error getting info");
                            e.printStackTrace();
                        }

                    }
                });

                TextView hide = (TextView) layout.findViewById(R.id.hide);
                hide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sendButtonClickToGoogle("Hide Info");
                        layout.findViewById(R.id.moreinfo).setVisibility(View.VISIBLE);
                        Animation animation = new TranslateAnimation(0,0,0,500);
                        animation.setDuration(500);
                        animation.setInterpolator(new DecelerateInterpolator());

                        layout.findViewById(R.id.infopane).startAnimation(animation);
                        layout.findViewById(R.id.infopane).setVisibility(View.INVISIBLE);
                    }
                });
                moreinfo.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
                progressBar.setProgress(values[0]);
                super.onProgressUpdate(values);
            }
        }.execute();
    }

    private void createTags(){

        if(tag != null){
            RelativeLayout tagLayout = new RelativeLayout(getActivity());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tagLayout.setLayoutParams(lp);
            tagLayout.setId(View.generateViewId());
            tagViewID=tagLayout.getId();

            renderTag(tag,tagLayout);

            if(fragPosition > 0) {
                tagLayout.setVisibility(View.INVISIBLE);
            }
            layout.addView(tagLayout);
        }
    }

    //helper method called by create tags
    private void renderTag(Tag tag,RelativeLayout relativeLayout){

        //creates yellow circle
        ImageView icon = new ImageView(getContext());
        icon.setBackgroundResource(R.drawable.tag_set);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.setMargins(pix(tag.getLeft()), pix(tag.getTop()), 0, 0);
        icon.setLayoutParams(lp);
        icon.setId(View.generateViewId());
        icon.setVisibility(View.INVISIBLE);
        relativeLayout.addView(icon);


        //creates pink tag
        ImageView pinkTag = new ImageView(getContext());
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_BOTTOM, icon.getId());
        pinkTag.setBackgroundResource(R.drawable.clothes_left);
        lp2.addRule(RelativeLayout.ALIGN_RIGHT, icon.getId());
        lp2.setMargins(0, 0, pix(pinkTagMargin), 0);
        pinkTag.setLayoutParams(lp2);
        pinkTag.setId(View.generateViewId());
        relativeLayout.addView(pinkTag);


        //creates price
        TextView tv = new TextView(getContext());
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TahomaBold.ttf");
        tv.setText("$" + tag.getPrice());
        tv.setTypeface(face);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(pix(22));
        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(pix(priceTagWidth), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.addRule(RelativeLayout.ALIGN_LEFT, pinkTag.getId());
        lp3.setMargins(0, priceTagVerticalBuffer,0, 0);
        lp3.addRule(RelativeLayout.ALIGN_TOP, pinkTag.getId());

        tv.setLayoutParams(lp3);
        relativeLayout.addView(tv);
    }



    /////////////////////////////////BUTTON ACTIONS/////////////////////////////////////////////////

    public void popUpTags(){
        if(layout.findViewById(tagViewID) != null) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.popup);
            RelativeLayout rl = (RelativeLayout) layout.findViewById(tagViewID);
            rl.startAnimation(anim);
            rl.setVisibility(View.VISIBLE);
        }

    }

    private void sendButtonClickToGoogle(String category){
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Page: "+styleNumber)
                .setAction(category)
                .build());
    }

    private void sendButtonClickToGoogle(String category, String type){
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Page:"+styleNumber)
                .setAction(category)
                .setLabel("Item: "+type)
                .build());
    }

    /////////////////////////////////HELPERS///////////////////////////////////////////////////////

    private int pix(int dp){
        float d = getContext().getResources().getDisplayMetrics().density;
        return (int)(dp * d);
    }

    /////////////////////////////////DATABASE///////////////////////////////////////////////////////

    private void getSizeAvailability(){
        new AsyncTask<Void, Integer, Void>() {
            @Override public Void doInBackground(Void... arg) {
                try {

                    DataBaseHelper db = new DataBaseHelper(getContext());
                    String sizeType = db.getSizeType(styleNumber,color);
                    if(sizeType != null){
                        String[] sizes = SizeMap.getSizes(sizeType);
                        for (int i = 0; i < sizes.length; i++){
                            sizeGetter(sizes[i],i);
                        }
                    }
                }
                catch(Exception e){
                    System.out.println("Size Availability Error");
                    System.out.println(e.getMessage());
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
            }

        }.execute();

    }

    private void sizeGetter(final String size, final int column){
        new AsyncTask<Void,Integer,Void>(){
            int quantity;
            @Override
            protected Void doInBackground(Void... params) {
                try{
                    DataBaseHelper db = new DataBaseHelper(getContext());
                    InventoryUnit inventoryUnit = db.getInventoryUnit(styleNumber,color,size);
                    if(inventoryUnit != null){
                        quantity = inventoryUnit.getQuantity();
                    }
                    else{
                        quantity = 0;
                    }
                }
                catch(Exception e){
                    System.out.println("Size Getter Error");
                    System.out.println(e.getMessage());
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
                super.onPostExecute(aVoid);
                if(size.equals("SMALL")){
                    //sizeList.add(new SizeNode(quantity,"S"));
                   createSizeSquare(column,"S",quantity);
                }
                else if(size.equals("MED")){
                    //sizeList.add(new SizeNode(quantity,"M"));
                    createSizeSquare(column,"M",quantity);
                }
                else if(size.equals("LARGE")){
                    //sizeList.add(new SizeNode(quantity,"L"));
                   createSizeSquare(column,"L",quantity);
                }
                else {
                    //sizeList.add(new SizeNode(quantity,size));
                    createSizeSquare(column, size, quantity);
                }
            }
        }.execute();
    }


    private void createSizeSquare(int column, String size, int quantity){
        GridLayout gridLayout = (GridLayout) layout.findViewById(R.id.sizeGrid);

        TextView box = new TextView(getContext());
        box.setText(size);
        if(quantity > 0) {
            box.setBackgroundColor(Color.parseColor("#2fbc63"));
        }
        else{
            box.setBackgroundColor(Color.parseColor("#000000"));
        }
        GridLayout.LayoutParams param =new GridLayout.LayoutParams();
        param.height = pix(50);
        param.width = pix(75);
        param.rightMargin = 5;
        param.topMargin = 5;
        param.setGravity(Gravity.CENTER);
        param.columnSpec = GridLayout.spec(column);
        param.rowSpec = GridLayout.spec(0);
        box.setLayoutParams (param);
        box.setGravity(Gravity.CENTER);
        box.setTextSize(18);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TahomaBold.ttf");
        box.setTypeface(face);
        box.setTextColor(Color.WHITE);
        gridLayout.addView(box);
    }

    private void getRecommendations(){

        new AsyncTask<Void, Integer, Void>() {
            @Override public Void doInBackground(Void... arg) {
                try {
                    DataBaseHelper db = new DataBaseHelper(getContext());
                    ArrayList<StyleUnit> pairings = db.getPairings(styleNumber,color);
                    if(pairings != null) {
                        for(int i = 0; i < pairings.size();i++){
                            if(i == 3){
                                break;
                            }
                            StyleUnit temp = pairings.get(i);
                            createStyleBox(temp.getStyleNumber(), temp.getColor(), temp.getUrl(), i);
                        }
                    }
                    else{
                        saySorry();
                    }

                }
                catch(Exception e){
                    System.out.println("Get Recommendation Error");
                    System.out.println(e.getMessage());
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
            }

        }.execute();

    }

    private void saySorry(){

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                TextView textView = new TextView(layout.getContext());
                textView.setText("Sorry, there do not seem to be any\nsuggested pairings for this item");
                textView.setTextSize(18);
                textView.setTextColor(Color.BLACK);
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TahomaBold.ttf");
                textView.setTypeface(face);
                textView.setGravity(Gravity.CENTER);

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.height = pix(300);
                lp.width = pix(500);
                lp.rightMargin = pix(5);
                lp.columnSpec = GridLayout.spec(0);
                lp.rowSpec = GridLayout.spec(0);

                textView.setLayoutParams(lp);

                GridLayout grid = (GridLayout)layout.findViewById(R.id.recommendationGrid);
                grid.addView(textView);
            }
        }.execute();
    }

    private void createStyleBox(final String styleNumber, final String color, final String image, final int position){
        new AsyncTask<Void,Void,Void>(){

            Drawable drawable;
            double price;
            boolean available;

            @Override
            protected Void doInBackground(Void... params) {
                try{
                    URL url = new URL(image);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    drawable = new BitmapDrawable(getResources(),myBitmap);

                    DataBaseHelper db = new DataBaseHelper(getContext());
                    InventoryUnit iu = db.getInventoryUnit(styleNumber,color);
                    if(iu != null) {
                        price = iu.getCurrentPrice();
                    }
                    else{
                        price = 0.00;
                    }
                    available = db.getAvailability(styleNumber,color);
                }
                catch(Exception e){
                    System.out.println("Creating Style Box Error");
                    System.out.println(e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //ImageView imageView = new ImageView(layout.getContext());
                ToggleButton toggle = new ToggleButton(layout.getContext());
                toggle.setBackgroundColor(Color.WHITE);
                toggle.setAlpha(0f);
                toggle.setText("");
                toggle.setTextOff("");
                if(available){
                    toggle.setTextOn("Price: $" + price+ "\n" +"\n" + "\n"+ "In Stock");
                }
                else{
                    toggle.setTextOn("Price: $" + price+ "\n" +"\n" + "\n" + "Not In Stock");
                }
                toggle.setTextSize(18);
                toggle.setTextColor(Color.BLACK);
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TahomaBold.ttf");
                toggle.setTypeface(face);

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.height = pix(300);
                lp.width = pix(187);
                lp.rightMargin = pix(5);
                lp.columnSpec = GridLayout.spec(position);
                lp.rowSpec = GridLayout.spec(0);

                toggle.setLayoutParams(lp);

                toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            buttonView.setAlpha(.8f);
                            sendButtonClickToGoogle("Recommendation Details", styleNumber);
                        }
                        else{
                            buttonView.setAlpha(0f);
                        }
                    }
                });
                GridLayout grid = (GridLayout)layout.findViewById(R.id.recommendationGridOverlay);
                grid.addView(toggle);

                ImageView imageView = new ImageView(layout.getContext());
                imageView.setBackground(drawable);
                imageView.setLayoutParams(lp);
                GridLayout grid2 = (GridLayout)layout.findViewById(R.id.recommendationGrid);
                grid2.addView(imageView);

            }
        }.execute();
    }

}
