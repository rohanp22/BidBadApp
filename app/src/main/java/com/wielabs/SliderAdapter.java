package com.wielabs;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wielabs.Models.PastProducts;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<Integer> colors = new ArrayList<>();
    private  ArrayList<PastProducts> pastProducts;

    public SliderAdapter(Context context, RecyclerView recyclerView, ArrayList<PastProducts> pastProducts) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.colors.add(ContextCompat.getColor(recyclerView.getContext(), R.color.color1));
        this.colors.add(ContextCompat.getColor(recyclerView.getContext(), R.color.color2));
        this.colors.add(ContextCompat.getColor(recyclerView.getContext(), R.color.color3));
        this.colors.add(ContextCompat.getColor(recyclerView.getContext(), R.color.color4));
        this.colors.add(ContextCompat.getColor(recyclerView.getContext(), R.color.color5));
        this.pastProducts = pastProducts;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, parent, false);
        int width = recyclerView.getWidth();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) (width * .65);
        view.setLayoutParams(layoutParams);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SliderViewHolder holder, final int position) {
        ((GradientDrawable) holder.itemView.findViewById(R.id.constraintLayout).getBackground()).setColor(colors.get(position % 5));
        if (position == 0) {
            holder.sliderDetailRoot.setVisibility(View.VISIBLE);
            holder.sliderDetailRoot.animate().alpha(1.0f);
        }
        holder.bidAmount.setText("₹"+pastProducts.get(position).getBidamount());
        holder.mrp.setText("₹"+pastProducts.get(position).getMrp());
        holder.winnerName.setText(pastProducts.get(position).getWinner());
        Glide.with(context)
                .asBitmap()
                .load(pastProducts.get(position).getImage_url())
                .into(holder.sliderImage);

        Glide.with(context)
                .asBitmap()
                .load(pastProducts.get(position).getImage_url())
                .into(holder.backgroundScaled);
        holder.productTitle.setText(pastProducts.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return pastProducts.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout sliderDetailRoot;
        private ImageView backgroundScaled;
        private ConstraintLayout constraintLayout;
        private TextView bidAmount;
        private TextView winnerName;
        private TextView mrp;
        private ImageView sliderImage;
        private TextView productTitle;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderDetailRoot = itemView.findViewById(R.id.sliderDetailRoot);
            backgroundScaled = itemView.findViewById(R.id.backgroundScaled);
            bidAmount = itemView.findViewById(R.id.sliderBidAmount);
            winnerName = itemView.findViewById(R.id.sliderTitle);
            productTitle = itemView.findViewById(R.id.sliderProductTitle);
            mrp = itemView.findViewById(R.id.sliderMrp);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            sliderImage = itemView.findViewById(R.id.sliderImage);

            constraintLayout.post(new Runnable() {
                @Override
                public void run() {
                    final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) backgroundScaled.getLayoutParams();
                    Log.d("ViewHolder", (int)(layoutParams.width) + "");
                    backgroundScaled.setPadding(0, 0, (int)(layoutParams.width * .2), 0);
                    //backgroundScaled.setLayoutParams(layoutParams);
                }
            });
        }
    }
}
