package com.example.smart_city_pulse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.smart_city_pulse.R;
import com.example.smart_city_pulse.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText phoneNumber, password, confirmPassword;
    private Button registerBtn, goToLoginBtn;
    private RadioGroup userRoleGroup;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        registerBtn = findViewById(R.id.registerBtn);
        goToLoginBtn = findViewById(R.id.goToLoginBtn);
        userRoleGroup = findViewById(R.id.userRoleGroup);

        databaseHelper = new DatabaseHelper(this);

        registerBtn.setOnClickListener(v -> registerUser());
        goToLoginBtn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String phone = phoneNumber.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        int selectedRoleId = userRoleGroup.getCheckedRadioButtonId();
        if (selectedRoleId == -1) {
            Toast.makeText(this, "Select a user role!", Toast.LENGTH_SHORT).show();
            return;
        }
        String role = ((RadioButton) findViewById(selectedRoleId)).getText().toString().toLowerCase();

        if (!validateInputs(phone, pass, confirmPass)) return;

        // Store password directly (no hashing)
        boolean isRegistered = databaseHelper.registerUser(phone, pass, role);
        if (isRegistered) {
            Toast.makeText(this, "Registration successful! Please log in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String phone, String pass, String confirmPass) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.PHONE.matcher(phone).matches() || phone.length() != 10) {
            Toast.makeText(this, "Enter a valid 10-digit phone number!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pass.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
