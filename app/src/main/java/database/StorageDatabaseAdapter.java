package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Asmaa on 28/12/2015.
 */
public class StorageDatabaseAdapter {
    StorageHelper helper ;


    public long insertMarket(int id , String name , String url , String details, String other , String place_name, String mlong , String mlatt , String mupdates){
        SQLiteDatabase db =    helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(StorageHelper.market_id, id);
        contentValues.put(StorageHelper.market_name , name);
        contentValues.put(StorageHelper.market_url , url);
        contentValues.put(StorageHelper.market_details, details);
        contentValues.put(StorageHelper.market_other, other);
        contentValues.put(StorageHelper.market_place_name, place_name);
        contentValues.put(StorageHelper.market_long, mlong);
        contentValues.put(StorageHelper.market_latt, mlatt);
        contentValues.put(StorageHelper.market_on_updates, mupdates);

        long insert_id = db.insert(StorageHelper.MARKETTABLE,null , contentValues);
        return insert_id;
    }
    public long insertPlace(int id , String name){
        SQLiteDatabase db =    helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(StorageHelper.place_id, id);
        contentValues.put(StorageHelper.place_name , name);
        long insert_id = db.insert(StorageHelper.PLACETABLE,null , contentValues);
        return insert_id;
    }
    public long insertCategory(int id , String name){
        SQLiteDatabase db =    helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(StorageHelper.category_id, id);
        contentValues.put(StorageHelper.category_name , name);
        long insert_id = db.insert(StorageHelper.CATEGORYTABLE,null , contentValues);
        return insert_id;
    }
    public String[][] getallmarket(){
        SQLiteDatabase db =helper.getWritableDatabase();


        Cursor cursor=db.query(StorageHelper.MARKETTABLE,null, null,null,null,null,null);

        String names[][]=new String[cursor.getCount()][8];
        int i=0;
        while(cursor.moveToNext()){
            int mid=cursor.getColumnIndex(StorageHelper.market_id);
            int mname=cursor.getColumnIndex(StorageHelper.market_name);
            int murl=cursor.getColumnIndex(StorageHelper.market_url);
            int mdetails=cursor.getColumnIndex(StorageHelper.market_details);
            int mother=cursor.getColumnIndex(StorageHelper.market_other);
            int mplace=cursor.getColumnIndex(StorageHelper.market_place_name);
            int mlong=cursor.getColumnIndex(StorageHelper.market_long);
            int mlatt=cursor.getColumnIndex(StorageHelper.market_latt);
            int id=cursor.getInt(mid);
            String name=cursor.getString(mname);
            String url=cursor.getString(murl);
            String details=cursor.getString(mdetails);
            String other=cursor.getString(mother);
            String place=cursor.getString(mplace);
            String getlong=cursor.getString(mlong);
            String getlatt=cursor.getString(mlatt);
            names[i][0]=""+id;
            names[i][1]=name;
            names[i][2]=url;
            names[i][3]=details;
            names[i][4]=other;
            names[i][5]=place;
            names[i][6]=getlong;
            names[i][7]=getlatt;
            i++;
        }
        return names;
    }

    public String[][] getallplaces() {
        SQLiteDatabase db = helper.getWritableDatabase();


        Cursor cursor = db.query(StorageHelper.PLACETABLE, null, null, null, null, null, null);

        String names[][] = new String[cursor.getCount()][2];
        int i = 0;
        while (cursor.moveToNext()) {
            int pid=cursor.getColumnIndex(StorageHelper.place_id);
            int pname=cursor.getColumnIndex(StorageHelper.place_name);
            int id=cursor.getInt(pid);
            String name=cursor.getString(pname);
            names[i][0]=""+id;
            names[i][1]=name;
            i++;

        }
        return  names;
    }
    public String[][] getallcategories() {
        SQLiteDatabase db = helper.getWritableDatabase();


        Cursor cursor = db.query(StorageHelper.CATEGORYTABLE, null, null, null, null, null, null);

        String names[][] = new String[cursor.getCount()][2];
        int i = 0;
        while (cursor.moveToNext()) {
            int cid=cursor.getColumnIndex(StorageHelper.category_id);
            int cname=cursor.getColumnIndex(StorageHelper.category_name);
            int id=cursor.getInt(cid);
            String name=cursor.getString(cname);
            names[i][0]=""+id;
            names[i][1]=name;
            i++;

        }
        return  names;
    }
    public String getMarketVersion(){
        SQLiteDatabase db =helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT MAX(" + StorageHelper.market_on_updates + ")  AS " + StorageHelper.market_on_updates + " FROM " + StorageHelper.MARKETTABLE, null);
        if(cursor.moveToNext())
        {
            int index1=cursor.getColumnIndex(StorageHelper.market_on_updates);
            String lastdate=cursor.getString(index1);
            return lastdate;
        }
        else
            return null;
    }
    public int deletemarket(int id){
        SQLiteDatabase db =    helper.getWritableDatabase();
        int count = db.delete(StorageHelper.MARKETTABLE,  StorageHelper.market_id + " = " + id, null);
        return count ;
    }
    public int deleteplace(int id){
        SQLiteDatabase db =    helper.getWritableDatabase();
        int count = db.delete(StorageHelper.PLACETABLE, StorageHelper.place_id + " = " + id, null);
        return count ;
    }
    public int deletecategory(int id){
        SQLiteDatabase db =    helper.getWritableDatabase();
        int count = db.delete(StorageHelper.CATEGORYTABLE,  StorageHelper.category_id + " = " + id, null);
        return count ;
    }

    public StorageDatabaseAdapter(Context context){

        helper = new StorageHelper(context);
    }






    static class StorageHelper extends SQLiteOpenHelper {
        private Context context;
        private static final String DATABASE_NAME = "marketDB";
        private static final String MARKETTABLE = "market";
        private static final String PLACETABLE = "place";
        private static final String CATEGORYTABLE = "category";
        private static final int DATABASE_VERSION =1;

        //market table
        private static final String market_id = "market_id";
        private static final String market_name = "market_name";
        private static final String market_url = "market_url";
        private static final String market_details = "market_details";
        private static final String market_other = "market_other";
        private static final String market_place_name = "market_place_name";
        private static final String market_long = "market_long";
        private static final String market_latt = "market_latt";
        private static final String market_on_updates = "market_on_updates";

        private static final String CREATE_TABLE_MARKET = "CREATE TABLE "+ MARKETTABLE +" (" + market_id + " INTEGER  ," + market_name +
                " VARCHAR(255), " + market_url + " VARCHAR(255)  , " + market_details + " VARCHAR(255) , " + market_other + " VARCHAR(255) , "+ market_place_name +" VARCHAR(255) ,"+ market_long+" VARCHAR(255),"+market_latt+" VARCHAR(255),"+market_on_updates+" TIMESTAMP,PRIMARY KEY ("+market_id+" ) );";
        private static final String DROP_TABLE_MARKET = "DROP TABLE " + MARKETTABLE + " IF EXISTS;";

        //place table
        private static final String place_id = "place_id";
        private static final String place_name = "place_name";

        private static final String CREATE_TABLE_PLACE = "CREATE TABLE "+ PLACETABLE +" (" + place_id + " INTEGER  ," + place_name +
                " VARCHAR(255) "+",PRIMARY KEY ("+place_id+" ) );";
        private static final String DROP_TABLE_PLACE = "DROP TABLE " + PLACETABLE + " IF EXISTS;";


        //category table
        private static final String category_id = "category_id";
        private static final String category_name = "category_name";

        private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "+ CATEGORYTABLE +" (" + category_id + " INTEGER  ," + category_name +
                " VARCHAR(255) " +",PRIMARY KEY ("+category_id+" ) );";
        private static final String DROP_TABLE_CATEGORY = "DROP TABLE " + CATEGORYTABLE + " IF EXISTS;";




        public StorageHelper(Context context) {


            // super ( context , database name , custom cursor , version number )
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
//            Log.i("query", CREATE_TABLE_MARKET);
//            Log.i("query", CREATE_TABLE_PLACE);
//            Log.i("query", CREATE_TABLE_CATEGORY);
//            Toast.makeText(context, "constructor is called", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onCreate(SQLiteDatabase db) {

            try {

                db.execSQL(CREATE_TABLE_MARKET);
                db.execSQL(CREATE_TABLE_PLACE);
                db.execSQL(CREATE_TABLE_CATEGORY);
//                Toast.makeText(context, "on create", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {

//                Toast.makeText(context, "on create exception", Toast.LENGTH_LONG).show();
            }

        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {

                db.execSQL(CREATE_TABLE_MARKET);
                db.execSQL(CREATE_TABLE_PLACE);
                db.execSQL(CREATE_TABLE_CATEGORY);
                onCreate(db);
//                Toast.makeText(context, "upgrade", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
//                Toast.makeText(context, " upgrade exception", Toast.LENGTH_LONG).show();
            }
        }
    }
}
