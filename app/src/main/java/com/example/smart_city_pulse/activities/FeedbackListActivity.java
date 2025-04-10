package com.example.smart_city_pulse.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.smart_city_pulse.database.DatabaseManager;
import com.example.smart_city_pulse.R;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackListActivity extends Activity {
    private DatabaseManager databaseManager;
    private SQLiteDatabase db;
    private ListView feedbackListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);

        databaseManager = new DatabaseManager(this);
        db = databaseManager.getReadableDatabase();
        feedbackListView = findViewById(R.id.feedbackListView);

        loadFeedback();
    }

    private void loadFeedback() {
        List<Map<String, String>> feedbackList = new ArrayList<>();
        List<String[]> feedbackData = databaseManager.getAllFeedbackWithCategory(db);
        Log.d("FeedbackData", "Feedback Data Size: " + feedbackData.size());

        if (feedbackData != null) {
            for (String[] feedback : feedbackData) {
                if (feedback.length >= 2) {
                    Log.d("FeedbackData", "Feedback: " + feedback[0] + ", Category: " + feedback[1]);
                    Map<String, String> map = new HashMap<>();
                    map.put("feedback", feedback[0]);
                    map.put("category", feedback[1]);
                    feedbackList.add(map);
                }
            }
        }

        if (feedbackList.isEmpty()) {
            Log.d("FeedbackData", "No feedback found!");
        }

        if (!feedbackList.isEmpty()) {
            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    feedbackList,
                    R.layout.feedback_list_item,
                    new String[]{"feedback", "category"},
                    new int[]{R.id.feedbackText, R.id.feedbackCategory}
            );
            feedbackListView.setAdapter(adapter);
        } else {
            Log.d("FeedbackData", "No feedback data to display");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
            Log.d("Database", "Database closed in FeedbackListActivity");
        }
    }
}