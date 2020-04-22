package com.wielabs.Fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.wielabs.Activities.ProductDescription;
import com.wielabs.DeliveryDetails;
import com.wielabs.Models.Current_Product;
import com.wielabs.ProductDetails;
import com.wielabs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ProductDetail extends Fragment {
    private float deliveryXCoordinate;
    private float productXCoordinate;
    String productId;
    private String currentid, imageurl, imageurl2, imageurl3, titleString, mrpString, sp, endtime, description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        getActivity().findViewById(R.id.fabhome).setVisibility(View.GONE);
        getActivity().findViewById(R.id.bar).setVisibility(View.GONE);

        final TextView detailPrice = view.findViewById(R.id.detailPrice);
        final TextView detailAmount = view.findViewById(R.id.detailOfferCalculatedAmt);
        final TextView detailTitle = view.findViewById(R.id.detailProductTitle);
        int deviceWidth = 0;
        if (getArguments() != null)
            deviceWidth = getArguments().getInt("width");
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        final int finalDeviceWidth = deviceWidth;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        final MaterialButton productDetails = view.findViewById(R.id.detailProductDescription);
        final MaterialButton deliveryDetails = view.findViewById(R.id.detailDeliveryDescription);
        final MaterialButton bidNow = view.findViewById(R.id.bidNow);
        final ConstraintLayout detailConstraintLayout = view.findViewById(R.id.detailConstraintLayout);
        final View dot = view.findViewById(R.id.view3);
        String value = "0";
        if (getArguments() != null) {
            value = getArguments().getString("YourKey");
            productId = value;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getproductinfo.php?id=" + value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Current_Products");
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                currentid = heroObject.getString("currentid");
                                imageurl = heroObject.getString("image_url");
                                imageurl2 = heroObject.getString("image_url2");
                                imageurl3 = heroObject.getString("image_url3");
                                titleString = heroObject.getString("title");
                                endtime = heroObject.getString("endtime");
                                mrpString = heroObject.getString("mrp");
                                description = heroObject.getString("description");
                                sp = heroObject.getString("sp");
                                final String[] imageurls = new String[]{imageurl, imageurl2, imageurl3};

                                detailPrice.setText(mrpString);
                                detailAmount.setText(sp);
                                detailTitle.setText(titleString);

                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(new ProductDescriptionImageAdapter(finalDeviceWidth / 2, recyclerView, imageurls));
                                        recyclerView.setLayoutManager(layoutManager);
                                    }
                                });

//                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
//
//                                try {
//                                    startDate1 = simpleDateFormat.parse(endtime);
//                                    diff = startDate1.getTime() - System.currentTimeMillis();
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//
//                                final long secondsInMilli = 1000;
//                                final long minutesInMilli = secondsInMilli * 60;
//                                final long hoursInMilli = minutesInMilli * 60;
//                                final long daysInMilli = hoursInMilli * 24;
//
//                                final long finalDiff = diff;
//
//                                new CountDownTimer(finalDiff, 1000) {
//
//                                    long dif = finalDiff;
//
//                                    public void onTick(long millisUntilFinished) {
//
//                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
//
//                                        try {
//                                            startDate1 = simpleDateFormat.parse(endtime);
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        long different = Math.abs(startDate1.getTime() - System.currentTimeMillis());
//
//                                        final long elapsedDays = different / daysInMilli;
//
//                                        final long elapsedHours = different / hoursInMilli;
//                                        different = different % hoursInMilli;
//
//                                        final long elapsedMinutes = different / minutesInMilli;
//                                        different = different % minutesInMilli;
//
//                                        final long elapsedSeconds = different / secondsInMilli;
//                                        String curtime = "";
//                                        if (elapsedHours > 0) {
//                                            curtime = elapsedHours + " hr " + String.format("%02d", elapsedMinutes) + " m ";
//                                        } else {
//                                            curtime = String.format("%02d", elapsedMinutes) + " m " + String.format("%02d", elapsedSeconds) + " s ";
//                                        }
//                                        //clock.setText(curtime);
//                                    }
//
//                                    public void onFinish() {
//                                    }
//                                }.start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

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
        private String[] imageurls;

        public ProductDescriptionImageAdapter(int deviceHalfWidth, RecyclerView recyclerView, String[] imageurls) {
            this.deviceHalfWidth = deviceHalfWidth;
            this.recyclerView = recyclerView;
            this.imageurls = imageurls;
        }

        @NonNull
        @Override
        public ProductDescriptionImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductDescriptionImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_image, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductDescriptionImageViewHolder holder, int position) {
            holder.backgroundShape.setTransitionName("transition_" + position);
            Glide.with(holder.itemView)
                    .load(imageurls[position])
                    .into(holder.productImage);
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
            private ImageView productImage;

            ProductDescriptionImageViewHolder(@NonNull View itemView) {
                super(itemView);
                productImage = itemView.findViewById(R.id.productImage);
                backgroundShape = itemView.findViewById(R.id.backgroundShape);
            }
        }
    }
}

