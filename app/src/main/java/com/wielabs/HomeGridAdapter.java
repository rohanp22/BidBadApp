package com.wielabs;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
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
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.card.MaterialCardView;
import com.wielabs.Models.Current_Product;

import java.util.ArrayList;

public class HomeGridAdapter extends RecyclerView.Adapter<HomeGridAdapter.HomeGridViewHolder> {
    private Context context;
    private int deviceWidth;
    private boolean isDimensionChanged = false;
    private ArrayList<Integer> colors = new ArrayList<>();
    private GradientDrawable gradientDrawable;
    ArrayList<Current_Product> current_products;

    public HomeGridAdapter(Context context, int deviceWidth, ArrayList<Current_Product> current_products) {
        this.context = context;
        this.deviceWidth = deviceWidth;
        this.current_products = current_products;
        this.colors.add(ContextCompat.getColor(context, R.color.color6));
        this.colors.add(ContextCompat.getColor(context, R.color.color7));
        this.colors.add(ContextCompat.getColor(context, R.color.color8));
        this.colors.add(ContextCompat.getColor(context, R.color.color9));
        this.colors.add(ContextCompat.getColor(context, R.color.color10));
        this.colors.add(ContextCompat.getColor(context, R.color.color11));
    }

    @NonNull
    @Override
    public HomeGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeGridViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeGridViewHolder holder, int position) {
        if (position == getItemCount())
            isDimensionChanged = true;
        if (position % 7 == 0 && !isDimensionChanged)
            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.watch));
        else if (position % 7 == 1 && !isDimensionChanged)
            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.headphones));
        else if (position % 7 == 2 && !isDimensionChanged)
            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.bottle));
        else if (position % 7 == 3 && !isDimensionChanged)
            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.ps4));
        else if (position % 7 == 4 && !isDimensionChanged)
            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.iphone));
        else if (position % 7 == 5 && !isDimensionChanged)
            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.headphones));
        else if (!isDimensionChanged)
            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.shoes));

        ((GradientDrawable)holder.bidButton.getBackground()).setColor(colors.get(position % 6));
        ((GradientDrawable)holder.constraintLayout.getBackground()).setColor(colors.get(position % 6));

        int imageHeight = holder.productImage.getDrawable().getIntrinsicHeight();
        int imageWidth = holder.productImage.getDrawable().getIntrinsicWidth();
        if (imageHeight > imageWidth && !isDimensionChanged) {
            Log.d("Adapter", "H > W");
            holder.constraintLayout.post(new Runnable() {
                @Override
                public void run() {
                    // Get height and width of ImageView (not the png, but the view)
                    int height = holder.productImage.getHeight();
                    int width = holder.productImage.getWidth();
                    ViewGroup.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    Log.d("Adapter", (layoutParams instanceof FrameLayout.LayoutParams) + "");
                    layoutParams.height = (int) (height * 1.5);
                    layoutParams.width = width;
                    holder.productImage.setLayoutParams(layoutParams);

                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(holder.constraintLayout);
                    constraintSet.connect(
                            holder.productImage.getId(),
                            ConstraintSet.TOP,
                            holder.bidTimer.getId(),
                            ConstraintSet.BOTTOM,
                            dpToPx(holder.productImage, 16)
                    );
                    constraintSet.connect(
                            holder.productImage.getId(),
                            ConstraintSet.START,
                            holder.constraintLayout.getId(),
                            ConstraintSet.START
                    );
                    constraintSet.connect(
                            holder.productImage.getId(),
                            ConstraintSet.END,
                            holder.constraintLayout.getId(),
                            ConstraintSet.END
                    );
                    constraintSet.applyTo(holder.constraintLayout);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    class HomeGridViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardView;
        private ImageView productImage;
        private ConstraintLayout constraintLayout;
        private TextView bidTimer;
        private TextView bidButton;

        public HomeGridViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.homeItemCard);
            productImage = itemView.findViewById(R.id.homeItemImage);
            constraintLayout = itemView.findViewById(R.id.itemHomeRootLayout);
            bidTimer = itemView.findViewById(R.id.homeItemBidTimer);
            bidButton = itemView.findViewById(R.id.homeItemBid);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) cardView.getLayoutParams();
            layoutParams.width = (int) (deviceWidth / 2.22);
            cardView.setLayoutParams(layoutParams);
        }
    }

    private int pxToDp(View view, int px) {
        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private int dpToPx(View view, int dp) {
        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
