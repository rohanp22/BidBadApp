package com.wielabs.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wielabs.Activities.ProductDescription;
import com.wielabs.Models.CartItems;
import com.bumptech.glide.Glide;
import com.wielabs.R;
import com.wielabs.Others.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class MyBidsFragment extends Fragment {

    private ArrayList<CartItems> cartItems;
    private RecyclerView cartList;
    private View view;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bids, container, false);
        this.view = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cartItems = new ArrayList<>();
        loadList(view);
    }

    void loadList(final View view) {
        cartList = view.findViewById(R.id.myBidsList);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getmyongoingbids.php?id=" + SharedPrefManager.getInstance(getActivity()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            cartItems.clear();
                            JSONObject obj = new JSONObject(response);

                            JSONArray heroArray = obj.getJSONArray("Bids");
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                Log.d("Object", heroObject.getString("image_url") + "");
                                Date a1 = simpleDateFormat.parse(heroObject.getString("endtime"));
                                Log.d("Now", a1.compareTo(new java.sql.Date(System.currentTimeMillis())) + "");

                                CartItems c = new CartItems(
                                        heroObject.getString("image_url"),
                                        heroObject.getString("bidamount"),
                                        heroObject.getString("endtime"),
                                        heroObject.getString("title"),
                                        heroObject.getString("currentid"));

                                if (a1.compareTo(new java.sql.Date(System.currentTimeMillis())) > 0)
                                    cartItems.add(c);
                            }

                            cartItems.sort(new sortTime());

                            if(cartItems.size() == 0){
                                view.findViewById(R.id.nobids).setVisibility(View.VISIBLE);
                            } else {
                                view.findViewById(R.id.nobids).setVisibility(View.GONE);
                            }

                            BidsAdapter walletAdapter = new BidsAdapter(view.getContext(), cartItems);

                            Drawable horizontalDivider = ContextCompat.getDrawable(view.getContext(), R.drawable.horizontal);
                            DividerItemDecoration horizontalDecoration = new DividerItemDecoration(cartList.getContext(),
                                    DividerItemDecoration.VERTICAL);
                            horizontalDecoration.setDrawable(horizontalDivider);
                            cartList.addItemDecoration(horizontalDecoration);
                            cartList.setLayoutManager(new LinearLayoutManager(view.getContext()));
                            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                            cartList.setAdapter(walletAdapter);

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

    class BidsAdapter extends RecyclerView.Adapter<BidsAdapter.ViewHolder> {

        ArrayList<CartItems> cartList;
        Context mContext;
        Date startDate1;

        public BidsAdapter(Context context, ArrayList<CartItems> heroList) {
            mContext = context;
            this.cartList = heroList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mybid_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final BidsAdapter.ViewHolder holder, final int position) {

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductDescription ldf = new ProductDescription();
                    Bundle args = new Bundle();
                    args.putString("YourKey", cartItems.get(position).getId());
                    args.putString("show", "no");
                    args.putString("im", cartItems.get(position).getImage_url());
                    ldf.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, ldf).addToBackStack(null).commit();
                }
            });

            holder.amount.setText("Your bids: "+cartItems.get(position).getMybid());

            holder.title.setText(cartItems.get(position).getTitleCart());

            long diff = 0;

            final long secondsInMilli = 1000;
            final long minutesInMilli = secondsInMilli * 60;
            final long hoursInMilli = minutesInMilli * 60;
            final long daysInMilli = hoursInMilli * 24;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

            try {
                startDate1 = simpleDateFormat.parse(cartItems.get(position).getStatus());
                diff = startDate1.getTime() - System.currentTimeMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            final long finalDiff = diff;

            new CountDownTimer(finalDiff, 1000) {

                public void onTick(long millisUntilFinished) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

                    try {
                        startDate1 = simpleDateFormat.parse(cartItems.get(position).getStatus());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long different = startDate1.getTime() - System.currentTimeMillis();

                    if (different > 0) {

                        final long elapsedDays = different / daysInMilli;

                        final long elapsedHours = different / hoursInMilli;
                        different = different % hoursInMilli;

                        final long elapsedMinutes = different / minutesInMilli;
                        different = different % minutesInMilli;

                        final long elapsedSeconds = different / secondsInMilli;

                        String curtime = elapsedHours + "hr " + String.format("%02d", elapsedMinutes) + "m " + String.format("%02d", elapsedSeconds) + "s";

                        holder.date.setText("Ends in: "+curtime);

                    } else {

                    }
                }

                public void onFinish() {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyBidsFragment()).commit();
                }
            }.start();

            Glide.with(mContext)
                    .asBitmap()
                    .load(cartItems.get(position).getImage_url())
                    .into(holder.image);
        }

        @Override
        public int getItemCount() {
            return cartList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView title;
            TextView amount;
            TextView date;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.titleMyBid);
                amount = (TextView) itemView.findViewById(R.id.yourBid);
                image = (ImageView) itemView.findViewById(R.id.myBidImage);
                date = (TextView) itemView.findViewById(R.id.status);
            }
        }
    }

    class sortTime implements Comparator<CartItems> {

        public int compare(CartItems a, CartItems b) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            Date a1 = null, b1 = null;
            try {
                a1 = simpleDateFormat.parse(a.getStatus());
                b1 = simpleDateFormat.parse(b.getStatus());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return a1.compareTo(b1);
        }
    }
}
