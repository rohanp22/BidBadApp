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
import com.wielabs.Models.WonItem;
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

public class WonBidsFagment extends Fragment {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<WonItem> pastItems;
    WonItemsAdapter walletAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_won_items, container, false);
        recyclerView = view.findViewById(R.id.wonbidsrecyclerview);
        progressBar = view.findViewById(R.id.wonbidsprogressbar);
        loadWonBids(view);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        return view;
    }

    void loadWonBids(final View view) {
        final ArrayList<WonItem> sortedList = new ArrayList<>();
        pastItems = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getwonitems.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
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
                                WonItem c = new WonItem(
                                        heroObject.getString("past_id"),
                                        heroObject.getString("image_url"),
                                        heroObject.getString("title"),
                                        heroObject.getString("endtime"),
                                        heroObject.getString("mrp"),
                                        heroObject.getString("sp"),
                                        heroObject.getString("description"),
                                        heroObject.getString("bidamount"),
                                        "Won", heroObject.getString("orderplaced"));
                                pastItems.add(c);
                            }
                            int i;
                            pastItems.sort(new sortTime2());
                            for (i = 0; i < pastItems.size(); i++) {
                                if (pastItems.get(i).getOrderplaced().equals("null")) {
                                    sortedList.add(pastItems.get(i));
                                }
                            }
                            for (int j = 0; j < pastItems.size(); j++) {
                                if (pastItems.get(j).getOrderplaced().equals("1")) {
                                    sortedList.add(pastItems.get(j));
                                }
                            }
                            pastItems = sortedList;
                            walletAdapter = new WonItemsAdapter(view.getContext(), pastItems);
                            recyclerView.setAdapter(walletAdapter);

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

    class sortTime2 implements Comparator<WonItem> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(WonItem a, WonItem b) {
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

    class WonItemsAdapter extends RecyclerView.Adapter<WonItemsAdapter.BidHistoryViewHolder> {

        Context context;
        ArrayList<WonItem> heroList;

        WonItemsAdapter(Context context, ArrayList<WonItem> heroList) {
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
            String pattern = "dd MMM yyyy HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            Date date = null;
            try {
                date = simpleDateFormat2.parse(heroList.get(position).getEnd_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.bidHistoryMedalImage.setVisibility(View.GONE);
            String dt = simpleDateFormat.format(date);
            holder.bidHistoryStartDate.setText(dt);
            holder.bidHistoryAmount.setText(getResources().getString(R.string.ruppesymbol) + heroList.get(position).getBidamount());
            Glide.with(context)
                    .load(heroList.get(position).getImage_url())
                    .into(holder.bidHistoryImage);
            if (heroList.get(position).getOrderplaced().equals("1")) {
                holder.bidHistoryRank.setVisibility(View.GONE);
            } else if (heroList.get(position).getOrderplaced().equals("null")) {
                holder.bidHistoryRank.setVisibility(View.VISIBLE);
                holder.bidHistoryRank.setText("Place order");
                holder.bidHistoryRank.setTextColor(getResources().getColor(R.color.DarkGreen, null));
                holder.bidHistoryRank.setBackground(getResources().getDrawable(R.drawable.timer_bg, null));

                holder.bidHistoryRank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = new PlaceOrderFragment();
                        Bundle b = new Bundle();
                        b.putSerializable("object", heroList.get(position));
                        b.putString("id", heroList.get(position).getId());
                        fragment.setArguments(b);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (heroList.size() == 0)
                progressBar.setVisibility(View.GONE);
            return heroList.size();
        }

        class BidHistoryViewHolder extends RecyclerView.ViewHolder {
            ImageView bidHistoryImage;
            TextView bidHistoryTitle;
            TextView bidHistoryStartDate;
            TextView bidHistoryAmount;
            TextView bidHistoryRank;
            ImageView bidHistoryMedalImage;

            BidHistoryViewHolder(View itemView) {
                super(itemView);
                bidHistoryMedalImage = itemView.findViewById(R.id.bidHistoryMedalImage);
                bidHistoryImage = itemView.findViewById(R.id.bidHistoryImage);
                bidHistoryAmount = itemView.findViewById(R.id.bidHistoryAmount);
                bidHistoryTitle = itemView.findViewById(R.id.bidHistoryTitle);
                bidHistoryStartDate = itemView.findViewById(R.id.bidHistoryStartDate);
                bidHistoryRank = itemView.findViewById(R.id.bidHistoryRank);
            }
        }
    }
}
