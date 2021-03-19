package com.example.facebooknativeads.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FacebookRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FaceBookRecyclerAdapterInterface faceBookRecyclerAdapterInterface;
    public FacebookRecyclerAdapter(FaceBookRecyclerAdapterInterface faceBookRecyclerAdapterInterface) {
        this.faceBookRecyclerAdapterInterface = faceBookRecyclerAdapterInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return faceBookRecyclerAdapterInterface.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        faceBookRecyclerAdapterInterface.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return faceBookRecyclerAdapterInterface.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return faceBookRecyclerAdapterInterface.getItemViewType(position);
    }
}
