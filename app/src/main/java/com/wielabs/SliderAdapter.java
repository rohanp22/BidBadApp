package com.wielabs;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
    private ArrayList<Integer> colors = new ArrayList<>();
    ArrayList<PastProducts> pastProducts;
    ArrayList<GradientDrawable> gradientDrawables = new ArrayList<>();
    int SIZE;

    public SliderAdapter(RecyclerView recyclerView, ArrayList<PastProducts> pastProducts) {
        this.recyclerView = recyclerView;
        this.pastProducts = pastProducts;
        this.SIZE = pastProducts.size();
  //      this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Color.parseColor("#006fff"), Color.parseColor("#00e5e5"), Color.parseColor("#0bff96")}));
  //      this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Color.parseColor("#ec038b"), Color.parseColor("#fb6468"), Color.parseColor("#fbb936")}));
//        this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Color.parseColor("#ffb8b7"), Color.parseColor("#ff5da9"), Color.parseColor("#a115ad")}));
//        this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Color.parseColor("#13ffff"), Color.parseColor("#3ba3ff"), Color.parseColor("#9c00ff")}));
//        this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Color.parseColor("#b6feff"), Color.parseColor("#5bfeb0"), Color.parseColor("#20ad16")}));
//        this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Color.parseColor("#ffecb6"), Color.parseColor("#ff8b5e"), Color.parseColor("#ad1646")}));
//        this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Color.parseColor("#ffadf2"), Color.parseColor("#d157fb"), Color.parseColor("#441dae")}));

        this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#E35588"), Color.parseColor("#FCC190")}));
        this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#4A59EA"), Color.parseColor("#A0A3FD")}));
        this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#42D6B8"), Color.parseColor("#5E96E8")}));
        this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#D756C9"), Color.parseColor("#464CDC")}));
        this.gradientDrawables.add(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#64C0EA"), Color.parseColor("#5E8DE4")}));

        this.colors.add(ContextCompat.getColor(recyclerView.getContext(), R.color.color1));
        this.colors.add(ContextCompat.getColor(recyclerView.getContext(), R.color.color2));
        this.colors.add(ContextCompat.getColor(recyclerView.getContext(), R.color.color3));
        this.colors.add(ContextCompat.getColor(recyclerView.getContext(), R.color.color4));
        this.colors.add(ContextCompat.getColor(recyclerView.getContext(), R.color.color5));
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
        holder.itemView.findViewById(R.id.constraintLayout).setBackground((GradientDrawable) gradientDrawables.get(position % 5));

        if (position == Integer.MAX_VALUE / 2) {
            holder.sliderDetailRoot.setVisibility(View.VISIBLE);
            holder.sliderDetailRoot.animate().alpha(1.0f);
        }

        Glide.with(holder.imageView.getContext())
                .load(pastProducts.get(position % SIZE).getImage_url())
                .into(holder.imageView);

        holder.winnername.setText(pastProducts.get(position % SIZE).getWinner());
        holder.title.setText(pastProducts.get(position % SIZE).getTitle());
        holder.mrp.setText(holder.itemView.getContext().getResources().getString(R.string.ruppesymbol) + pastProducts.get(position % SIZE).getMrp());
        holder.spent.setText(holder.itemView.getContext().getResources().getString(R.string.ruppesymbol) + pastProducts.get(position % SIZE).getBidamount());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout sliderDetailRoot;
        private ImageView imageView;
        private ConstraintLayout constraintLayout;
        TextView spent, title, winnername, mrp;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            spent = itemView.findViewById(R.id.sliderBidAmount);
            title = itemView.findViewById(R.id.sliderProductTitle);
            winnername = itemView.findViewById(R.id.sliderTitle);
            mrp = itemView.findViewById(R.id.sliderMrp);
            imageView = itemView.findViewById(R.id.sliderImage);
            sliderDetailRoot = itemView.findViewById(R.id.sliderDetailRoot);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
}
