package com.example.facebooknativeads.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface FaceBookRecyclerAdapterInterface {
    RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);
    void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position);
    int getItemCount();
    int getItemViewType(int position);
}
