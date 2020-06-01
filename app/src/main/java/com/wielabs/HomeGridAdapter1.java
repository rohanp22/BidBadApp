package com.wielabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.card.MaterialCardView;
import com.wielabs.Fragments.ActionBottomDialogFragment;
import com.wielabs.Fragments.HomeFragment;
import com.wielabs.Fragments.ProductDetail;
import com.wielabs.Models.Current_Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeGridAdapter1 extends RecyclerView.Adapter<HomeGridAdapter1.HomeGridViewHolder> {
    private int deviceWidth;
    private boolean isDimensionChanged = false;
    private ArrayList<Integer> colors = new ArrayList<>();
    private GradientDrawable gradientDrawable;
    private ArrayList<Current_Product> current_products;
    private FragmentManager fragmentManager;
    Context context;

    public HomeGridAdapter1(int deviceWidth, ArrayList<Current_Product> current_products, FragmentManager fragmentManager, Context context) {
        this.deviceWidth = deviceWidth;
        this.current_products = current_products;
        this.fragmentManager = fragmentManager;
        this.context = context;
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

        this.colors.add(ContextCompat.getColor(holder.constraintLayout.getContext(), R.color.color6));
        this.colors.add(ContextCompat.getColor(holder.constraintLayout.getContext(), R.color.color7));
        this.colors.add(ContextCompat.getColor(holder.constraintLayout.getContext(), R.color.color8));
        this.colors.add(ContextCompat.getColor(holder.constraintLayout.getContext(), R.color.color9));
        this.colors.add(ContextCompat.getColor(holder.constraintLayout.getContext(), R.color.color10));
        this.colors.add(ContextCompat.getColor(holder.constraintLayout.getContext(), R.color.color11));

        if (position == getItemCount())
            isDimensionChanged = true;

//        ((GradieentDrawable)holder.bidButton.getBackground()).setColor(colors.get(position % 6));
//        ((GradientDrawable)holder.constraintLayout.getBackground()).setColor(colors.get(position % 6));

        holder.title.setText(current_products.get(position).getTitle());
        holder.mrp.setText("MRP: " + context.getResources().getString(R.string.ruppesymbol) + current_products.get(position).getMrp());
        holder.bidEntry.setText(current_products.get(position).getSp());
        date = current_products.get(position).getEnd_date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProductDetail();
                Bundle b = new Bundle();
                b.putInt("width", deviceWidth);
                b.putString("YourKey", current_products.get(position).getId());
                b.putInt("color", colors.get(position % 6));
                fragment.setArguments(b);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

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
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }

        }.start();

        holder.bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionBottomDialogFragment addPhotoBottomDialogFragment =
                        ActionBottomDialogFragment.newInstance(current_products.get(position).getId(), current_products.get(position).getImage_url(), current_products.get(position).getTitle(), current_products.get(position).getSp());
                addPhotoBottomDialogFragment.show(fragmentManager,
                        ActionBottomDialogFragment.TAG);
            }
        });

        Glide.with(holder.constraintLayout.getContext())
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
                holder.productImage.setImageBitmap(bitmap);

//                holder.bidEntry.setText(holder.bidEntry.getContext().getResources().getString(R.string.ruppesymbol) + current_products.get(position).getSp());
//
//                if (imageHeight > imageWidth) {
//                    //holder.bidEntry.setText(current_products.get(position).getSp());
//
//                    holder.constraintLayout.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            // Get height and width of ImageView (not the png, but the view)
//                            int height = holder.productImage.getHeight();
//                            int width = holder.productImage.getWidth();
//                            ViewGroup.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//                            layoutParams.height = (int) (height * 1.5);
//                            layoutParams.width = width;
//                            holder.productImage.setLayoutParams(layoutParams);
//
//                            ConstraintSet constraintSet = new ConstraintSet();
//                            constraintSet.clone(holder.constraintLayout);
//                            constraintSet.connect(
//                                    holder.productImage.getId(),
//                                    ConstraintSet.TOP,
//                                    holder.bidTimer.getId(),
//                                    ConstraintSet.BOTTOM,
//                                    dpToPx(holder.productImage, 16)
//                            );
//                            constraintSet.connect(
//                                    holder.productImage.getId(),
//                                    ConstraintSet.START,
//                                    holder.constraintLayout.getId(),
//                                    ConstraintSet.START
//                            );
//                            constraintSet.connect(
//                                    holder.productImage.getId(),
//                                    ConstraintSet.END,
//                                    holder.constraintLayout.getId(),
//                                    ConstraintSet.END
//                            );
//                            constraintSet.applyTo(holder.constraintLayout);
//                        }
//                    });
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return current_products.size();
    }

    class HomeGridViewHolder extends RecyclerView.ViewHolder {
        private ImageView bidButton;
        private MaterialCardView cardView;
        private ImageView productImage;
        private TextView title;
        private TextView bidEntry;
        TextView mrp;
        private ConstraintLayout constraintLayout;
        private TextView bidTimer;
        private ImageView bid;

        public HomeGridViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.homeItemTitle);
            bidEntry = itemView.findViewById(R.id.homeItemMinBid);
            mrp = itemView.findViewById(R.id.homeItemMrp);
            cardView = itemView.findViewById(R.id.homeItemCard);
            bid = itemView.findViewById(R.id.homeItemBid);
            productImage = itemView.findViewById(R.id.homeItemImage);
            constraintLayout = itemView.findViewById(R.id.itemHomeRootLayout);
            bidTimer = itemView.findViewById(R.id.homeItemBidTimer);
            bidButton = itemView.findViewById(R.id.homeItemBid);
        }
    }

    private int dpToPx(View view, int dp) {
        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void showBottomSheet() {

    }
}
