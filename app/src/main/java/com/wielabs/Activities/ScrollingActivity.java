package com.wielabs.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout;
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

public class ScrollingActivity extends AppCompatActivity {

    ArrayList<WalletTransaction> walletTransactions;
    RecyclerView recyclerView;
    int bal;
    Toolbar toolbar;
    SubtitleCollapsingToolbarLayout s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Transactions");
        s = findViewById(R.id.toolbar_layout);
        s.setCollapsedTitleGravity(View.FOCUS_RIGHT);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/checkbalance.php?id="+SharedPrefManager.getInstance(ScrollingActivity.this).getUser().getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            bal = Integer.parseInt(obj.getString("balance"));
                            //getSupportActionBar().setSubtitle("Balance"+bal);
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

        RequestQueue requestQueue = Volley.newRequestQueue(ScrollingActivity.this);
        requestQueue.add(stringRequest);

        setSupportActionBar(toolbar);
        toolbar.setSubtitle("yes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScrollingActivity.this, AddMoney.class));
                finish();
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//         });

        recyclerView = (RecyclerView) findViewById(R.id.walletTransactions);
        Drawable horizontalDivider = ContextCompat.getDrawable(ScrollingActivity.this, R.drawable.horizontal_divider);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        walletTransactions = new ArrayList<>();

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/showtransactions.php?id="+ SharedPrefManager.getInstance(ScrollingActivity.this).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            walletTransactions.clear();
                            JSONObject obj = new JSONObject(response);
                            Log.d("Transaction", response+"");
                            JSONArray heroArray = obj.getJSONArray("Transactions");
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                Log.d("Object", heroObject+"");
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
                            WalletAdapter walletAdapter = new WalletAdapter(ScrollingActivity.this, walletTransactions);

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

        RequestQueue requestQueue2 = Volley.newRequestQueue(ScrollingActivity.this);
        requestQueue2.add(stringRequest2);
    }

    class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder>{

        ArrayList<WalletTransaction> walletTransactions;
        Context context;

        WalletAdapter(Context context,ArrayList<WalletTransaction> walletTransactions){
            this.context = context;
            this.walletTransactions = walletTransactions;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.latest_transactions, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WalletAdapter.ViewHolder viewHolder, int i) {
            if(Integer.parseInt(walletTransactions.get(i).getValue()) > 0) {
                viewHolder.amount.setText("+ ₹" + walletTransactions.get(i).getValue());
                viewHolder.amount.setTextColor(getColor(R.color.green));
            }
            else {
                viewHolder.amount.setText("- ₹" + abs(Integer.parseInt(walletTransactions.get(i).getValue())));
                viewHolder.amount.setTextColor(Color.BLACK);
            }
            viewHolder.orderid.setText("Order id - "+walletTransactions.get(i).getOrderid());
            viewHolder.date.setText(walletTransactions.get(i).getDate());
            viewHolder.type.setText(walletTransactions.get(i).getDescription());

            Glide.with(ScrollingActivity.this)
                    .asBitmap()
                    .load(walletTransactions.get(i).getImage_url())
                    .into(viewHolder.c);
        }

        @Override
        public int getItemCount() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}