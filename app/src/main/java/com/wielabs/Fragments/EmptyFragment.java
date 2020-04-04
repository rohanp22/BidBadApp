package com.wielabs.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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

public class EmptyFragment extends Fragment {

    ProgressBar progressBar;
    ArrayList<PastProducts> cartItems;
    View view;
    RecyclerView cartList;
    String bid;
    BidHistoryAdapterAll adapterall;

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
        }

        return view;
    }

    void loadMyBids(final View view){

    }

    void loadList(final View view){

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
}
