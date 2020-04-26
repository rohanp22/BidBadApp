package com.wielabs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ProductDetailsViewHolder> {

    ArrayList<String> details;

    public ProductDetailsAdapter(ArrayList<String> details){
        this.details = details;
    }

    @NonNull
    @Override
    public ProductDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductDetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailsViewHolder holder, int position) {
        holder.detail.setText(details.get(position));
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class ProductDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView detail;
        ProductDetailsViewHolder(View itemView) {
            super(itemView);
            detail = itemView.findViewById(R.id.cardItem2);
        }
    }
}