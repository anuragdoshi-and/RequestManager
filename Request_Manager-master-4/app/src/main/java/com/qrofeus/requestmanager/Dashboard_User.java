package com.qrofeus.requestmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard_User extends AppCompatActivity implements Dialog_Confirmation.Interface_DialogResults {

    private String username;
    private String password;
    private String mail;
    private String phone;
    private String dataKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_user);

        // Get User Details
        dataKey = getIntent().getExtras().getString("Database Key");
        username = getIntent().getExtras().getString("Username");
        password = getIntent().getExtras().getString("Password");
        mail = getIntent().getExtras().getString("Mail ID");
        phone = getIntent().getExtras().getString("Phone");

        // Set page title
        TextView title = findViewById(R.id.user_title);
        title.setText(username);
    }

    public void displayQueue(View view) {
        startActivity(new Intent(this, RequestQueue.class));
    }

    public void userProfile(View view) {
        startActivity(new Intent(this, Account_Profile.class)
                .putExtra("use", "Customer")
                .putExtra("Database Key", dataKey)
                .putExtra("Username", username)
                .putExtra("Password", password)
                .putExtra("Mail ID", mail)
                .putExtra("Phone", phone));
        finish();
    }

    public void makeRequest(View view) {
        startActivity(new Intent(this, RegisterRequest.class)
                .putExtra("User", username)
                .putExtra("Mail", mail)
                .putExtra("Phone", phone));
    }

    public void prevRequests(View view) {
        startActivity(new Intent(this, Account_Request_Queue.class)
                .putExtra("use", "Customer")
                .putExtra("username", username));
    }

    @Override
    public void onBackPressed() {
        Dialog_Confirmation dialogClass = new Dialog_Confirmation("Confirm Logout");
        dialogClass.show(getSupportFragmentManager(), "Confirm Logout");
    }

    @Override
    public void confirmDialog() {
        startActivity(new Intent(this, Dashboard_Main.class));
        finish();
    }
}