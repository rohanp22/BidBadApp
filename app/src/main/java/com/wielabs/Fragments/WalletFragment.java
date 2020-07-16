package com.wielabs.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.wielabs.Activities.WalletTransaction;
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

import static java.lang.StrictMath.abs;

public class WalletFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<WalletTransaction> walletTransactions;

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
        final View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        // Inflate the layout for this fragment

        recyclerView = (RecyclerView) view.findViewById(R.id.walletTransactions);
        Drawable horizontalDivider = ContextCompat.getDrawable(view.getContext(), R.drawable.horizontal_divider);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        horizontalDecoration.setDrawable(horizontalDivider);
        //recyclerView.addItemDecoration(horizontalDecoration);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        walletTransactions = new ArrayList<>();

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/showtransactions.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            walletTransactions.clear();
                            JSONObject obj = new JSONObject(response);
                            Log.d("Transaction", response + "");
                            JSONArray heroArray = obj.getJSONArray("Transactions");
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                Log.d("Object", heroObject + "");
                                Log.d("Object", heroObject.getString("orderid"));
                                WalletTransaction c = new WalletTransaction(
                                        heroObject.getString("orderid"),
                                        heroObject.getString("amount"),
                                        heroObject.getString("date"),
                                        heroObject.getString("type"),
                                        heroObject.getString("image_url"));
                                walletTransactions.add(c);

                                //ListView l = (ListView) findViewById(R.id.transactionList);
                            }
                            walletTransactions.sort(new sortTime2());
                            WalletAdapter walletAdapter = new WalletAdapter(view.getContext(), walletTransactions);

                            recyclerView.setAdapter(walletAdapter);
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(view.getContext());
        requestQueue2.add(stringRequest2);

        return view;
    }

    class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {

        ArrayList<WalletTransaction> walletTransactions;
        Context context;

        WalletAdapter(Context context, ArrayList<WalletTransaction> walletTransactions) {
            this.context = context;
            this.walletTransactions = walletTransactions;
        }

        @NonNull
        @Override
        public WalletAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.latest_transactions, viewGroup, false);

            return new WalletAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WalletAdapter.ViewHolder viewHolder, int i) {
            if (Integer.parseInt(walletTransactions.get(i).getValue()) > 0) {
                viewHolder.amount.setText("+ ₹" + walletTransactions.get(i).getValue());
                viewHolder.amount.setTextColor(getContext().getColor(R.color.green));
            } else {
                viewHolder.amount.setText("- ₹" + abs(Integer.parseInt(walletTransactions.get(i).getValue())));
                viewHolder.amount.setTextColor(Color.BLACK);
            }
            viewHolder.orderid.setText("Order id - " + walletTransactions.get(i).getOrderid());


            String pattern = "d MMM yy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMMM d, yyyy, h:mm a");
            Date date = null;
            try {
                date = simpleDateFormat2.parse(walletTransactions.get(i).getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(date);
            String dt = simpleDateFormat.format(date);
            Log.d("dt", dt);
            String da = getDisplayableTime(date.getTime());
            if (da == null) {
                viewHolder.date.setText(dt);
            } else {
                viewHolder.date.setText(da);
            }

            //viewHolder.date.setText(walletTransactions.get(i).getDate());
            viewHolder.type.setText(walletTransactions.get(i).getDescription());

            Glide.with(getContext())
                    .asBitmap()
                    .load(walletTransactions.get(i).getImage_url())
                    .into(viewHolder.c);
        }

        public String getDisplayableTime(long delta) {
            long difference = 0;
            Long mDate = java.lang.System.currentTimeMillis();

            if (mDate > delta) {
                difference = mDate - delta;
                final long seconds = difference / 1000;
                final long minutes = seconds / 60;
                final long hours = minutes / 60;
                final long days = hours / 24;
                final long months = days / 31;
                final long years = days / 365;

                if (seconds < 0) {
                    return "not yet";
                } else if (seconds < 60) {
                    return seconds == 1 ? "one second ago" : seconds + " seconds ago";
                } else if (seconds < 120) {
                    return "a minute ago";
                } else if (seconds < 2700) // 45 * 60
                {
                    return minutes + " minutes ago";
                } else if (seconds < 5400) // 90 * 60
                {
                    return "an hour ago";
                } else if (seconds < 86400) // 24 * 60 * 60
                {
                    return hours + " hours ago";
                } else if (seconds < 172800) // 48 * 60 * 60
                {
                    return "yesterday";
                }
            }
            return null;
        }

        @Override
        public int getItemCount() {
            if (walletTransactions.size() > 5)
                return 4;
            else
                return walletTransactions.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView amount;
            TextView type;
            TextView date;
            TextView orderid;
            ImageView c;

            public ViewHolder(View itemView) {
                super(itemView);
                c = itemView.findViewById(R.id.imageTransaction);
                amount = itemView.findViewById(R.id.value);
                date = itemView.findViewById(R.id.date);
                type = itemView.findViewById(R.id.transactionDescription);
                orderid = itemView.findViewById(R.id.orderId);
            }
        }
    }

    class sortTime2 implements Comparator<WalletTransaction> {
        public int compare(WalletTransaction a, WalletTransaction b) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d,yyyy, h:mm a");
            Date a1 = null, b1 = null;
            try {
                a1 = simpleDateFormat.parse(a.getDate());
                b1 = simpleDateFormat.parse(b.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return b1.compareTo(a1);
        }
    }

}
