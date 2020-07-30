package com.wielabs.Fragments;

import android.content.Context;
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

public class AllBidsFragment extends Fragment {

    ArrayList<PastProducts> cartItems;
    RecyclerView cartList;
    ProgressBar progressBar;
    BidHistoryAdapterAll adapterall;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_allbids, container, false);
        cartList = view.findViewById(R.id.allbidsRecyclerview);
        cartList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        cartList.addItemDecoration(dividerItemDecoration);
        cartItems = new ArrayList<>();
        progressBar = view.findViewById(R.id.allbidsprogress);
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
                                        heroObject.getString("image_url3"),
                                        heroObject.getString("firstname"),
                                        heroObject.getString("bidamount"),
                                        heroObject.getString("id"),
                                        null
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

        return view;
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

        BidHistoryAdapterAll(Context context, ArrayList<PastProducts> heroList) {
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
            if (getItemCount() == 0) {
                progressBar.setVisibility(View.GONE);
            }
            if (SharedPrefManager.getInstance(context).getUser().getId() == Integer.parseInt(heroList.get(position).getWinnerid())) {
                holder.bidHistoryRank.setText("You won");
                holder.bidHistoryRank.setTextColor(getResources().getColor(R.color.colorDarkGreen));
            } else {
                holder.bidHistoryRank.setText(heroList.get(position).getWinner());
                holder.bidHistoryRank.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            progressBar.setVisibility(View.GONE);
            holder.bidHistoryTitle.setText(heroList.get(position).getTitle());
            String pattern = "dd MMM yyyy HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            Date date = null;
            try {
                date = simpleDateFormat2.parse(heroList.get(position).getEnd_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(date);
            String dt = simpleDateFormat.format(date);
            holder.bidHistoryStartDate.setText(dt);
            holder.bidHistoryAmount.setText(getResources().getString(R.string.ruppesymbol) + heroList.get(position).getBidamount());
            Glide.with(context)
                    .load(heroList.get(position).getImage_url())
                    .into(holder.bidHistoryImage);
        }

        @Override
        public int getItemCount() {
            if(cartItems.size() == 0){
                progressBar.setVisibility(View.GONE);
            }
            Log.d("Size", cartItems.size()+"");
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
}