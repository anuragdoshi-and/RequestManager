package com.qrofeus.requestmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestAdapterComplete extends RecyclerView.Adapter<RequestAdapterComplete.RequestViewHolder> {

    onItemClickListener listener;
    private ArrayList<CompletedRequest> requests;

    public RequestAdapterComplete(ArrayList<CompletedRequest> requests) {
        this.requests = requests;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void updateList(ArrayList<CompletedRequest> newRequests) {
        requests = newRequests;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card, parent, false);
        return new RequestViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapterComplete.RequestViewHolder holder, int position) {
        CompletedRequest request = requests.get(position);

        holder.cardID.setText(String.format("RID: %s", request.getReq_id().toUpperCase()));
        holder.cardSubject.setText(String.format("Subject: %s", request.getReq_subject()));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        public TextView cardID;
        public TextView cardSubject;

        public RequestViewHolder(@NonNull View itemView, final onItemClickListener clickListener) {
            super(itemView);

            cardID = itemView.findViewById(R.id.request_listID);
            cardSubject = itemView.findViewById(R.id.request_listSubject);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
