package com.qrofeus.requestmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard_Admin extends AppCompatActivity implements Dialog_Confirmation.Interface_DialogResults {

    private String username;
    private String password;
    private String mail;
    private String phone;
    private String dataKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_admin);

        // Get User Details
        dataKey = getIntent().getExtras().getString("Database Key");
        username = getIntent().getExtras().getString("Username");
        password = getIntent().getExtras().getString("Password");
        mail = getIntent().getExtras().getString("Mail ID");
        phone = getIntent().getExtras().getString("Phone");

        // Set page title
        TextView title = findViewById(R.id.username_text);
        title.setText(username);
    }

    public void registerAdmin(View view) {
        startActivity(new Intent(this, RegisterAccount.class)
                .putExtra("use", "Admin"));
    }

    public void profile(View view) {
        startActivity(new Intent(this, Account_Profile.class)
                .putExtra("use", "Admin")
                .putExtra("Database Key", dataKey)
                .putExtra("Username", username)
                .putExtra("Password", password)
                .putExtra("Mail ID", mail)
                .putExtra("Phone", phone));
        finish();
    }

    public void requestQueue(View view) {
        startActivity(new Intent(this, Account_Request_Queue.class)
                .putExtra("use", "Admin")
                .putExtra("username", username));
    }

    @Override
    public void onBackPressed() {
        Dialog_Confirmation dialogClass = new Dialog_Confirmation("Confirm Logout");
        dialogClass.show(getSupportFragmentManager(), "Confirm Logout");
    }

    public void logout(View view) {
        onBackPressed();
    }

    @Override
    public void confirmDialog() {
        startActivity(new Intent(this, Dashboard_Main.class));
        finish();
    }
}