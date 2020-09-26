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
import com.wielabs.Models.Order;
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

public class MyOrdersFragment extends Fragment {

    public ArrayList<Order> pastItems;
    RecyclerView pastList;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_myorders, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarOrders);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pastItems = new ArrayList<>();
        loadList(view);
    }


    void loadList(final View view){
        pastList = (RecyclerView) view.findViewById(R.id.Myorderslist);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        pastList.addItemDecoration(dividerItemDecoration);
        pastList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/orders.php?id="+ SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            pastItems.clear();
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Bids");
                            Log.d(" Orders", response);
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                Order c = new Order(
                                        heroObject.getString("title"),
                                        heroObject.getString("bidamount"),
                                        heroObject.getString("date"),
                                        heroObject.getString("image_url"),
                                        heroObject.getString("mrp"),
                                        heroObject.getString("sp"),
                                        heroObject.getString("oid"),
                                        heroObject.getString("address"));
                                pastItems.add(c);
                            }
                            OrderAdapter walletAdapter = new OrderAdapter(view.getContext(), pastItems);
                            pastList.setAdapter(walletAdapter);
                            view.findViewById(R.id.progressBarOrders).setVisibility(View.GONE);
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

    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.BidHistoryViewHolder> {

        Context context;
        ArrayList<Order> heroList;

        OrderAdapter(Context context, ArrayList<Order> heroList){
            this.context = context;
            this.heroList = heroList;
        }

        @NonNull
        @Override
        public OrderAdapter.BidHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OrderAdapter.BidHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BidHistoryViewHolder holder, int position) {
            holder.orderId.setText("Orderid : " + heroList.get(position).getOid());
            holder.orderTitle.setText(heroList.get(position).getName());
            holder.orderPlaceDate.setText(heroList.get(position).getDate());

            Glide.with(context)
                    .load(heroList.get(position).getImage_url())
                    .into(holder.orderImage);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return heroList.size();
        }

        class BidHistoryViewHolder extends RecyclerView.ViewHolder {
            ImageView orderImage;
            TextView orderTitle;
            TextView orderPlaceDate;
            TextView orderId;

            BidHistoryViewHolder(View itemView) {
                super(itemView);
                orderId = itemView.findViewById(R.id.summaryOrderId);
                orderImage = itemView.findViewById(R.id.summaryOrderImage);
                orderTitle = itemView.findViewById(R.id.summaryOrderTitle);
                //orderAmount = itemView.findViewById(R.id.bidHistoryTitle);
                orderPlaceDate = itemView.findViewById(R.id.summaryOrderDate);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderSummary()).addToBackStack(null).commit();
                    }
                });
            }
        }
    }

    class sortTime implements Comparator<WonItem> {
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
}