package com.wielabs.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

public class EmptyFragment extends Fragment {

    ProgressBar progressBar;
    ArrayList<PastProducts> cartItems;
    View view;
    RecyclerView cartList;
    String bid;
    BidHistoryAdapterAll adapterall;
    public ArrayList<WonItem> pastItems;
    TextView sortText;
    WonItemsAdapter walletAdapter;
    RadioButton mybids, wonbids;
    TextView mybidstext, wonbidstext;

    EmptyFragment(String bid) {
        this.bid = bid;
    }

    Dialog dialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().findViewById(R.id.bar).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.fabhome).setVisibility(View.VISIBLE);
        pastItems = new ArrayList<>();
        final View view = inflater.inflate(R.layout.bid_history_fragments, container, false);
        sortText = view.findViewById(R.id.wonbidstext);
        ImageView sortImage = view.findViewById(R.id.sortImage);
        cartList = view.findViewById(R.id.bidHistoryRecyclerView);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        cartList.addItemDecoration(dividerItemDecoration);
        cartList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        this.view = view;
        cartItems = new ArrayList<>();
        progressBar = view.findViewById(R.id.mybidsprogress);

        loadWonBids(view);
        loadMyBids(view);

        dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.sort_dialog);

        mybids = dialog.findViewById(R.id.radioMybids);
        mybids.setChecked(true);
        wonbids = dialog.findViewById(R.id.radioWon);
        mybidstext = dialog.findViewById(R.id.mybidsradiotext);
        mybidstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mybids.setChecked(true);
                wonbids.setChecked(false);
                cartList.setAdapter(adapterall);
                dialog.dismiss();
                sortText.setText("My bids");
            }
        });
        wonbidstext = dialog.findViewById(R.id.wonbidsradiotext);
        wonbidstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wonbids.setChecked(true);
                mybids.setChecked(false);
                cartList.setAdapter(walletAdapter);
                dialog.dismiss();
                sortText.setText("Won bids");
            }
        });

        sortText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
            }
        });

        sortImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
            }
        });


        return view;
    }

    void loadMyBids(final View view){
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
                                        heroObject.getString("image_url3"),
                                        heroObject.getString("firstname"),
                                        heroObject.getString("bidamount"),
                                        heroObject.getString("ids"),
                                        heroObject.getString("orderplaced")
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

    void loadWonBids(final View view){
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
                                        heroObject.getString("bidamount"), "Won", heroObject.getString("orderplaced"));
                                pastItems.add(c);
                            }
                            pastItems.sort(new sortTime2());
                            walletAdapter = new WonItemsAdapter(view.getContext(), pastItems);
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
        public void onBindViewHolder(@NonNull final BidHistoryViewHolder holder, final int position) {
            if (getItemCount() == 0) {
                progressBar.setVisibility(View.GONE);
            }
            Log.d("Orderplaced", heroList.get(position).getOrderplaced());
            if (Integer.parseInt(heroList.get(position).getWinnerid()) != SharedPrefManager.getInstance(context).getUser().getId() && heroList.get(position).getOrderplaced() == "null") {
                holder.bidHistoryRank.setText(heroList.get(position).getWinner());
                holder.bidHistoryRank.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                holder.bidHistoryRank.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (Integer.parseInt(heroList.get(position).getWinnerid()) == SharedPrefManager.getInstance(context).getUser().getId()) {
                if (heroList.get(position).getOrderplaced() == "null") {
                    holder.bidHistoryRank.setText("Place order");
                } else {
                    holder.bidHistoryRank.setText("Order placed");
                }
                holder.bidHistoryRank.setTextColor(getResources().getColor(R.color.DarkGreen, null));
                holder.bidHistoryRank.setBackgroundColor(getResources().getColor(R.color.LightGreen, null));
            }


            holder.bidHistoryRank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.bidHistoryRank.getText().equals("Place order")) {
                        for (int i = 0; i < pastItems.size(); i++) {
                            if (pastItems.get(i).getId().equals(heroList.get(position).getId())) {
                                Fragment fragment = new PlaceOrderFragment();
                                Bundle b = new Bundle();
                                b.putSerializable("object", pastItems.get(i));
                                b.putString("id", pastItems.get(i).getId());
                                fragment.setArguments(b);
                                getParentFragment().getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                            }
                        }
                    }
                }
            });

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
        public void onBindViewHolder(@NonNull final BidHistoryViewHolder holder, final int position) {
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
            if (pastItems.get(position).getOrderplaced().equals("1")) {
                holder.bidHistoryRank.setVisibility(View.GONE);
            } else {
                holder.bidHistoryRank.setText("Place order");
                holder.bidHistoryRank.setTextColor(getResources().getColor(R.color.DarkGreen, null));
                holder.bidHistoryRank.setBackground(getResources().getDrawable(R.drawable.timer_bg, null));
            }

            holder.bidHistoryRank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new PlaceOrderFragment();
                    Bundle b = new Bundle();
                    b.putSerializable("object", heroList.get(position));
                    b.putString("id", heroList.get(position).getId());
                    fragment.setArguments(b);
                    getParentFragment().getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            if(heroList.size() == 0)
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
