package com.qrofeus.requestmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.regex.Pattern;

public class RegisterRequest extends AppCompatActivity {

    private String username_text;
    private String subject_text;
    private String email_address;
    private String phone_number;
    private String unique_id;

    private DatabaseReference reference;

    private Spinner priority;
    private EditText username;
    private EditText mail_id;
    private EditText phone;
    private EditText subject;
    private EditText details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register_request);

        username = findViewById(R.id.username);
        mail_id = findViewById(R.id.email_address);
        phone = findViewById(R.id.contact_phone);
        subject = findViewById(R.id.request_subject);
        details = findViewById(R.id.request_details);
        priority = findViewById(R.id.priority_dropdown);

        unique_id = hexCode();

        TextView request_id = findViewById(R.id.register_id);
        request_id.setText(String.format("Request ID: %s", unique_id.toUpperCase()));

        if (!getIntent().getStringExtra("User").isEmpty()) {
            username.setText(getIntent().getExtras().get("User").toString());
            mail_id.setText(getIntent().getExtras().get("Mail").toString());
            phone.setText(getIntent().getExtras().get("Phone").toString());
        }
    }

    public void onSubmitClick(View view) {
        // Validate Input
        email_address = mail_id.getText().toString();
        phone_number = phone.getText().toString();
        if (verifyMail(email_address) || verifyPhone(phone_number)) {
            username_text = username.getText().toString().trim();
            subject_text = subject.getText().toString();
            if (username_text.isEmpty() || subject_text.isEmpty()) {
                Toast.makeText(RegisterRequest.this, "Username/Subject field is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            storeData();
        }
    }

    private void storeData() {
        changeReference();

        // Generate Random Request ID
        String details_text = details.getText().toString();

        // Create data structure for storage
        RequestClass newRequest = new RequestClass(unique_id, username_text, subject_text, details_text, email_address, phone_number);
        reference.child(unique_id).setValue(newRequest);

        Toast.makeText(this, "Request Registered", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean verifyMail(String mail) {
        if (mail.isEmpty()) {
            mail_id.requestFocus();
            return false;
        }

        Pattern patternMail = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
        return patternMail.matcher(mail).matches();
    }

    private boolean verifyPhone(String phoneNo) {
        if (phoneNo.isEmpty()) {
            phone.requestFocus();
            return false;
        }
        Pattern patternPhone = Pattern.compile("[789][0-9]{9}");
        return patternPhone.matcher(phoneNo).matches();
    }

    private void changeReference() {
        String request_priority = priority.getSelectedItem().toString();
        // Create Database Reference
        switch (request_priority) {
            case "Low":
                reference = FirebaseDatabase.getInstance().getReference("Low");
                break;
            case "Medium":
                reference = FirebaseDatabase.getInstance().getReference("Medium");
                break;
            case "High":
                reference = FirebaseDatabase.getInstance().getReference("High");
                break;
            default:
                reference = FirebaseDatabase.getInstance().getReference();
        }
    }

    private String hexCode() {
        Random random = new Random();
        return Integer.toHexString(random.nextInt()).toLowerCase();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}