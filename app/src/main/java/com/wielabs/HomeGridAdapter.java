package com.wielabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.card.MaterialCardView;
import com.wielabs.Models.Current_Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeGridAdapter extends RecyclerView.Adapter<HomeGridAdapter.HomeGridViewHolder> {
    private Context context;
    private int deviceWidth;
    private boolean isDimensionChanged = false;
    private ArrayList<GradientDrawable> gradientColors = new ArrayList<>();
    private GradientDrawable gradientDrawable;
    private ArrayList<Current_Product> current_products;

    public HomeGridAdapter(Context context, int deviceWidth, ArrayList<Current_Product> current_products) {
        this.context = context;
        this.deviceWidth = deviceWidth;
        this.current_products = current_products;
        this.gradientColors.add(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{Color.parseColor("#ffafbd"), Color.parseColor("#ffc3a0")}));
        this.gradientColors.add(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{Color.parseColor("#eacda3"), Color.parseColor("#d6ae7b")}));
        this.gradientColors.add(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#a1ffce"), Color.parseColor("#faffd1")}));
        this.gradientColors.add(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{Color.parseColor("#b2fefa"), Color.parseColor("#0ed2f7")}));
        this.gradientColors.add(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#4e54c8"), Color.parseColor("#8f94fb")}));
        this.gradientColors.add(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#ffefba"), Color.parseColor("#ffffff")}));
        this.gradientColors.add(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#d9a7c7"), Color.parseColor("#fffcdc")}));
    }

    @NonNull
    @Override
    public HomeGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeGridViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false));
    }

    int imageHeight, imageWidth;
    String date;
    Date startDate1;


    @Override
    public void onBindViewHolder(@NonNull final HomeGridViewHolder holder, final int position) {
        if (position == getItemCount())
            isDimensionChanged = true;
//        if (position % 7 == 0 && !isDimensionChanged)
//            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.watch));
//        else if (position % 7 == 1 && !isDimensionChanged)
//            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.headphones));
//        else if (position % 7 == 2 && !isDimensionChanged)
//            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.bottle));
//        else if (position % 7 == 3 && !isDimensionChanged)
//            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.ps4));
//        else if (position % 7 == 4 && !isDimensionChanged)
//            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.iphone));
//        else if (position % 7 == 5 && !isDimensionChanged)
//            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.headphones));
//        else if (!isDimensionChanged)
//            holder.productImage.setImageDrawable(ContextCompat.getDrawable(holder.productImage.getContext(), R.drawable.shoes));

        date = current_products.get(position).getEnd_date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        long diff = 0;
        try {
            startDate1 = simpleDateFormat.parse(current_products.get(position).getEnd_date());
            diff = startDate1.getTime() - System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final long secondsInMilli = 1000;
        final long minutesInMilli = secondsInMilli * 60;
        final long hoursInMilli = minutesInMilli * 60;
        final long daysInMilli = hoursInMilli * 24;

        final long finalDiff = diff;

        new CountDownTimer(finalDiff, 1000) {

            long dif = finalDiff;

            public void onTick(long millisUntilFinished) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

                try {
                    startDate1 = simpleDateFormat.parse(current_products.get(position).getEnd_date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long different = startDate1.getTime() - System.currentTimeMillis();

                if (different > 0) {

                    final long elapsedDays = different / daysInMilli;

                    final long elapsedHours = different / hoursInMilli;
                    different = different % hoursInMilli;

                    final long elapsedMinutes = different / minutesInMilli;
                    different = different % minutesInMilli;

                    final long elapsedSeconds = different / secondsInMilli;

                    String curtime;

                    if (elapsedHours > 0) {
                        curtime = elapsedHours + " hr " + String.format("%02d", elapsedMinutes) + " min";
                        holder.bidTimer.setText(curtime);
                    } else {
                        curtime = String.format("%02d", elapsedMinutes) + " m " + String.format("%02d", elapsedSeconds) + " sec";
                        holder.bidTimer.setText(curtime);
                    }
                }
            }

            public void onFinish() {
            }

        }.start();

        Glide.with(context)
                .asBitmap()
                .load(current_products.get(position).getImage_url())
                .into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap,
                    Transition<? super Bitmap> transition) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                imageHeight = h;
                imageWidth = w;
                Log.d("height width", h+"    "+w);
                holder.productImage.setImageBitmap(bitmap);
                holder.bidEntry.setText(current_products.get(position).getSp());

                if (imageHeight > imageWidth) {
                    Log.d("Adapter", "H > W");        //holder.bidEntry.setText(current_products.get(position).getSp());

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
        });

        holder.constraintLayout.setBackground(gradientColors.get(position % 7));
    }

    @Override
    public int getItemCount() {
        Log.d("Current product size", ""+current_products.size());
        return current_products.size();
    }

    class HomeGridViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private ImageView productImage;
        private TextView bidEntry;
        private ConstraintLayout constraintLayout;
        private TextView bidTimer;

        public HomeGridViewHolder(@NonNull View itemView) {
            super(itemView);
            bidEntry = itemView.findViewById(R.id.homeItemBidAmount);
            cardView = itemView.findViewById(R.id.homeItemCard);
            productImage = itemView.findViewById(R.id.homeItemImage);
            constraintLayout = itemView.findViewById(R.id.itemHomeRootLayout);
            bidTimer = itemView.findViewById(R.id.homeItemBidTimer);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) cardView.getLayoutParams();
            layoutParams.width = (int) (deviceWidth / 2.22);
            cardView.setLayoutParams(layoutParams);
        }
    }

    private int dpToPx(View view, int dp) {
        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
