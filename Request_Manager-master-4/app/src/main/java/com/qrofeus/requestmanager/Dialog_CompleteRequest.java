package com.qrofeus.requestmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog_CompleteRequest extends AppCompatDialogFragment {
    private final CompletedRequest request;

    public Dialog_CompleteRequest(CompletedRequest request) {
        this.request = request;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_completed_requests, null);

        final TextView id = view.findViewById(R.id.complete_id);
        final TextView username = view.findViewById(R.id.complete_username);
        final TextView email = view.findViewById(R.id.complete_email);
        final TextView phoneNo = view.findViewById(R.id.complete_phoneNo);
        final TextView priority = view.findViewById(R.id.complete_priority);
        final TextView subject = view.findViewById(R.id.complete_subject);
        final TextView details = view.findViewById(R.id.complete_details);
        final TextView message = view.findViewById(R.id.complete_message);

        id.setText(String.format("RID: %s", request.getReq_id().toUpperCase()));
        username.setText(String.format("Username: %s", request.getUsername()));
        email.setText(String.format("Email: %s", request.getEmail()));
        phoneNo.setText(String.format("Phone: %s", request.getPhoneNo()));
        priority.setText(String.format("Priority: %s", request.getPriority()));
        subject.setText(String.format("Subject: %s", request.getReq_subject()));
        details.setText(String.format("Details:\n%s", request.getReq_details()));
        message.setText(String.format("Status:\n%s", request.getMessage()));

        builder.setView(view);
        return builder.create();
    }

    // No reason to add interface if not implementing "Re-request functionality"
}
