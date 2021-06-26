package com.qrofeus.requestmanager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Account_Request_Queue extends AppCompatActivity implements Dialog_RequestDetails.Interface_requestDetails {

    private Dialog_RequestDetails requestDetails;
    private Dialog_CompleteRequest dialog;
    private DatabaseReference reference;

    private ArrayList<RequestClass> pendingDisplay;
    private ArrayList<RequestClass> searchPending = null;

    private ArrayList<CompletedRequest> completeDisplay;
    private ArrayList<CompletedRequest> searchComplete = null;

    private RequestAdapterPending adapterPending = null;
    private RequestAdapterComplete adapterComplete;

    private RecyclerView recycler;
    private CardView emptyCard;
    private Spinner spinner;
    private EditText searchBar;
    private TextView textView;
    private Button pending;
    private Button complete;

    private String use;
    private String username;
    private String priority;
    private String currentUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_request_queue);

        use = getIntent().getStringExtra("use");
        username = getIntent().getStringExtra("username");

        spinner = findViewById(R.id.account_spinner);
        searchBar = findViewById(R.id.account_search);
        textView = findViewById(R.id.account_text);
        pending = findViewById(R.id.button_pending);
        complete = findViewById(R.id.button_completed);
        recycler = findViewById(R.id.account_recycle);
        emptyCard = findViewById(R.id.account_emptyCard);

        priority = spinner.getSelectedItem().toString();

        setUp();
    }

    private void setUp() {
        currentUse = "Pending";

        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        pendingView();
    }

    // for pending requests
    private void pendingView() {
        pending.setEnabled(false);
        complete.setEnabled(true);

        textView.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        getPendingRequests();
    }

    private void getPendingRequests() {
        pendingDisplay = new ArrayList<>();
        priority = spinner.getSelectedItem().toString();
        reference = FirebaseDatabase.getInstance().getReference(priority);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        RequestClass request = dataSnapshot.getValue(RequestClass.class);
                        if (use.equals("Customer") && request != null)
                            if (!request.getUsername().equals(username))
                                continue;
                        pendingDisplay.add(request);
                    }
                    if (adapterPending == null)
                        addAdapterPending();
                    else
                        adapterPending.updateList(pendingDisplay);
                    searchBar.setText("");
                    if (!pendingDisplay.isEmpty())
                        emptyCard.setVisibility(View.GONE);
                    else
                        emptyCard.setVisibility(View.VISIBLE);
                } else
                    emptyCard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Account_Request_Queue.this, "Database Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAdapterPending() {
        adapterPending = new RequestAdapterPending(pendingDisplay);
        recycler.setAdapter(adapterPending);

        adapterPending.setOnItemClickListener(new RequestAdapterPending.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                if (searchPending != null)
                    requestDetails = new Dialog_RequestDetails(searchPending.get(position), use, spinner.getSelectedItemPosition());
                else
                    requestDetails = new Dialog_RequestDetails(pendingDisplay.get(position), use, spinner.getSelectedItemPosition());
                requestDetails.show(getSupportFragmentManager(), "Request Details");
            }
        });

        setUpPriorities();
        setUpSearchBar();
    }

    // Will only be used in case of pending requests
    private void setUpPriorities() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPendingRequests();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // for completed requests
    private void completedView() {
        complete.setEnabled(false);
        pending.setEnabled(true);

        spinner.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        getCompletedRequests();
    }

    private void getCompletedRequests() {
        completeDisplay = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Completed Requests");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CompletedRequest request = dataSnapshot.getValue(CompletedRequest.class);
                        if (use.equals("Customer") && request != null)
                            if (!request.getUsername().equals(username))
                                continue;
                        completeDisplay.add(request);
                    }
                    if (!completeDisplay.isEmpty())
                        emptyCard.setVisibility(View.GONE);
                    else
                        emptyCard.setVisibility(View.VISIBLE);
                    addAdapterComplete();
                } else
                    emptyCard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addAdapterComplete() {
        adapterComplete = new RequestAdapterComplete(completeDisplay);
        recycler.setAdapter(adapterComplete);

        adapterComplete.setOnItemClickListener(new RequestAdapterComplete.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (searchComplete != null)
                    dialog = new Dialog_CompleteRequest(searchComplete.get(position));
                else
                    dialog = new Dialog_CompleteRequest(completeDisplay.get(position));
                dialog.show(getSupportFragmentManager(), "Completed Request Details");
            }
        });

        setUpSearchBar();
    }

    // Common for Completed and Pending as well
    private void setUpSearchBar() {
        searchBar.setText("");
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchQuery(s.toString());
            }
        });
    }

    private void searchQuery(String text) {
        emptyCard.setVisibility(View.GONE);
        if (currentUse.equals("Completed")) {
            // Search in completed requests
            searchComplete = new ArrayList<>();
            for (CompletedRequest request : completeDisplay) {
                if (request.getReq_id().toLowerCase().contains(text.toLowerCase())) {
                    searchComplete.add(request);
                }
            }
            if (searchComplete.isEmpty())
                emptyCard.setVisibility(View.VISIBLE);
            adapterComplete.updateList(searchComplete);
        } else if (currentUse.equals("Pending")) {
            // Search in pending requests
            searchPending = new ArrayList<>();
            for (RequestClass request : pendingDisplay) {
                if (request.getRequest_id().toLowerCase().contains(text.toLowerCase())) {
                    searchPending.add(request);
                }
            }
            if (searchPending.isEmpty())
                emptyCard.setVisibility(View.VISIBLE);
            adapterPending.updateList(searchPending);
        }
    }

    // on Button Clicks
    public void onCLickPending(View view) {
        adapterComplete = null;
        currentUse = "Pending";
        pendingView();
    }

    public void onClickComplete(View view) {
        adapterPending = null;
        currentUse = "Completed";
        completedView();
    }

    // Will only be used in case of pending requests by admin
    @Override
    public void dismissRequest(String requestID) {
        // Remove request from database
        reference = FirebaseDatabase.getInstance().getReference(priority);
        reference.child(requestID).removeValue();
        int pos;
        for (pos = 0; pos < pendingDisplay.size(); pos++) {
            if (pendingDisplay.get(pos).getRequest_id().equals(requestID)) {
                break;
            }
        }
        completeMessage(pendingDisplay.get(pos), "dismissed");

        // Remove request from current display
        requestDetails.dismiss();
        pendingDisplay.remove(pos);
        adapterPending.updateList(pendingDisplay);
    }

    @Override
    public void completeRequest(String requestID) {
        // Remove request from list
        reference = FirebaseDatabase.getInstance().getReference(priority);
        reference.child(requestID).removeValue();
        int pos;
        for (pos = 0; pos < pendingDisplay.size(); pos++) {
            if (pendingDisplay.get(pos).getRequest_id().equals(requestID)) {
                break;
            }
        }
        completeMessage(pendingDisplay.get(pos), "completed");
        requestDetails.dismiss();
        pendingDisplay.remove(pos);
        adapterPending.updateList(pendingDisplay);
    }

    @Override
    public void changePriority(String requestID, String targetPriority) {
        int pos;
        for (pos = 0; pos < pendingDisplay.size(); pos++) {
            if (pendingDisplay.get(pos).getRequest_id().equals(requestID)) {
                break;
            }
        }
        // Add to target priority
        reference = FirebaseDatabase.getInstance().getReference(targetPriority);
        reference.child(requestID).setValue(pendingDisplay.get(pos));

        // Remove from current priority
        reference = FirebaseDatabase.getInstance().getReference(priority);
        reference.child(requestID).removeValue();

        // Remove from current display
        requestDetails.dismiss();
        pendingDisplay.remove(pos);
        adapterPending.updateList(pendingDisplay);
    }

    private void completeMessage(RequestClass request, String status) {
        reference = FirebaseDatabase.getInstance().getReference("Completed Requests");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String message = String.format("Request %s by admin: %s on %s", status, username, formatter.format(date));

        // Create storage data structure
        CompletedRequest comRequest = new CompletedRequest(request.getUsername(), request.getEmail(), request.getPhone(),
                request.getRequest_id(), request.getRequest_subject(), request.getRequest_details(), priority, message);

        reference.push().setValue(comRequest);
    }
}