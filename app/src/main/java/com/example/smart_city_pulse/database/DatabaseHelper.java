package com.example.smart_city_pulse.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smartcity.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_FEEDBACK = "feedback";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_COMPLAINTS = "complaints";

    // Feedback Table Columns
    private static final String COLUMN_FEEDBACK_ID = "id";
    private static final String COLUMN_FEEDBACK_TEXT = "feedback_text";
    private static final String COLUMN_FEEDBACK_CATEGORY = "category";

    // User Table Columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_PHONE = "phone";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_ROLE = "role";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Feedback Table
        String createFeedbackTable = "CREATE TABLE " + TABLE_FEEDBACK + " (" +
                COLUMN_FEEDBACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FEEDBACK_TEXT + " TEXT, " +
                COLUMN_FEEDBACK_CATEGORY + " TEXT)";
        db.execSQL(createFeedbackTable);

        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_PHONE + " TEXT UNIQUE, " +
                COLUMN_USER_PASSWORD + " TEXT, " +
                COLUMN_USER_ROLE + " TEXT)";
        db.execSQL(createUsersTable);

        String createComplaintsTable = "CREATE TABLE " + TABLE_COMPLAINTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "description TEXT, " +
                "status TEXT)";
        db.execSQL(createComplaintsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINTS);
        onCreate(db);
    }

    public boolean registerUser(String phone, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_PHONE + " = ?",
                new String[]{phone});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PHONE, phone);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUserLogin(String phone, String password) {
        return checkLogin(phone, password, null);
    }

    public boolean checkOfficerLogin(String phone, String password) {
        return checkLogin(phone, password, "officer");
    }

    private boolean checkLogin(String phone, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_PHONE + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] args;

        if (role != null) {
            query += " AND " + COLUMN_USER_ROLE + " = ?";
            args = new String[]{phone, password, role};
        } else {
            args = new String[]{phone, password};
        }

        Cursor cursor = db.rawQuery(query, args);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Cursor getUserDetails(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_PHONE + " = ?",
                new String[]{phone});
    }

    public boolean insertFeedback(String feedbackText, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FEEDBACK_TEXT, feedbackText);
        values.put(COLUMN_FEEDBACK_CATEGORY, category);

        long result = db.insert(TABLE_FEEDBACK, null, values);

        if (result == -1) {
            Log.e("Database", "Feedback insertion failed");
            return false;
        } else {
            Log.d("Database", "Feedback inserted successfully with ID: " + result);
            return true;
        }
    }

    public Cursor getAllComplaints() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_COMPLAINTS, null);
    }

    public Cursor getUser(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_PHONE + " = ?",
                new String[]{phone});
    }

    public Cursor getOfficer(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_PHONE + " = ? AND " + COLUMN_USER_ROLE + " = 'officer'",
                new String[]{phone});
    }
}
