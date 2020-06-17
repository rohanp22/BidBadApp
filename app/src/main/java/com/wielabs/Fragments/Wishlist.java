package com.wielabs.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.wielabs.Models.Current_Product;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Wishlist extends Fragment {

    ArrayList<Current_Product> wishlistProducts;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        // Inflate the layout for this fragment
        final RecyclerView recyclerView = view.findViewById(R.id.wishlistRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        wishlistProducts = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getwishlist.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            wishlistProducts.clear();
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Bids");
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                Current_Product c = new Current_Product(
                                        heroObject.getString("currentid"),
                                        heroObject.getString("image_url"),
                                        heroObject.getString("title"),
                                        heroObject.getString("endtime"),
                                        heroObject.getString("mrp"),
                                        heroObject.getString("sp"),
                                        heroObject.getString("description"),
                                        heroObject.getString("image_url2"),
                                        heroObject.getString("image_url3"));
                                wishlistProducts.add(c);
                            }
                            WishlistAdapter wishlistAdapter = new WishlistAdapter(view.getContext(), wishlistProducts);
                            recyclerView.setAdapter(wishlistAdapter);
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
        return view;
    }

    class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.BidHistoryViewHolder> {

        Context context;
        ArrayList<Current_Product> heroList;

        WishlistAdapter(Context context, ArrayList<Current_Product> heroList) {
            this.context = context;
            this.heroList = heroList;
        }

        @NonNull
        @Override
        public WishlistAdapter.BidHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WishlistAdapter.BidHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bid_history, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BidHistoryViewHolder holder, int position) {
            if (getItemCount() == 0) {
                //    progressBar.setVisibility(View.GONE);
            }
            //progressBar.setVisibility(View.GONE);
            holder.bidHistoryTitle.setText(heroList.get(position).getTitle());
            holder.bidHistoryStartDate.setText(heroList.get(position).getEnd_date());
            Glide.with(context)
                    .load(heroList.get(position).getImage_url())
                    .into(holder.bidHistoryImage);
        }

        @Override
        public int getItemCount() {
            if (heroList.size() == 0) {
                //progressBar.setVisibility(View.GONE);
            }
            Log.d("Size", heroList.size() + "");
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


