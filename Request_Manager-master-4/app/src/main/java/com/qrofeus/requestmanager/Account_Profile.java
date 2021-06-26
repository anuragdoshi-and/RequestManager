package com.qrofeus.requestmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Base64;
import java.util.regex.Pattern;

public class Account_Profile extends AppCompatActivity implements Dialog_Confirmation.Interface_DialogResults {

    private String username;
    private String password;
    private String mail;
    private String phone;
    private String use;

    private String dataKey;

    private EditText username_edit;
    private EditText password_edit;
    private EditText mail_edit;
    private EditText phone_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_profile);

        use = getIntent().getExtras().getString("use");

        dataKey = getIntent().getExtras().getString("Database Key");
        username = getIntent().getExtras().getString("Username");
        password = getIntent().getExtras().getString("Password");
        mail = getIntent().getExtras().getString("Mail ID");
        phone = getIntent().getExtras().getString("Phone");

        username_edit = findViewById(R.id.admin_user);
        password_edit = findViewById(R.id.admin_pass);
        mail_edit = findViewById(R.id.admin_mail);
        phone_edit = findViewById(R.id.admin_phone);

        username_edit.setText(username);
        password_edit.setText(password);
        mail_edit.setText(mail);
        phone_edit.setText(phone);
    }

    public void onUpdate(View view) {
        String newName = username_edit.getText().toString();
        String newPass = password_edit.getText().toString();
        String newMail = mail_edit.getText().toString();
        String newPhone = phone_edit.getText().toString();

        if (!newName.equals(username) || !newPass.equals(password) || !newMail.equals(mail) || !newPhone.equals(phone)) {
            if (verifyInput(newMail, newPhone)) {
                username = newName;
                password = newPass;
                mail = newMail;
                phone = newPhone;

                UserAccount account = new UserAccount(dataKey, username, caesarCipherEncrypt(password), mail, phone);
                FirebaseDatabase.getInstance().getReference("Accounts").child(use).child(dataKey).setValue(account);

                Toast.makeText(this, "User profile updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Invalid Format for Mail/Phone", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No Changes Applied", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verifyInput(String mail, String phone) {
        Pattern patternMail = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
        Pattern patternPhone = Pattern.compile("[789][0-9]{9}");

        return patternMail.matcher(mail).matches() && patternPhone.matcher(phone).matches();
    }

    public void onDelete(View view) {
        Dialog_Confirmation dialogClass = new Dialog_Confirmation("Confirm account deletion");
        dialogClass.show(getSupportFragmentManager(), "Confirm Delete");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (use.equals("Admin")) {
            startActivity(new Intent(this, Dashboard_Admin.class)
                    .putExtra("Username", username)
                    .putExtra("Database Key", dataKey)
                    .putExtra("Password", password)
                    .putExtra("Mail ID", mail)
                    .putExtra("Phone", phone));
            finish();
        } else if (use.equals("Customer")) {
            startActivity(new Intent(this, Dashboard_User.class)
                    .putExtra("Database Key", dataKey)
                    .putExtra("Username", username)
                    .putExtra("Password", password)
                    .putExtra("Mail ID", mail)
                    .putExtra("Phone", phone));
            finish();
        }
    }

    @Override
    public void confirmDialog() {
        FirebaseDatabase.getInstance().getReference("Accounts").child(use).child(dataKey).removeValue();
        Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Dashboard_Main.class));
        finish();
    }

    public String caesarCipherEncrypt(String plain) {
        String b64encoded = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            b64encoded = Base64.getEncoder().encodeToString(plain.getBytes());
        }

        // Reverse the string
        String reverse = null;
        if (b64encoded != null) {
            reverse = new StringBuffer(b64encoded).reverse().toString();
        }

        StringBuilder tmp = new StringBuilder();
        final int OFFSET = 4;
        if (reverse != null) {
            for (int i = 0; i < reverse.length(); i++) {
                tmp.append((char) (reverse.charAt(i) + OFFSET));
            }
        }
        return tmp.toString();
    }
}