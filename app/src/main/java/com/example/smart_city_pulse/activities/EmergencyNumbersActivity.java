package com.example.smart_city_pulse.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.smart_city_pulse.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmergencyNumbersActivity extends Activity {
    private ListView emergencyListView;
    private TextView headerText;
    private ArrayList<String> emergencyContacts;
    private ArrayList<String> phoneNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_numbers);

        emergencyListView = findViewById(R.id.emergencyListView);
        headerText = findViewById(R.id.headerText);

        // List of emergency contacts with numbers
        emergencyContacts = new ArrayList<>(Arrays.asList(
                "🚔 Police: 100",
                "🚒 Fire Brigade: 101",
                "🚑 Ambulance: 102",
                "🚨 Disaster Management: 1070",
                "👩‍💼 Women Helpline: 1091",
                "👨‍⚕️ Medical Emergency: 108",
                "👴 Senior Citizen Helpline: 14567",
                "📞 Child Helpline: 1098",
                "🚂 Railway Helpline: 139",
                "✈️ Air Ambulance: 9540161344",
                "🚔 Traffic Police: 103",
                "🔌 Electricity Helpline: 1912",
                "💧 Water Supply Helpline: 1916",
                "📱 Cyber Crime: 1930",
                "🌍 Earthquake/Flood Relief: 1078"
        ));

        phoneNumbers = extractPhoneNumbers(emergencyContacts);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, emergencyContacts);
        emergencyListView.setAdapter(adapter);

        // Handle item clicks to initiate calls
        emergencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phoneNumber = phoneNumbers.get(position);
                makeCall(phoneNumber);
            }
        });

        Toast.makeText(this, "Emergency numbers available offline!", Toast.LENGTH_LONG).show();
    }

    private ArrayList<String> extractPhoneNumbers(ArrayList<String> contacts) {
        ArrayList<String> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        for (String contact : contacts) {
            Matcher matcher = pattern.matcher(contact);
            if (matcher.find()) {
                numbers.add(matcher.group());
            }
        }
        return numbers;
    }

    private void makeCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
