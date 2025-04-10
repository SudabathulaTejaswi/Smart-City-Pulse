package com.example.smart_city_pulse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smart_city_pulse.R;
import com.example.smart_city_pulse.utils.SessionManager;
import androidx.activity.OnBackPressedCallback;

public class OfficerDashboardActivity extends AppCompatActivity {
    private Button btnViewFeedback, btnLogout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_dashboard);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(OfficerDashboardActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Removes current activity from back stack
            }
        });

        btnViewFeedback = findViewById(R.id.btnViewFeedback);
        btnLogout = findViewById(R.id.btnLogout);

        sessionManager = new SessionManager(this);

        btnViewFeedback.setOnClickListener(v -> startActivity(new Intent(this, FeedbackListActivity.class)));

        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}