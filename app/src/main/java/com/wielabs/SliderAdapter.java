package com.wielabs;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.wielabs.Models.PastProducts;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private int deviceWidth;
//    private boolean[] isDimensionChanged = new boolean[getItemCount()];
    private ArrayList<PastProducts> pastProducts;
    private Context context;

    public SliderAdapter(Context context, int deviceWidth, ArrayList<PastProducts> pastProducts) {
        this.deviceWidth = deviceWidth;
        this.pastProducts = pastProducts;
        this.context = context;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SliderViewHolder holder, final int position) {

        Glide.with(context)
                .asBitmap()
                .load(pastProducts.get(position).getImage_url())
                .into(holder.sliderImageBg);

        holder.sliderWinnerName.setText(pastProducts.get(position).getWinner());

        holder.MRP.setText(pastProducts.get(position).getMrp());

        holder.sliderWinnerTitle.setText("Product sold for ₹"+pastProducts.get(position).getBidamount());

        if (position % 6 == 0 /*&& !isDimensionChanged[position]*/) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(), R.color.color1));
        }
        else if (position % 6 == 1 /*&& !isDimensionChanged[position]*/) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(), R.color.color2));
        }
        else if (position % 6 == 2 /*&& !isDimensionChanged[position]*/) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(), R.color.color3));
        }
        else if (position % 6 == 3 /*&& !isDimensionChanged[position]*/) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(), R.color.color4));
        }
        else if (position % 6 == 4 /*&& !isDimensionChanged[position]*/) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(), R.color.color5));
        }
        else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(), R.color.color6));
        }
    }

    @Override
    public int getItemCount() {
        return pastProducts.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView sliderImageBg;
        private CoordinatorLayout sliderCoordinator;
        private ConstraintLayout constraintLayout;
        private MaterialCardView cardView;
        private TextView sliderWinnerTitle;
        private TextView sliderWinnerName;
        private TextView MRP;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderImageBg = itemView.findViewById(R.id.sliderImage);
            sliderCoordinator = itemView.findViewById(R.id.sliderCoordinator);
            constraintLayout = itemView.findViewById(R.id.constraintLayout2);
            cardView = itemView.findViewById(R.id.sliderCardView);
            sliderWinnerTitle = itemView.findViewById(R.id.sliderWinnerTitle);
            sliderWinnerName = itemView.findViewById(R.id.sliderWinnerName);
            MRP = itemView.findViewById(R.id.sliderMrpAmount);
            sliderCoordinator.post(new Runnable() {
                @Override
                public void run() {

                    FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) constraintLayout.getLayoutParams();
                    layoutParams1.width = (int)(deviceWidth / 2.5);
                    constraintLayout.setLayoutParams(layoutParams1);

                    ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) sliderWinnerTitle.getLayoutParams();
                    layoutParams2.topMargin = sliderImageBg.getHeight() / 2 + dpToPx(sliderImageBg, 16);
                    sliderWinnerTitle.setLayoutParams(layoutParams2);

                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) sliderCoordinator.getLayoutParams();
                    //layoutParams.width = (int)(deviceWidth / 3);
                    layoutParams.height = (int)sliderCoordinator.getHeight() + (int)(sliderImageBg.getHeight() + 1.5 * layoutParams2.topMargin);
                    sliderCoordinator.setLayoutParams(layoutParams);

                    /*ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) sliderImageBg.getLayoutParams();
                    layoutParams1.setMarginEnd((int)(sliderImageBg.getWidth() / 2));
                    layoutParams1.bottomMargin = (int)(sliderImageBg.getHeight() / 2.5);
                    layoutParams1.setMarginStart(sliderCoordinator.getWidth() - sliderImageBg.getWidth() + dpToPx(sliderCoordinator, 24));
                    sliderImageBg.setLayoutParams(layoutParams1);*/
                }
            });
        }
    }

    private int dpToPx(View view, int dp) {
        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
