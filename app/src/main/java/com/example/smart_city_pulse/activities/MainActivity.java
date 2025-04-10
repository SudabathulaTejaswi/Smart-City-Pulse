package com.example.smart_city_pulse.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart_city_pulse.R;
import com.example.smart_city_pulse.database.DatabaseHelper;

public class MainActivity extends Activity {
    private EditText phoneNumber, password;
    private Button loginBtn, officerLoginBtn, registerBtn;
    private CheckBox rememberMe;
    private TextView forgotPassword;
    private ImageButton showPassword;
    private boolean isPasswordVisible = false;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        officerLoginBtn = findViewById(R.id.officerLoginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        rememberMe = findViewById(R.id.rememberMe);
        forgotPassword = findViewById(R.id.forgotPassword);
        showPassword = findViewById(R.id.showPassword);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("SmartCityPrefs", MODE_PRIVATE);

        phoneNumber.setText(sharedPreferences.getString("savedPhone", ""));
        rememberMe.setChecked(sharedPreferences.getBoolean("rememberMeChecked", false));

        showPassword.setOnClickListener(v -> togglePasswordVisibility());
        loginBtn.setOnClickListener(v -> handlePublicLogin());
        officerLoginBtn.setOnClickListener(v -> handleOfficerLogin());
        registerBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
        forgotPassword.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class)));
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            showPassword.setImageResource(R.drawable.ic_visibility_off);
        } else {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            showPassword.setImageResource(R.drawable.ic_visibility);
        }
        isPasswordVisible = !isPasswordVisible;
        password.setSelection(password.getText().length());
    }

    private void handlePublicLogin() {
        String phone = phoneNumber.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (validateInputs(phone, pass)) {
            if (phone.equals("8888888888") && pass.equals("Chai@123")) {
                saveLoginDetails(phone);
                Toast.makeText(this, "Test User Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, PublicDashboardActivity.class));
                finish();
                return;
            }

            Cursor cursor = dbHelper.getUser(phone);
            if (cursor != null && cursor.moveToFirst()) {
                String storedPassword = cursor.getString(cursor.getColumnIndex("password"));
                if (storedPassword.equals(pass)) {
                    saveLoginDetails(phone);
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, PublicDashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "User Not Found!", Toast.LENGTH_SHORT).show();
            }

            if (cursor != null) cursor.close();
        }
    }

    private void handleOfficerLogin() {
        String phone = phoneNumber.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (validateInputs(phone, pass)) {
            if (phone.equals("9999999999") && pass.equals("Chai@123")) {
                saveLoginDetails(phone);
                Toast.makeText(this, "Test Officer Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, OfficerDashboardActivity.class));
                finish();
                return;
            }

            Cursor cursor = dbHelper.getOfficer(phone);
            if (cursor != null && cursor.moveToFirst()) {
                String storedPassword = cursor.getString(cursor.getColumnIndex("password"));
                if (storedPassword.equals(pass)) {
                    saveLoginDetails(phone);
                    Toast.makeText(this, "Officer Login Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, OfficerDashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid Officer Credentials!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Officer Not Found!", Toast.LENGTH_SHORT).show();
            }

            if (cursor != null) cursor.close();
        }
    }

    private boolean validateInputs(String phone, String pass) {
        if (phone.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter all details!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.length() != 10 || !phone.matches("\\d+")) {
            Toast.makeText(this, "Enter a valid 10-digit phone number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveLoginDetails(String phone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (rememberMe.isChecked()) {
            editor.putString("savedPhone", phone);
            editor.putBoolean("rememberMeChecked", true);
        } else {
            editor.clear();
        }
        editor.apply();
    }
}