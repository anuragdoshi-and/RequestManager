package com.qrofeus.requestmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard_Main extends AppCompatActivity implements Dialog_Mail.InterfaceMail {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_main);
    }

    public void requestList(View view) {
        startActivity(new Intent(this, RequestQueue.class));

        //startActivity(new Intent(this, RequestList.class));
    }

    public void makeRequest(View view) {
        startActivity(new Intent(this, RegisterRequest.class)
                .putExtra("User", ""));
    }

    public void adminLogin(View view) {
        startActivity(new Intent(this, LoginUser.class)
                .putExtra("use", "Admin"));
        finish();
    }

    public void contactUs(View view) {
        Dialog_Mail mail = new Dialog_Mail();
        mail.show(getSupportFragmentManager(), "Contact Us");
    }

    public void registerUser(View view) {
        startActivity(new Intent(this, RegisterAccount.class)
                .putExtra("use", "Customer"));
    }

    public void userLogin(View view) {
        startActivity(new Intent(this, LoginUser.class)
                .putExtra("use", "Customer"));
        finish();
    }

    @Override
    public void sendMail(String mailSubject, String mailDetails) {
        // Send Mail to admin

        Uri data = Uri.parse("mailto:pday3683@gmail.com?subject=" + mailSubject + "&body=" + mailDetails);
        Intent intent = new Intent(Intent.ACTION_VIEW, data);
        startActivity(intent);
    }
}