package com.qrofeus.requestmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestAdapterPending extends RecyclerView.Adapter<RequestAdapterPending.RequestViewHolder> {

    private ArrayList<RequestClass> arrayList;
    private OnItemClickListener clickListener;

    public RequestAdapterPending(ArrayList<RequestClass> arrayList) {
        this.arrayList = arrayList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    public void updateList(ArrayList<RequestClass> newList) {
        arrayList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card, parent, false);
        return new RequestViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestClass request = arrayList.get(position);

        holder.cardID.setText(String.format("RID: %s", request.getRequest_id().toUpperCase()));
        holder.cardSubject.setText(String.format("Subject: %s", request.getRequest_subject()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        public TextView cardID;
        public TextView cardSubject;

        public RequestViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            cardID = itemView.findViewById(R.id.request_listID);
            cardSubject = itemView.findViewById(R.id.request_listSubject);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(getAdapterPosition());
                }
            });
        }
    }
}
