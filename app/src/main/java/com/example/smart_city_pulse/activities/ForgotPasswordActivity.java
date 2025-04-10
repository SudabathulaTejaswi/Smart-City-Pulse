package com.example.smart_city_pulse.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smart_city_pulse.R;
import com.example.smart_city_pulse.database.DatabaseManager;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText phoneNumber, otpInput, newPassword, confirmPassword;
    private Button actionButton;
    private ProgressDialog progressDialog;
    private int step = 1;
    private String phone;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        phoneNumber = findViewById(R.id.phoneNumber);
        otpInput = findViewById(R.id.otpInput);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        actionButton = findViewById(R.id.actionButton);

        databaseManager = new DatabaseManager(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        actionButton.setOnClickListener(v -> handleAction());
    }

    private void handleAction() {
        switch (step) {
            case 1:
                verifyPhoneNumber();
                break;
            case 2:
                verifyOtp();
                break;
            case 3:
                resetPassword();
                break;
        }
    }

    private void verifyPhoneNumber() {
        phone = phoneNumber.getText().toString().trim();
        if (!isValidPhoneNumber(phone)) {
            Toast.makeText(this, "Enter a valid 10-digit phone number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!databaseManager.isUserExists(phone)) {
            Toast.makeText(this, "Phone number not registered!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        phoneNumber.postDelayed(() -> {
            progressDialog.dismiss();
            Toast.makeText(ForgotPasswordActivity.this, "OTP sent to " + phone, Toast.LENGTH_SHORT).show();
            phoneNumber.setVisibility(View.GONE);
            otpInput.setVisibility(View.VISIBLE);
            actionButton.setText("Verify OTP");
            step = 2;
        }, 2000);
    }

    private void verifyOtp() {
        String otp = otpInput.getText().toString().trim();
        if (otp.length() != 6) {
            Toast.makeText(this, "Enter a valid 6-digit OTP!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        otpInput.postDelayed(() -> {
            progressDialog.dismiss();
            Toast.makeText(ForgotPasswordActivity.this, "OTP Verified!", Toast.LENGTH_SHORT).show();
            otpInput.setVisibility(View.GONE);
            newPassword.setVisibility(View.VISIBLE);
            confirmPassword.setVisibility(View.VISIBLE);
            actionButton.setText("Reset Password");
            step = 3;
        }, 2000);
    }

    private void resetPassword() {
        String password = newPassword.getText().toString().trim();
        String confirm = confirmPassword.getText().toString().trim();

        if (!isValidPassword(password, confirm)) return;

        progressDialog.show();
        confirmPassword.postDelayed(() -> {
            boolean updated = databaseManager.updatePassword(phone, password);
            progressDialog.dismiss();

            if (updated) {
                Toast.makeText(ForgotPasswordActivity.this, "Password Reset Successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Error resetting password!", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }

    private boolean isValidPhoneNumber(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches() && phone.length() == 10;
    }

    private boolean isValidPassword(String password, String confirm) {
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
