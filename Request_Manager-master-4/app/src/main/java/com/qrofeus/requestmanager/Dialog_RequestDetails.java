package com.qrofeus.requestmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;

public class Dialog_RequestDetails extends AppCompatDialogFragment {

    private final RequestClass request;
    private final String user;

    private final int priorityVal;

    private String message;
    private Interface_requestDetails requestDetails;

    public Dialog_RequestDetails(RequestClass request, String user, int priorityVal) {
        this.request = request;
        this.user = user;
        this.priorityVal = priorityVal;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_request_details, null);

        final TextView reqID = view.findViewById(R.id.popup_id);
        final TextView reqSubject = view.findViewById(R.id.popup_subject);
        final TextView reqUsername = view.findViewById(R.id.popup_username);
        final TextView reqDetails = view.findViewById(R.id.popup_details);

        reqID.setText(String.format("RID: %s", request.getRequest_id().toUpperCase()));
        reqUsername.setText(String.format("Username: %s", request.getUsername()));
        reqSubject.setText(String.format("Subject: %s", request.getRequest_subject()));
        reqDetails.setText(String.format("Details:\n%s", request.getRequest_details()));

        if (user.equals("Admin")) {
            final Spinner priorities = view.findViewById(R.id.spinner_details);

            final Button priority = view.findViewById(R.id.changePriority);
            final Button dismiss = view.findViewById(R.id.popup_dismissButton);
            final Button complete = view.findViewById(R.id.popup_completeButton);
            final Button confirm = view.findViewById(R.id.popup_confirmButton);
            final Button cancel = view.findViewById(R.id.popup_cancelButton);

            final TextView phone = view.findViewById(R.id.popup_phone);
            final TextView mail = view.findViewById(R.id.popup_mail);

            final CardView reqAdminCard = view.findViewById(R.id.adminCard);
            reqAdminCard.setVisibility(View.VISIBLE);

            phone.setText(String.format("Phone: %s", request.getPhone()));
            mail.setText(String.format("Mail: %s", request.getEmail()));

            priorities.setSelection(priorityVal);

            //Admin functionality
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss.setText(R.string.confirm_dismissal);
                    complete.setEnabled(false);
                    priority.setEnabled(false);
                    confirm.setVisibility(View.VISIBLE);
                    confirm.setClickable(true);
                    cancel.setVisibility(View.VISIBLE);
                    cancel.setClickable(true);
                    message = "Dismiss";
                }
            });

            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    complete.setText(R.string.confirm_completion);
                    dismiss.setEnabled(false);
                    priority.setEnabled(false);
                    confirm.setVisibility(View.VISIBLE);
                    confirm.setClickable(true);
                    cancel.setVisibility(View.VISIBLE);
                    cancel.setClickable(true);
                    message = "Complete";
                }
            });

            priority.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (priorities.getSelectedItemPosition() == priorityVal) {
                        Toast.makeText(getActivity(), "no change in priority", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    complete.setEnabled(false);
                    dismiss.setEnabled(false);
                    confirm.setVisibility(View.VISIBLE);
                    confirm.setClickable(true);
                    cancel.setVisibility(View.VISIBLE);
                    cancel.setClickable(true);
                    message = "Priority";
                }
            });

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (message) {
                        case "Complete":
                            requestDetails.completeRequest(request.getRequest_id());
                            break;
                        case "Dismiss":
                            requestDetails.dismissRequest(request.getRequest_id());
                            break;
                        case "Priority":
                            requestDetails.changePriority(request.getRequest_id(), priorities.getSelectedItem().toString());
                            break;
                    }
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        //Set view to builder
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            requestDetails = (Interface_requestDetails) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Interface_requestDetails");
        }
    }

    public interface Interface_requestDetails {
        void dismissRequest(String requestID);

        void completeRequest(String requestID);

        void changePriority(String requestID, String targetPriority);
    }
}
