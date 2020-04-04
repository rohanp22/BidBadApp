package com.wielabs.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.wielabs.Activities.PlaceOrder;
import com.wielabs.Models.Orders;
import com.wielabs.Models.PastProducts;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EmptyFragment extends Fragment {

    ProgressBar progressBar;
    ArrayList<PastProducts> cartItems;
    View view;
    RecyclerView cartList;
    String bid;
    BidHistoryAdapterAll adapterall;
    public ArrayList<Orders> pastItems;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    EmptyFragment(String bid){
        this.bid = bid;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pastItems = new ArrayList<>();
        final View view = inflater.inflate(R.layout.bid_history_fragments, container, false);
        cartList = view.findViewById(R.id.bidHistoryRecyclerView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        cartList.addItemDecoration(dividerItemDecoration);
        cartList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        this.view = view;
        cartItems = new ArrayList<>();
        progressBar = view.findViewById(R.id.mybidsprogress);

        if(bid.equals("mybids"))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getmybids.php?id=" + SharedPrefManager.getInstance(getActivity()).getUser().getId(),
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(String response) {
                            try {
                                cartItems.clear();
                                JSONObject obj = new JSONObject(response);
                                JSONArray heroArray = obj.getJSONArray("Bids");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                                for (int i = 0; i < heroArray.length(); i++) {
                                    JSONObject heroObject = heroArray.getJSONObject(i);
                                    Date a1 = simpleDateFormat.parse(heroObject.getString("endtime"));
                                    PastProducts c = new PastProducts(
                                            heroObject.getString("past_id"),
                                            heroObject.getString("image_url"),
                                            heroObject.getString("title"),
                                            heroObject.getString("endtime"),
                                            heroObject.getString("mrp"),
                                            heroObject.getString("sp"),
                                            heroObject.getString("description"),
                                            heroObject.getString("image_url2"),
                                            heroObject.getString("image_url3")
                                    );
                                    if (!(a1.compareTo(new java.sql.Date(System.currentTimeMillis())) > 0))
                                        cartItems.add(c);
                                }
                                cartItems.sort(new sortTime());
                                adapterall = new BidHistoryAdapterAll(view.getContext(), cartItems);
                                cartList.setAdapter(adapterall);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(stringRequest);
        }
        else if(bid.equals("allbids"))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/pastproducts.php",
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(String response) {
                            try {
                                cartItems.clear();
                                JSONObject obj = new JSONObject(response);
                                JSONArray heroArray = obj.getJSONArray("Current_Products");
                                for (int i = 0; i < heroArray.length(); i++) {
                                    JSONObject heroObject = heroArray.getJSONObject(i);
                                    PastProducts c = new PastProducts(
                                            heroObject.getString("past_id"),
                                            heroObject.getString("image_url"),
                                            heroObject.getString("title"),
                                            heroObject.getString("endtime"),
                                            heroObject.getString("mrp"),
                                            heroObject.getString("sp"),
                                            heroObject.getString("description"),
                                            heroObject.getString("image_url2"),
                                            heroObject.getString("image_url3")
                                    );
                                    cartItems.add(c);
                                }
                                cartItems.sort(new sortTime());
                                adapterall = new BidHistoryAdapterAll(view.getContext(), cartItems);
                                cartList.setAdapter(adapterall);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        } else if(bid.equals("wonbids")){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getwonitems.php?id="+ SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(String response) {
                            try {
                                pastItems.clear();
                                JSONObject obj = new JSONObject(response);
                                JSONArray heroArray = obj.getJSONArray("Bids");
                                for (int i = 0; i < heroArray.length(); i++) {
                                    JSONObject heroObject = heroArray.getJSONObject(i);
                                    Orders c = new Orders(
                                            heroObject.getString("past_id"),
                                            heroObject.getString("image_url"),
                                            heroObject.getString("title"),
                                            heroObject.getString("endtime"),
                                            heroObject.getString("mrp"),
                                            heroObject.getString("sp"),
                                            heroObject.getString("description"),
                                            heroObject.getString("bidamount"),"Won", heroObject.getString("orderplaced"));
                                    pastItems.add(c);
                                }
                                pastItems.sort(new sortTime2());
                                WonItemsAdapter walletAdapter = new WonItemsAdapter(view.getContext(), pastItems);
                                cartList.setAdapter(walletAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }

        return view;
    }

    class sortTime2 implements Comparator<Orders> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Orders a, Orders b) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            Date a1 = null, b1 = null;
            try {
                a1 = simpleDateFormat.parse(a.getEnd_date());
                b1 = simpleDateFormat.parse(b.getEnd_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return b1.compareTo(a1);
        }
    }

    class sortTime implements Comparator<PastProducts> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(PastProducts a, PastProducts b) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            Date a1 = null, b1 = null;
            try {
                a1 = simpleDateFormat.parse(a.getEnd_date());
                b1 = simpleDateFormat.parse(b.getEnd_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return b1.compareTo(a1);
        }
    }

    class BidHistoryAdapterAll extends RecyclerView.Adapter<BidHistoryAdapterAll.BidHistoryViewHolder> {

        Context context;
        ArrayList<PastProducts> heroList;

        BidHistoryAdapterAll(Context context, ArrayList<PastProducts> heroList){
            this.context = context;
            this.heroList = heroList;
        }

        @NonNull
        @Override
        public BidHistoryAdapterAll.BidHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BidHistoryAdapterAll.BidHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bid_history, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BidHistoryViewHolder holder, int position) {
            progressBar.setVisibility(View.GONE);
            holder.bidHistoryTitle.setText(heroList.get(position).getTitle());
            holder.bidHistoryStartDate.setText(heroList.get(position).getEnd_date());
            holder.bidHistoryAmount.setText(heroList.get(position).getSp());
            Glide.with(context)
                    .load(heroList.get(position).getImage_url())
                    .into(holder.bidHistoryImage);
        }


        @Override
        public int getItemCount() {
            return cartItems.size();
        }

        class BidHistoryViewHolder extends RecyclerView.ViewHolder {
            ImageView bidHistoryImage;
            TextView bidHistoryTitle;
            TextView bidHistoryStartDate;
            TextView bidHistoryAmount;
            TextView bidHistoryRank;

            BidHistoryViewHolder(View itemView) {
                super(itemView);
                bidHistoryImage = itemView.findViewById(R.id.bidHistoryImage);
                bidHistoryAmount = itemView.findViewById(R.id.bidHistoryAmount);
                bidHistoryTitle = itemView.findViewById(R.id.bidHistoryTitle);
                bidHistoryStartDate = itemView.findViewById(R.id.bidHistoryStartDate);
                bidHistoryRank = itemView.findViewById(R.id.bidHistoryRank);
            }
        }
    }

    class WonItemsAdapter extends RecyclerView.Adapter<WonItemsAdapter.BidHistoryViewHolder> {

        Context context;
        ArrayList<Orders> heroList;

        WonItemsAdapter(Context context, ArrayList<Orders> heroList){
            this.context = context;
            this.heroList = heroList;
        }

        @NonNull
        @Override
        public WonItemsAdapter.BidHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WonItemsAdapter.BidHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bid_history, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BidHistoryViewHolder holder, final int position) {
            progressBar.setVisibility(View.GONE);
            holder.bidHistoryTitle.setText(heroList.get(position).getTitle());
            holder.bidHistoryStartDate.setText(heroList.get(position).getEnd_date());
            holder.bidHistoryAmount.setText(heroList.get(position).getSp());
            Glide.with(context)
                    .load(heroList.get(position).getImage_url())
                    .into(holder.bidHistoryImage);
            if(pastItems.get(position).getOrderplaced().equals("1")){
                holder.bidHistoryRank.setVisibility(View.GONE);
            } else {
                holder.bidHistoryRank.setText("Place order");
            }

            holder.bidHistoryRank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
                    String url = "http://easyvela.esy.es/AndroidAPI/placeorder.php?id="+ SharedPrefManager.getInstance(context).getUser().getId(); // <----enter your post url here
                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //This code is executed if the server responds, whether or not the response contains data.
                            //The String 'response' contains the server's response.
                        }
                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //This code is executed if there is an error.
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("productid", heroList.get(position).getId());
                            params.put("status", "Order placed");
                            params.put("address", SharedPrefManager.getInstance(context).getUser().getAddress());
                            return params;
                        }
                    };
                    MyRequestQueue.add(MyStringRequest);
                }
            });
        }

        @Override
        public int getItemCount() {
            return heroList.size();
        }

        class BidHistoryViewHolder extends RecyclerView.ViewHolder {
            ImageView bidHistoryImage;
            TextView bidHistoryTitle;
            TextView bidHistoryStartDate;
            TextView bidHistoryAmount;
            TextView bidHistoryRank;

            BidHistoryViewHolder(View itemView) {
                super(itemView);
                bidHistoryImage = itemView.findViewById(R.id.bidHistoryImage);
                bidHistoryAmount = itemView.findViewById(R.id.bidHistoryAmount);
                bidHistoryTitle = itemView.findViewById(R.id.bidHistoryTitle);
                bidHistoryStartDate = itemView.findViewById(R.id.bidHistoryStartDate);
                bidHistoryRank = itemView.findViewById(R.id.bidHistoryRank);
            }
        }
    }
}
