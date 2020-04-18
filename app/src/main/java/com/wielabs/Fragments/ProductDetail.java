package com.wielabs.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.wielabs.Activities.ProductDescription;
import com.wielabs.DeliveryDetails;
import com.wielabs.ProductDetails;
import com.wielabs.R;

public class ProductDetail extends Fragment {
    private float deliveryXCoordinate;
    private float productXCoordinate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        int deviceWidth = 0;
        if (getArguments() != null)
            deviceWidth = getArguments().getInt("width");
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        final int finalDeviceWidth = deviceWidth;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.post(new Runnable() {

            @Override
            public void run() {
                recyclerView.setAdapter(new ProductDescriptionImageAdapter(finalDeviceWidth / 2, recyclerView));
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        final MaterialButton productDetails = view.findViewById(R.id.detailProductDescription);
        final MaterialButton deliveryDetails = view.findViewById(R.id.detailDeliveryDescription);
        final MaterialButton bidNow = view.findViewById(R.id.bidNow);
        final ConstraintLayout detailConstraintLayout = view.findViewById(R.id.detailConstraintLayout);
        final View dot = view.findViewById(R.id.view3);

        detailConstraintLayout.post(new Runnable() {
            @Override
            public void run() {
                // Set bottom padding for ConstraintLayout
                detailConstraintLayout.setPadding(0, 0, 0, bidNow.getHeight() + 2 * bidNow.getPaddingBottom());
                /*FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.bottomMargin = bidNow.getHeight() + 3 * bidNow.getPaddingBottom();
                view.findViewById(R.id.fragmentDetailsHolder).setLayoutParams(layoutParams);*/
                // Get X coordinates for later reference
                productXCoordinate = productDetails.getX();
                deliveryXCoordinate = deliveryDetails.getX();
            }
        });

        productDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dot.animate().setDuration(250).translationX(productXCoordinate).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFragmentManager().beginTransaction().replace(R.id.fragmentDetailsHolder, new ProductDetails()).commit();
                    }
                }, 250);
            }
        });

        deliveryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dot.animate().setDuration(250).translationX(deliveryXCoordinate).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFragmentManager().beginTransaction().replace(R.id.fragmentDetailsHolder, new DeliveryDetails()).commit();
                    }
                }, 250);
            }
        });
    }

    public class ProductDescriptionImageAdapter extends RecyclerView.Adapter<ProductDescriptionImageAdapter.ProductDescriptionImageViewHolder> {
        private int deviceHalfWidth;
        private RecyclerView recyclerView;

        public ProductDescriptionImageAdapter(int deviceHalfWidth, RecyclerView recyclerView) {
            this.deviceHalfWidth = deviceHalfWidth;
            this.recyclerView = recyclerView;
        }

        @NonNull
        @Override
        public ProductDescriptionImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductDescriptionImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_image, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductDescriptionImageViewHolder holder, int position) {
            holder.backgroundShape.setTransitionName("transition_" + position);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            if (position == 0) {
                layoutParams.width = deviceHalfWidth;
                layoutParams.height = deviceHalfWidth;
            }

        }

        @Override
        public int getItemCount() {
            return 3;
        }

        class ProductDescriptionImageViewHolder extends RecyclerView.ViewHolder {
            private ImageView backgroundShape;

            ProductDescriptionImageViewHolder(@NonNull View itemView) {
                super(itemView);
                backgroundShape = itemView.findViewById(R.id.backgroundShape);
            }
        }
    }
}

