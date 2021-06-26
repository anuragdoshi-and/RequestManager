package com.qrofeus.requestmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog_Mail extends AppCompatDialogFragment {
    private InterfaceMail entryInterface;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_mail, null);

        final EditText editText1 = view.findViewById(R.id.login_edit1);
        final EditText editText2 = view.findViewById(R.id.login_edit2);
        final Button posButton = view.findViewById(R.id.login_button);

        posButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userSubject = editText1.getText().toString();
                final String passDetails = editText2.getText().toString();

                entryInterface.sendMail(userSubject, passDetails);
            }
        });

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            entryInterface = (InterfaceMail) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Class must implement Interface_UserEntry");
        }
    }

    public interface InterfaceMail {
        void sendMail(String mailSubject, String mailDetails);
    }
}
