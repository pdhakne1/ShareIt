package com.example.pallavi.shareit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roopashree on 2/17/2016.
 */
public class AndroidDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "shareIt";
    public static final int DB_VERSION = 1;

    public static final String DATABASE_TABLE_USER = "user_info";
    public static final String COLUMN_NAME_USER_ID = "user_id_column";
    public static final String COLUMN_NAME_FIRST_NAME = " user_firstName_column";
    public static final String COLUMN_NAME_LAST_NAME = " user_lastName_column";
    public static final String COLUMN_NAME_USER_EMAIL = " user_email_column";
    public static final String COLUMN_NAME_USER_PASSWORD = " user_password_column";
    public static final String COLUMN_NAME_USER_STATUS = " user_status_column";
    public static final String COLUMN_NAME_USER_DP = " user_dp_column";

   // Moment DB details
    public static final String COLUMN_SERIAL = "MomentID";
    public static final String COLUMN_MOMENT_NAME = "MomentName";
    public static final String COLUMN_MOMENT_TYPE = "MomentType";
    public static final String COLUMN_MOMENT_PATH = "MomentPath";
    public static final String COLUMN_MOMENT_OWNERSHIP = "MomentOwnership";
    public static final String COLUMN_USER_ID = "UserId";
    public static final String COLUMN_FRIEND_ID = "FriendId";
    public static final String COLUMN_MOMENT_LATITUDE = "MomentLatitude";
    public static final String COLUMN_MOMENT_LONGITUDE = "MomentLongitude";

    public static final String DATABASE_TABLE_MOMENTS = "Moments";

    private static final String DATABASE_CREATE_MOMENT = String.format(
            "CREATE TABLE %s (" +
                    "  %s integer primary key, " +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text)",
            DATABASE_TABLE_MOMENTS, COLUMN_SERIAL, COLUMN_MOMENT_NAME, COLUMN_MOMENT_TYPE, COLUMN_MOMENT_PATH,COLUMN_MOMENT_OWNERSHIP,COLUMN_USER_ID,COLUMN_FRIEND_ID,COLUMN_MOMENT_LATITUDE,COLUMN_MOMENT_LONGITUDE);

    private static final String DATABASE_CREATE_USER = String.format(
            "CREATE TABLE %s (" +
                    "  %s integer primary key autoincrement, " +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text)",
            DATABASE_TABLE_USER, COLUMN_NAME_USER_ID, COLUMN_NAME_FIRST_NAME, COLUMN_NAME_LAST_NAME, COLUMN_NAME_USER_EMAIL,
            COLUMN_NAME_USER_PASSWORD,COLUMN_NAME_USER_DP,COLUMN_NAME_USER_STATUS);

    public AndroidDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

       /* String sqlQueryToCreatePhotoDetailsTable = "create table if not exists " + TABLE_NAME_USER  +
                " (" + COLUMN_NAME_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME_FIRST_NAME + " text not null, "
                + COLUMN_NAME_LAST_NAME + " text not null, "
                + COLUMN_NAME_USER_EMAIL + " text not null, "
                + COLUMN_NAME_USER_PASSWORD + " text not null,"
                + COLUMN_NAME_USER_DP + " text,"
                + COLUMN_NAME_USER_STATUS + " text); ";*/

        //db.execSQL(sqlQueryToCreatePhotoDetailsTable);
        db.execSQL(DATABASE_CREATE_MOMENT);
        db.execSQL(DATABASE_CREATE_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_MOMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_USER);
        onCreate(db);

    }

    public boolean checkUserExists(String userEmail)
    {
        User myUserDetails = new User();
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_NAME_USER_EMAIL+"=?";
        String[] whereArgs = {userEmail};
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {COLUMN_NAME_USER_ID, COLUMN_NAME_FIRST_NAME, COLUMN_NAME_LAST_NAME, COLUMN_NAME_USER_EMAIL,COLUMN_NAME_USER_DP,COLUMN_NAME_USER_STATUS};
        Cursor cursor = db.query(DATABASE_TABLE_USER, resultColumns, where, whereArgs, groupBy, having, order);

        if(cursor==null || cursor.getCount()==0)
        {

            cursor.close();
            db.close();
            return false;
        }

        cursor.close();
        db.close();
        return true;
    }

    public User checkValidUser(String userEmail, String psswd)
    {
        User myUserDetails = new User();
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_NAME_USER_EMAIL+"=? and "+ COLUMN_NAME_USER_PASSWORD+"=?";
        String[] whereArgs = {userEmail,psswd};
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {COLUMN_NAME_USER_ID, COLUMN_NAME_FIRST_NAME, COLUMN_NAME_LAST_NAME, COLUMN_NAME_USER_EMAIL,COLUMN_NAME_USER_DP,COLUMN_NAME_USER_STATUS};
        Cursor cursor = db.query(DATABASE_TABLE_USER, resultColumns, where, whereArgs, groupBy, having, order);

        if(cursor==null || cursor.getCount()==0)
        {
            cursor.close();
            return null;
        }
        while (cursor.moveToNext()) {

            myUserDetails.setUserID(cursor.getString(0));
            myUserDetails.setFirstName(cursor.getString(1));
            myUserDetails.setLastName(cursor.getString(2));
            myUserDetails.setUserEmail(cursor.getString(3));
            myUserDetails.setDp(cursor.getString(4));
            myUserDetails.setStatus(cursor.getString(5));
            //Log.d("USER", String.format("%s,%s,%s,%s", id, name, email, password, filepath, status));
        }
        db.close();
        return myUserDetails;
    }

    public void updateUser() {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = COLUMN_SERIAL + "= ?";
        String[] whereArgs = {"4"};

        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_MOMENT_NAME, "alpaca");
        newValues.put(COLUMN_MOMENT_TYPE,"abc@gmail.com");
        newValues.put(COLUMN_MOMENT_PATH, "changed");

        db.update(DATABASE_TABLE_MOMENTS, newValues, whereClause, whereArgs);
    }

    public List<Moment> getMoment() {

        final List<Moment> momentList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_MOMENT_OWNERSHIP+"=?";;
        String[] whereArgs = {"Private"};
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {COLUMN_SERIAL, COLUMN_MOMENT_NAME, COLUMN_MOMENT_TYPE, COLUMN_MOMENT_PATH,COLUMN_MOMENT_OWNERSHIP,COLUMN_USER_ID,COLUMN_MOMENT_LATITUDE,COLUMN_MOMENT_LONGITUDE};
        Cursor cursor = db.query(DATABASE_TABLE_MOMENTS, resultColumns, where, whereArgs, groupBy, having, order);


        while (cursor.moveToNext()) {
            Moment myMoment = new Moment();

            myMoment.setMomentID(cursor.getInt(0));
            myMoment.setMomentName(cursor.getString(1));
            myMoment.setMomentType(cursor.getString(2));
            myMoment.setMomentPath(cursor.getString(3));
            myMoment.setMomentOwnership(cursor.getString(4));
            myMoment.setUserId(cursor.getString(5));
            myMoment.setMomentLatidude(cursor.getString(6));
            myMoment.setMomentLongitude(cursor.getString(7));
            momentList.add(myMoment);
            //Log.d("USER", String.format("%s,%s,%s,%s", id, name, email, password, filepath, status));
        }
        db.close();
        return momentList;
    }

    public Moment getMomentDetailsForMomentId(String momentId)
    {
        Moment myMoment=new Moment();
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_SERIAL+"=?";
        String[] whereArgs = {momentId};
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {COLUMN_SERIAL, COLUMN_MOMENT_NAME, COLUMN_MOMENT_TYPE, COLUMN_MOMENT_PATH,COLUMN_MOMENT_OWNERSHIP,COLUMN_USER_ID,COLUMN_MOMENT_LATITUDE,COLUMN_MOMENT_LONGITUDE};
        Cursor cursor = db.query(DATABASE_TABLE_MOMENTS, resultColumns, where, whereArgs, groupBy, having, order);


        while (cursor.moveToNext()) {


            myMoment.setMomentID(cursor.getInt(0));
            myMoment.setMomentName(cursor.getString(1));
            myMoment.setMomentType(cursor.getString(2));
            myMoment.setMomentPath(cursor.getString(3));
            myMoment.setMomentOwnership(cursor.getString(4));
            myMoment.setUserId(cursor.getString(5));
            myMoment.setMomentLatidude(cursor.getString(6));
            myMoment.setMomentLongitude(cursor.getString(7));
            //momentList.add(myMoment);
            //Log.d("USER", String.format("%s,%s,%s,%s", id, name, email, password, filepath, status));
        }
        db.close();
        return myMoment;
    }

    public void insertMoment(String momentName, String momentType,int momentId, String momentPath, String momentOwnership, String momentCreater,String friendID,String momentLatitude, String momentLongitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_SERIAL,momentId);
        newValues.put(COLUMN_MOMENT_NAME, momentName);
        newValues.put(COLUMN_MOMENT_TYPE, momentType);
        newValues.put(COLUMN_MOMENT_PATH, momentPath);
        newValues.put(COLUMN_MOMENT_OWNERSHIP, momentOwnership);
        newValues.put(COLUMN_USER_ID, momentCreater);
        newValues.put(COLUMN_FRIEND_ID, friendID);
        newValues.put(COLUMN_MOMENT_LATITUDE, momentLatitude);
        newValues.put(COLUMN_MOMENT_LONGITUDE, momentLongitude);
        db.insert(DATABASE_TABLE_MOMENTS, null, newValues);
        db.close();
    }

    public void deleteMoment() {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_SERIAL + "=?";
        String[] whereArgs = {"6"};
        db.delete(DATABASE_TABLE_MOMENTS, whereClause, whereArgs);
    }

    public Moment getRandomMoment()
    {
        Moment randomMoment = new Moment();
        SQLiteDatabase db = this.getWritableDatabase();
        String where = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {COLUMN_SERIAL, COLUMN_MOMENT_NAME, COLUMN_MOMENT_TYPE, COLUMN_MOMENT_PATH,COLUMN_MOMENT_OWNERSHIP,COLUMN_USER_ID,COLUMN_MOMENT_LATITUDE,COLUMN_MOMENT_LONGITUDE};
        Cursor cursor = db.query(DATABASE_TABLE_MOMENTS, resultColumns, where, whereArgs, groupBy, having, "RANDOM()","1");

        if(cursor==null || cursor.getCount()==0)
        {
            cursor.close();
            return null;
        }

        while (cursor.moveToNext()) {

            randomMoment.setMomentID(cursor.getInt(0));
            randomMoment.setMomentName(cursor.getString(1));
            randomMoment.setMomentType(cursor.getString(2));
            randomMoment.setMomentPath(cursor.getString(3));
            randomMoment.setMomentOwnership(cursor.getString(4));
            randomMoment.setUserId(cursor.getString(5));
            randomMoment.setMomentLatidude(cursor.getString(6));
            randomMoment.setMomentLongitude(cursor.getString(7));
            //Log.d("USER", String.format("%s,%s,%s,%s", id, name, email, password, filepath, status));
        }
        db.close();
        return randomMoment;
    }

    public ArrayList<Moment> getSharedMoments()
    {
        final ArrayList<Moment> momentList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_MOMENT_OWNERSHIP+"=?";
        String[] whereArgs = {"Shared"};
        String groupBy = null;
        String having = null;
        String order = COLUMN_SERIAL+" DESC";
        String[] resultColumns = {COLUMN_SERIAL, COLUMN_MOMENT_NAME, COLUMN_MOMENT_TYPE, COLUMN_MOMENT_PATH,COLUMN_MOMENT_OWNERSHIP,COLUMN_USER_ID,COLUMN_MOMENT_LATITUDE,COLUMN_MOMENT_LONGITUDE};
        Cursor cursor = db.query(DATABASE_TABLE_MOMENTS, resultColumns, where, whereArgs, groupBy, having, order);


        while (cursor.moveToNext()) {
            Moment myMoment = new Moment();

            myMoment.setMomentID(cursor.getInt(0));
            myMoment.setMomentName(cursor.getString(1));
            myMoment.setMomentType(cursor.getString(2));
            myMoment.setMomentPath(cursor.getString(3));
            myMoment.setMomentOwnership(cursor.getString(4));
            myMoment.setUserId(cursor.getString(5));
            myMoment.setMomentLatidude(cursor.getString(6));
            myMoment.setMomentLongitude(cursor.getString(7));
            momentList.add(myMoment);
            //Log.d("USER", String.format("%s,%s,%s,%s", id, name, email, password, filepath, status));
        }
        db.close();
        return momentList;
    }

}