package com.example.ankurmgoyal.reellookbookv7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by ankurmgoyal on 4/4/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "dor";

    // Table Names
    private static final String TABLE_INVENTORY = "inventory";
    private static final String TABLE_STYLEINFO = "styleinfo";
    private static final String TABLE_PAIRINGS = "pairings";
    private static final String TABLE_LANDINGPAGES = "landingpages";

    private static final String KEY_STYLENUMBER = "stylenumber";
    private static final String KEY_COLOR = "color";

    // INVENTORY Table - column names

    private static final String KEY_Size = "size";
    private static final String KEY_Type = "type";
    private static final String KEY_Quantity = "quantity";
    private static final String KEY_CurrentPrice = "currentprice";
    private static final String KEY_OriginalPrice = "originalprice";

    // STYLEINFO Table - column names
    private static final String KEY_URL = "url";
    private static final String KEY_Type1 = "type1";
    private static final String KEY_Type2 = "type2";
    private static final String KEY_Type3 = "type3";

    // PAIRINGS Table - column names
    private static final String KEY_STYLENUMBER1 = "stylenumber1";
    private static final String KEY_COLOR1 = "color1";
    private static final String KEY_STYLENUMBER2 = "stylenumber2";
    private static final String KEY_COLOR2 = "color2";

    //LANDINGPAGE TABLE - column names
    private static final String KEY_FILTER = "filter";
    private static final String KEY_LEFF = "leftmargin";
    private static final String KEY_TOP = "topmargin";
    private static final String KEY_CLICK = "click";

    // Table Create Statements
    private static final String CREATE_TABLE_INVENTORY = "CREATE TABLE " + TABLE_INVENTORY
            + "(" + KEY_STYLENUMBER + " TEXT NOT NULL,"
            + KEY_COLOR + " TEXT NOT NULL,"
            + KEY_Size + " TEXT NOT NULL,"
            + KEY_Type + " TEXT NOT NULL,"
            + KEY_Quantity + " INTEGER NOT NULL,"
            + KEY_CurrentPrice + " REAL NOT NULL,"
            + KEY_OriginalPrice + " REAL NOT NULL,"
            + "PRIMARY KEY (" + KEY_STYLENUMBER + "," + KEY_COLOR + "," + KEY_Size +")"
            +")";

    // Tag table create statement
    private static final String CREATE_TABLE_STYLEINFO = "CREATE TABLE " + TABLE_STYLEINFO
            + "(" + KEY_STYLENUMBER + " TEXT NOT NULL,"
            + KEY_COLOR + " TEXT NOT NULL,"
            + KEY_URL + " TEXT NOT NULL,"
            + KEY_Type1 + " TEXT NOT NULL,"
            + KEY_Type2 + " TEXT,"
            + KEY_Type3 + " TEXT,"
            + "PRIMARY KEY (" + KEY_STYLENUMBER + "," + KEY_COLOR +")"
            +")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_PAIRINGS = "CREATE TABLE " + TABLE_PAIRINGS
    + "(" + KEY_STYLENUMBER1 + " TEXT NOT NULL,"
            + KEY_COLOR1 + " TEXT NOT NULL,"
            + KEY_STYLENUMBER2 + " TEXT NOT NULL,"
            + KEY_COLOR2 + " TEXT NOT NULL,"
            + "PRIMARY KEY (" + KEY_STYLENUMBER1 + "," + KEY_COLOR1 + "," + KEY_STYLENUMBER2 + "," + KEY_COLOR2 +")"
            +")";

    private static final String CREATE_TABLE_LANDINGPAGES = "CREATE TABLE " + TABLE_LANDINGPAGES
            + "(" + KEY_FILTER + " TEXT PRIMARY KEY NOT NULL,"
            + KEY_URL + " TEXT NOT NULL,"
            + KEY_LEFF + " INT NOT NULL,"
            + KEY_TOP + " INT NOT NULL,"
            + KEY_CLICK + " INT NOT NULL"
            +")";


    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_INVENTORY);
        db.execSQL(CREATE_TABLE_STYLEINFO);
        db.execSQL(CREATE_TABLE_PAIRINGS);
        db.execSQL(CREATE_TABLE_LANDINGPAGES);

        System.out.println("WHEN IS THIS CALLED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STYLEINFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIRINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANDINGPAGES);

        // create new tables
        onCreate(db);
    }


    public boolean createInventoryUnit(InventoryUnit inventoryUnit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STYLENUMBER, inventoryUnit.getStyleNumber());
        values.put(KEY_COLOR, inventoryUnit.getColor());
        values.put(KEY_Size, inventoryUnit.getSize());
        values.put(KEY_Type, inventoryUnit.getType());
        values.put(KEY_Quantity, inventoryUnit.getQuantity());
        values.put(KEY_CurrentPrice, inventoryUnit.getCurrentPrice());
        values.put(KEY_OriginalPrice, inventoryUnit.getOriginalPrice());

        // insert row
        if(getInventoryUnit(inventoryUnit.getStyleNumber(),inventoryUnit.getColor(),inventoryUnit.getSize()) == null) {
            db.insert(TABLE_INVENTORY, null, values);
            //db.close();
            return true;
        }

        //db.close();
        return false;
    }

    public InventoryUnit getInventoryUnit(String styleNumber, String color, String size){
        SQLiteDatabase db = this.getReadableDatabase();


        String selectQuery = "SELECT  * FROM " + TABLE_INVENTORY + " WHERE "
                + KEY_STYLENUMBER + " = '" + styleNumber + "' AND " + KEY_COLOR + " = '" + color
                + "' AND " + KEY_Size + " = '" + size+"'";


        //String selectQuery = "SELECT  * FROM " + TABLE_INVENTORY;


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount()>0) {
            c.moveToFirst();

            String sn = c.getString(0);
            String co = c.getString(1);
            String si = c.getString(2);
            String type = c.getString(3);
            int quantity = c.getInt(4);
            double currentPrice = c.getDouble(5);
            double originalPrice = c.getDouble(6);

            InventoryUnit inventoryUnit = new InventoryUnit(co, currentPrice, originalPrice, quantity, si, sn, type);
            //db.close();
            //c.close();
            return inventoryUnit;
        }
        else{
            //db.close();
            return null;
        }
    }

    public InventoryUnit getInventoryUnit(String styleNumber, String color){
        SQLiteDatabase db = this.getReadableDatabase();


        String selectQuery = "SELECT  * FROM " + TABLE_INVENTORY + " WHERE "
                + KEY_STYLENUMBER + " = '" + styleNumber + "' AND " + KEY_COLOR + " = '" + color + "'";


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount()>0) {
            c.moveToFirst();

            String sn = c.getString(0);
            String co = c.getString(1);
            String si = c.getString(2);
            String type = c.getString(3);
            int quantity = c.getInt(4);
            double currentPrice = c.getDouble(5);
            double originalPrice = c.getDouble(6);

            InventoryUnit inventoryUnit = new InventoryUnit(co, currentPrice, originalPrice, quantity, si, sn, type);
            //db.close();
            //c.close();
            return inventoryUnit;
        }
        else{
            //db.close();
            return null;
        }
    }

    public boolean getAvailability(String styleNumber, String color){
        SQLiteDatabase db = this.getReadableDatabase();


        String selectQuery = "SELECT * FROM " + TABLE_INVENTORY + " WHERE "
                + KEY_STYLENUMBER + " = '" + styleNumber + "' AND " + KEY_COLOR + " = '" + color + "'";


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            boolean avail = false;

            for(int i = 0 ; i<c.getCount(); i++) {
                if(c.getInt(4) > 0){
                    avail = true;
                }
                c.moveToNext();
            }

            //db.close();
            //c.close();
            return avail;
        }
        else{
            //db.close();
            return false;
        }
    }

    public String getSizeType(String styleNumber, String color){
        SQLiteDatabase db = this.getReadableDatabase();


        String selectQuery = "SELECT  "+ KEY_Type +","+ KEY_Size+" FROM " + TABLE_INVENTORY + " WHERE "
                + KEY_STYLENUMBER + " = '" + styleNumber + "' AND " + KEY_COLOR + " = '" + color +"'";


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();

            String type = c.getString(0);
            String sz = c.getString(1);

            if(type.equals("Regular")){
                if(sz.length()<3){
                    type= "Regular Single";
                }
                else{
                    type = "Regular Long";
                }
            }
            else if(type.equals("Numerical")){
                if(sz.length()>1 && (sz.substring(0,1).equals("3") || sz.substring(0,1).equals("2"))){
                    type = "Numerical";
                }
                else{
                    type = "Small Numerical";
                }
            }

            //db.close();
            //c.close();
            return type;
        }
        else{
            //db.close();
            return null;
        }
    }

    public void clearInventoryTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INVENTORY,null,null);
        //db.close();
    }

    public void createStyleUnit(StyleUnit styleUnit){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STYLENUMBER, styleUnit.getStyleNumber());
        values.put(KEY_COLOR, styleUnit.getColor());
        values.put(KEY_URL, styleUnit.getUrl());
        values.put(KEY_Type1, styleUnit.getType());
        values.put(KEY_Type2, styleUnit.getType2());
        values.put(KEY_Type3, styleUnit.getType3());

        // insert row
        db.insert(TABLE_STYLEINFO, null, values);
        //db.close();
    }

    public StyleUnit getStyleUnit(String styleNumber, String color){
        SQLiteDatabase db = this.getReadableDatabase();


        String selectQuery = "SELECT  * FROM " + TABLE_STYLEINFO + " WHERE "
                + KEY_STYLENUMBER + " = '" + styleNumber + "' AND " + KEY_COLOR + " = '" + color +"'";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();

            String sn = c.getString(0);
            String co = c.getString(1);
            String url = c.getString(2);
            String type = c.getString(3);
            String type2 = c.getString(4);
            String type3 = c.getString(5);

            StyleUnit styleUnit = new StyleUnit(co,sn,type,type2,type3,url);
            //db.close();
            //c.close();
            return styleUnit;
        }
        else{
            //db.close();
            return null;
        }
    }

    public ArrayList<StyleNode> getAllStylesOfType(String type){
        SQLiteDatabase db = this.getReadableDatabase();


        String selectQuery = "SELECT "+KEY_STYLENUMBER+","+KEY_COLOR+" FROM " + TABLE_STYLEINFO + " WHERE "
                + KEY_Type1 + " = '" + type + "' OR "+ KEY_Type2 + " = '" + type + "' OR "+ KEY_Type3 + " = '" + type + "'";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            ArrayList<StyleNode> toreturn = new ArrayList<StyleNode>();
            for(int i = 0; i< c.getCount(); i++){
                String sn = c.getString(0);
                String co = c.getString(1);
                StyleNode styleNode = new StyleNode(sn,co);
                toreturn.add(styleNode);
                c.moveToNext();
            }
            //db.close();
            //c.close();
            return toreturn;
        }
        else{
            //db.close();
            return null;
        }
    }

    public int getNumberStylesOfType(String type){
        SQLiteDatabase db = this.getReadableDatabase();


        String selectQuery = "SELECT "+KEY_STYLENUMBER+","+KEY_COLOR+" FROM " + TABLE_STYLEINFO + " WHERE "
                + KEY_Type1 + " = '" + type + "' OR "+ KEY_Type2 + " = '" + type + "' OR "+ KEY_Type3 + " = '" + type + "'";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            //db.close();
            //c.close();
            return c.getCount();
        }
        else{
            //db.close();
            return 0;
        }
    }

    public void clearStyleTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STYLEINFO,null,null);
        //db.close();
    }

    public void createPairing(String stylenumber1, String color1, String stylenumber2, String color2){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STYLENUMBER1, stylenumber1);
        values.put(KEY_COLOR1, color1);
        values.put(KEY_STYLENUMBER2, stylenumber2);
        values.put(KEY_COLOR2, color2);

        // insert row
        db.insert(TABLE_PAIRINGS, null, values);
        //db.close();
    }

    public ArrayList<StyleUnit> getPairings(String stylenumber, String color){

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT "+TABLE_PAIRINGS+"."+KEY_STYLENUMBER2+","+
                TABLE_PAIRINGS+"."+KEY_COLOR2+","+ TABLE_STYLEINFO+"."+KEY_URL+
                " FROM "+ TABLE_PAIRINGS+ " INNER JOIN "+TABLE_STYLEINFO+
                " ON "+TABLE_PAIRINGS+"."+KEY_STYLENUMBER2+" = "+TABLE_STYLEINFO+"."+KEY_STYLENUMBER + " AND " +
                TABLE_PAIRINGS+"."+KEY_COLOR2+" = "+TABLE_STYLEINFO+"."+KEY_COLOR +
                " WHERE " + TABLE_PAIRINGS+"."+KEY_STYLENUMBER1 + " = '"+stylenumber+
                "' AND "+ TABLE_PAIRINGS+"."+KEY_COLOR1 + " = '" + color +"'";


        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<StyleUnit> toReturn = new ArrayList<StyleUnit>();

        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                String sn = c.getString(0);
                //System.out.println("Adding "+sn+" to the list in loop 1");
                String co = c.getString(1);
                String url = c.getString(2);
                StyleUnit temp = new StyleUnit(co,sn,null,null,null,url);
                toReturn.add(temp);
                c.moveToNext();
            }
        }

        selectQuery = "SELECT "+TABLE_PAIRINGS+"."+KEY_STYLENUMBER1+","+
                TABLE_PAIRINGS+"."+KEY_COLOR1+","+ TABLE_STYLEINFO+"."+KEY_URL+
                " FROM "+ TABLE_PAIRINGS+ " INNER JOIN "+TABLE_STYLEINFO+
                " ON "+TABLE_PAIRINGS+"."+KEY_STYLENUMBER1+" = "+TABLE_STYLEINFO+"."+KEY_STYLENUMBER + " AND " +
                TABLE_PAIRINGS+"."+KEY_COLOR1+" = "+TABLE_STYLEINFO+"."+KEY_COLOR +
                " WHERE " + TABLE_PAIRINGS+"."+KEY_STYLENUMBER2 + " = '"+stylenumber+
                "' AND "+ TABLE_PAIRINGS+"."+KEY_COLOR2 + " = '" + color +"'";


        c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                String sn = c.getString(0);
                //System.out.println("Adding "+sn+" to the list in loop 2");
                String co = c.getString(1);
                String url = c.getString(2);
                StyleUnit temp = new StyleUnit(co,sn,null,null,null,url);
                toReturn.add(temp);
                c.moveToNext();
            }
        }

        if (toReturn.size() == 0){
            return null;
        }
        else{
            return toReturn;
        }

    }

    public void clearPairingTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PAIRINGS,null,null);
        //db.close();
    }

    public boolean createLandingPage(String filter, String url, int left, int top, int click){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FILTER, filter);
        values.put(KEY_URL, url);
        values.put(KEY_LEFF, left);
        values.put(KEY_TOP, top);
        values.put(KEY_CLICK, click);

        // insert row
        try {
            db.insert(TABLE_LANDINGPAGES, null, values);
            return true;
        }
        catch(Exception e) {
            return false;
        }
        //db.close();
    }

    public String[] getLandingPagePics(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_URL + " FROM " + TABLE_LANDINGPAGES;
        Cursor c = db.rawQuery(selectQuery, null);
        String[] toReturn = new String[c.getCount()];

        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                toReturn[i]=c.getString(0);
                c.moveToNext();
            }
        }

        return toReturn;

    }

    public String[] getFilters(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_FILTER + " FROM " + TABLE_LANDINGPAGES;
        Cursor c = db.rawQuery(selectQuery, null);
        String[] toReturn = new String[c.getCount()];

        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                String string = c.getString(0);
                toReturn[i] = string;
                c.moveToNext();
            }
        }

        return toReturn;

    }

    public int[] getLeft(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_LEFF + " FROM " + TABLE_LANDINGPAGES;
        Cursor c = db.rawQuery(selectQuery, null);
        int[] toReturn = new int[c.getCount()];

        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                int num = c.getInt(0);
                toReturn[i] = num;
                c.moveToNext();
            }
        }

        return toReturn;

    }

    public int[] getTop(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_TOP + " FROM " + TABLE_LANDINGPAGES;
        Cursor c = db.rawQuery(selectQuery, null);
        int[] toReturn = new int[c.getCount()];

        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                int num = c.getInt(0);
                toReturn[i] = num;
                c.moveToNext();
            }
        }

        return toReturn;

    }

    public int[] getClick(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_CLICK + " FROM " + TABLE_LANDINGPAGES;
        Cursor c = db.rawQuery(selectQuery, null);
        int[] toReturn = new int[c.getCount()];

        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                int num = c.getInt(0);
                toReturn[i] = num;
                c.moveToNext();
            }
        }

        return toReturn;

    }

    public void clearLandingTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LANDINGPAGES,null,null);
        //db.close();
    }
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
