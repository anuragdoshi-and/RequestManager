package com.qrofeus.requestmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Base64;

public class LoginUser extends AppCompatActivity {

    private EditText username;
    private EditText password;

    private ArrayList<UserAccount> userAccounts;

    private String use;
    private String username_text;
    private String password_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_activity);

        use = getIntent().getStringExtra("use");

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        TextView usage = findViewById(R.id.login_use);
        usage.setText(String.format("%s Login", use));

        getUsers();
    }

    private void getUsers() {
        userAccounts = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts").child(use);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserAccount account = dataSnapshot.getValue(UserAccount.class);
                        if (account != null) {
                            String password = account.getPassword();
                            account.setPassword(caesarCipherDecrypt(password));
                            userAccounts.add(account);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginUser.this, "Error occurred: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginUser.this, "Database Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSubmitClick(View view) {
        username_text = username.getText().toString().trim();
        password_text = password.getText().toString().trim();

        if (username_text.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            username.requestFocus();
            return;
        }

        if (password_text.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return;
        }

        checkUser();
    }

    private void checkUser() {
        for (UserAccount account : userAccounts) {
            if (username_text.equals(account.getUsername())) {
                checkPassword(account);
                return;
            }
        }
        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        username.requestFocus();
    }

    private void checkPassword(UserAccount account) {
        if (password_text.equals(account.getPassword())) {
            String dataKey = account.getUser_id();
            String email_address = account.getMailID();
            String phone_number = account.getPhone_number();

            // Start Activity based on usage
            if (use.equals("Admin")) {
                startActivity(new Intent(LoginUser.this, Dashboard_Admin.class)
                        .putExtra("Database Key", dataKey)
                        .putExtra("Username", username_text)
                        .putExtra("Password", password_text)
                        .putExtra("Mail ID", email_address)
                        .putExtra("Phone", phone_number));
                finish();
            } else if (use.equals("Customer")) {
                startActivity(new Intent(LoginUser.this, Dashboard_User.class)
                        .putExtra("Database Key", dataKey)
                        .putExtra("Username", username_text)
                        .putExtra("Password", password_text)
                        .putExtra("Mail ID", email_address)
                        .putExtra("Phone", phone_number));
                finish();
            }
        }
    }

    public void onRegisterUser(View view) {
        startActivity(new Intent(this, RegisterAccount.class)
                .putExtra("use", "Customer"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Dashboard_Main.class));
        finish();
    }

    @SuppressLint("NewApi")
    public String caesarCipherDecrypt(String secret) {
        StringBuilder tmp = new StringBuilder();
        final int OFFSET = 4;
        for (int i = 0; i < secret.length(); i++) {
            tmp.append((char) (secret.charAt(i) - OFFSET));
        }

        String reversed = new StringBuffer(tmp.toString()).reverse().toString();
        return new String(Base64.getDecoder().decode(reversed));
    }
}