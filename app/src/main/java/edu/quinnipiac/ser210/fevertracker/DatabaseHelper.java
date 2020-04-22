package edu.quinnipiac.ser210.fevertracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "feverTracker"; //The name of the database
    private static final int DB_VERSION = 1; //The version of the database

    //Used to store the data as a string so that it can be shared easier
    private String storedTemp = "";
    private String storedDate = "";
    private String storedTime = "";
    private String storedFeeling = "";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    public void insertRecord(SQLiteDatabase db, String temp,
                                    String date, String time, String feeling) {
        ContentValues feverValues = new ContentValues();
        feverValues.put("TEMPERATURE", temp);
        feverValues.put("DATE", date);
        feverValues.put("TIME", time);
        feverValues.put("FEELING", feeling);
        db.insert("RECORD", null, feverValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE RECORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "TEMPERATURE TEXT, "
                    + "DATE TEXT, "
                    + "TIME TEXT, "
                    + "FEELING TEXT); ");
        }
        if (oldVersion < 2) {

        }
    }

    public List<String> displayReport(String id) {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT * FROM RECORD WHERE _id = " + id;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex("TEMPERATURE")));
                    list.add(cursor.getString(cursor.getColumnIndex("DATE")));
                    list.add(cursor.getString(cursor.getColumnIndex("TIME")));
                    list.add(cursor.getString(cursor.getColumnIndex("FEELING")));
                }
            }
            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return list;
    }

    public List<String> getAllReports() {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM RECORD";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex("TEMPERATURE")));
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return list;
    }

    public void setSelected(String temp, String date, String time, String feeling) {
        storedTemp = temp;
        storedDate = date;
        storedTime = time;
        storedFeeling = feeling;
    }

    public String getStoredTemp() {
        return storedTemp;
    }
    public String getStoredDate() {
        return storedDate;
    }
    public String getStoredTime() {
        return storedTime;
    }
    public String getStoredFeeling() {
        return storedFeeling;
    }
}