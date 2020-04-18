package com.wielabs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ProductDetailsViewHolder> {

    @NonNull
    @Override
    public ProductDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductDetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ProductDetailsViewHolder extends RecyclerView.ViewHolder {
        ProductDetailsViewHolder(View itemView) {
            super(itemView);
        }
    }
}