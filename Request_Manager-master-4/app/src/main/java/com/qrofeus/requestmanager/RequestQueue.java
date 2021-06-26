package com.qrofeus.requestmanager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;

public class RequestQueue extends AppCompatActivity implements Dialog_RequestDetails.Interface_requestDetails {

    private Dialog_RequestDetails requestDetails;
    private ArrayList<RequestClass> displayQueue;
    private ArrayList<RequestClass> searchList = null;
    private ArrayList<RequestClass> requestQueue = null;
    private DatabaseReference reference;

    private RequestAdapterPending adapter;
    private CardView emptyCard;
    private Spinner spinner;
    private EditText searchBar;

    private String priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_queue_layout);

        reference = FirebaseDatabase.getInstance().getReference().child("Low");

        spinner = findViewById(R.id.recycler_prioritySpin);
        searchBar = findViewById(R.id.searchEdit);
        emptyCard = findViewById(R.id.emptyCard);

        initialSetUp();
    }

    private void initialSetUp() {
        priority = spinner.getSelectedItem().toString();
        reference = FirebaseDatabase.getInstance().getReference(priority);
        requestQueue = new ArrayList<>();

        Toast.makeText(this, "Please wait a few seconds...", Toast.LENGTH_SHORT).show();

        // Get initial list
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    emptyCard.setVisibility(View.VISIBLE);
                    Toast.makeText(RequestQueue.this, "No current requests", Toast.LENGTH_SHORT).show();
                }
                try {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        RequestClass requestClass = dataSnapshot.getValue(RequestClass.class);
                        requestQueue.add(requestClass);
                    }
                } catch (Exception e) {
                    Toast.makeText(RequestQueue.this, "Error occurred: " + e.toString(), Toast.LENGTH_SHORT).show();
                }

                if (requestQueue.isEmpty())
                    emptyCard.setVisibility(View.VISIBLE);
                else
                    emptyCard.setVisibility(View.GONE);
                displayQueue = requestQueue;
                setUpAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RequestQueue.this, "Database Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpAdapter() {
        // Set Up Recycler View
        RecyclerView recyclerView = findViewById(R.id.recycler_request);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new RequestAdapterPending(displayQueue);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RequestAdapterPending.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                if (searchList != null)
                    requestDetails = new Dialog_RequestDetails(searchList.get(position), "Customer", spinner.getSelectedItemPosition());
                else
                    requestDetails = new Dialog_RequestDetails(displayQueue.get(position), "Customer", spinner.getSelectedItemPosition());
                requestDetails.show(getSupportFragmentManager(), "Request Details");
            }
        });

        activatePriority();
        activateSearchBar();
    }

    private void activatePriority() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPriorityList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getPriorityList() {
        emptyCard.setVisibility(View.GONE);
        requestQueue = new ArrayList<>();
        priority = spinner.getSelectedItem().toString();
        reference = FirebaseDatabase.getInstance().getReference(priority);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    RequestClass requestClass;
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        requestClass = snap.getValue(RequestClass.class);
                        requestQueue.add(requestClass);
                    }
                    displayQueue = requestQueue;
                    searchBar.setText("");
                    adapter.updateList(displayQueue);
                } else
                    emptyCard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void activateSearchBar() {
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

    // This works
    private void searchQuery(String text) {
        emptyCard.setVisibility(View.GONE);
        searchList = new ArrayList<>();
        for (RequestClass request : displayQueue) {
            if (request.getRequest_id().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(request);
            }
        }
        if (searchList.isEmpty())
            emptyCard.setVisibility(View.VISIBLE);
        adapter.updateList(searchList);
    }

    @Override
    // This works
    public void dismissRequest(String requestID) {
    }

    @Override
    public void completeRequest(String requestID) {
    }

    @Override
    public void changePriority(String requestID, String targetPriority) {
    }
}