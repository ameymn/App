package com.example.jeeevandan;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class NewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new); // Set your layout file here

        // Add your code here to know that you've landed on this page
        // For example, you can log a message to the console or show a toast
        // For logging:
//        Log.d(TAG, "Response body: " + "Landed on NewActivity");

        // For showing a toast:
        // Toast.makeText(this, "Landed on NewActivity", Toast.LENGTH_SHORT).show();
    }
}