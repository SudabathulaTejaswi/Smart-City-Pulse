package com.example.smart_city_pulse.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SmartCityPulse.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_FEEDBACK = "feedback";
    private static final String TABLE_COMPLAINTS = "complaints";
    private static final String COLUMN_FEEDBACK_ID = "id";
    private static final String COLUMN_FEEDBACK_TEXT = "feedback_text";
    private static final String COLUMN_FEEDBACK_CATEGORY = "category";
    private static final String COLUMN_COMPLAINT_ID = "id";
    private static final String COLUMN_COMPLAINT_TEXT = "complaint_text";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Feedback Table
        String createFeedbackTable = "CREATE TABLE " + TABLE_FEEDBACK + " (" +
                COLUMN_FEEDBACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FEEDBACK_TEXT + " TEXT, " +
                COLUMN_FEEDBACK_CATEGORY + " TEXT)";

        // Create Complaints Table
        String createComplaintsTable = "CREATE TABLE " + TABLE_COMPLAINTS + " (" +
                COLUMN_COMPLAINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COMPLAINT_TEXT + " TEXT)";

        db.execSQL(createFeedbackTable);
        db.execSQL(createComplaintsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINTS);
        onCreate(db);
    }

    public List<String> getAllFeedback() {
        List<String> feedbackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_FEEDBACK_TEXT + " FROM " + TABLE_FEEDBACK, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                feedbackList.add(cursor.getString(0));
            }
            cursor.close();
        }
        db.close();
        return feedbackList;
    }

    public List<String> getAllComplaints() {
        List<String> complaints = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_COMPLAINT_TEXT + " FROM " + TABLE_COMPLAINTS, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                complaints.add(cursor.getString(0));
            }
            cursor.close();
        }
        return complaints;
    }

    public boolean isUserExists(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE phone = ?", new String[]{phone});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean updatePassword(String phone, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        int rows = db.update("users", values, "phone = ?", new String[]{phone});
        db.close();
        return rows > 0;
    }

    public List<String[]> getAllFeedbackWithCategory(SQLiteDatabase db) {
        List<String[]> feedbackList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT feedback_text, category FROM feedback", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String feedbackText = cursor.getString(0);
                String feedbackCategory = cursor.getString(1);
                feedbackList.add(new String[]{feedbackText, feedbackCategory});
                Log.d("FeedbackDebug", "Fetched Feedback: " + feedbackText + " | Category: " + feedbackCategory);
            }
            cursor.close();
        }
        return feedbackList;
    }
    public boolean checkUserCredentials(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("LoginDebug", "Checking login for phone: " + phone + " and password: " + password);

        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE phone=? AND password=?",
                new String[]{phone, password});

        if (cursor.moveToFirst()) {
            Log.d("LoginDebug", "User Found: " + phone);
            cursor.close();
            return true;
        } else {
            Log.d("LoginDebug", "Invalid credentials for: " + phone);
            cursor.close();
            return false;
        }
    }
}