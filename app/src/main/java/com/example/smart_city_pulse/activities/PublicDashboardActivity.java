package com.example.smart_city_pulse.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smart_city_pulse.R;
import com.example.smart_city_pulse.database.DatabaseManager;
import androidx.activity.OnBackPressedCallback;


public class PublicDashboardActivity extends AppCompatActivity {
    private EditText feedbackInput;
    private Spinner feedbackCategorySpinner;
    private Button submitFeedbackBtn, emergencyNumbersBtn, developersBtn;
    private DatabaseManager databaseManager; // Changed variable type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_dashboard);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(PublicDashboardActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Removes current activity from back stack
            }
        });

        // Initialize UI Components
        feedbackInput = findViewById(R.id.feedbackInput);
        feedbackCategorySpinner = findViewById(R.id.feedbackCategorySpinner);
        submitFeedbackBtn = findViewById(R.id.submitFeedbackBtn);
        emergencyNumbersBtn = findViewById(R.id.btnEmergencyNumbers);
        developersBtn = findViewById(R.id.btnDevelopers);
        databaseManager = new DatabaseManager(this);

        // Set up Spinner (no change needed)
        String[] feedbackCategories = {
                "Water Supply", "Electricity", "Roads", "Cleanliness", "Healthcare",
                "Public Transport", "Prices", "Development", "Public Meetings", "Other"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, feedbackCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedbackCategorySpinner.setAdapter(adapter);

        submitFeedbackBtn.setOnClickListener(v -> submitFeedback());

        emergencyNumbersBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PublicDashboardActivity.this, EmergencyNumbersActivity.class);
            startActivity(intent);
        });

        developersBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PublicDashboardActivity.this, DevelopersActivity.class);
            startActivity(intent);
        });
    }

    private void submitFeedback() {
        String feedbackText = feedbackInput.getText().toString().trim();
        String category = feedbackCategorySpinner.getSelectedItem().toString();

        if (feedbackText.isEmpty()) {
            Toast.makeText(this, "Please enter feedback", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = databaseManager.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("feedback_text", feedbackText);
        values.put("category", category);

        long result = db.insert("feedback", null, values);
        db.close();

        if (result == -1) {
            Toast.makeText(this, "Failed to submit feedback.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Feedback Submitted Successfully!", Toast.LENGTH_LONG).show();
        }

        feedbackInput.setText("");
        feedbackCategorySpinner.setSelection(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}