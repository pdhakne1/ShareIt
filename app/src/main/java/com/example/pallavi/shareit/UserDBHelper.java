package com.example.pallavi.shareit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by pallavi on 2/5/16.
 */
public class UserDBHelper extends SQLiteOpenHelper{

    public static final String COLUMN_SERIAL = "SerialID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_PICTURE_PATH = "Picture";
    public static final String COLUMN_STATUS = "Status";

    public static final String DATABASE_TABLE = "User";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ShareIt";


    private static final String DATABASE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "  %s integer primary key autoincrement, " +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text)",
            DATABASE_TABLE, COLUMN_SERIAL, COLUMN_NAME, COLUMN_EMAIL, COLUMN_PASSWORD,COLUMN_PICTURE_PATH,COLUMN_STATUS);

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);

    }

    public void updateUser() {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = COLUMN_SERIAL + "= ?";
        String[] whereArgs = {"4"};

        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_NAME, "alpaca");
        newValues.put(COLUMN_EMAIL,"abc@gmail.com");
        newValues.put(COLUMN_PASSWORD, "changed");

        db.update(DATABASE_TABLE, newValues, whereClause, whereArgs);
    }

    public void getUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {COLUMN_SERIAL, COLUMN_NAME, COLUMN_EMAIL, COLUMN_PASSWORD,COLUMN_PICTURE_PATH,COLUMN_STATUS};
        Cursor cursor = db.query(DATABASE_TABLE, resultColumns, where, whereArgs, groupBy, having, order);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            String password = cursor.getString(3);
            String filepath = cursor.getString(4);
            String status = cursor.getString(5);
            Log.d("USER", String.format("%s,%s,%s,%s", id, name, email, password,filepath,status));
        }
    }

    public void insertUser(String name, String email, String password, String picturePath, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_NAME, name);
        newValues.put(COLUMN_EMAIL, email);
        newValues.put(COLUMN_PASSWORD, password);
        newValues.put(COLUMN_PICTURE_PATH, picturePath);
        newValues.put(COLUMN_STATUS, status);
        db.insert(DATABASE_TABLE, null, newValues);
    }

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_SERIAL + "=?";
        String[] whereArgs = {"6"};
        db.delete(DATABASE_TABLE, whereClause, whereArgs);
    }
}
