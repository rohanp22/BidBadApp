package com.wielabs.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.wielabs.Models.Orders;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.Activities.PlaceOrder;
import com.wielabs.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.sql.Types.NULL;

public class MyOrdersFragment extends Fragment {

    public ArrayList<Orders> pastItems;
    ListView pastList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_myorders, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pastItems = new ArrayList<>();
        loadList(view);
    }


    void loadList(final View view){
        pastList = (ListView) view.findViewById(R.id.Myorderslist);

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
                                        heroObject.getString("currentid"),
                                        heroObject.getString("image_url"),
                                        heroObject.getString("title"),
                                        heroObject.getString("endtime"),
                                        heroObject.getString("mrp"),
                                        heroObject.getString("sp"),
                                        heroObject.getString("description"),
                                        heroObject.getString("bidamount"),"Won", heroObject.getString("orderplaced"));
                                pastItems.add(c);
                            }
                            pastItems.sort(new sortTime());
                            PastAdapter walletAdapter = new PastAdapter(view.getContext(), pastItems);
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

    class PastAdapter extends ArrayAdapter<Orders> {

        List<Orders> CartList;
        Context mContext;

        public PastAdapter(Context context, List<Orders> heroList) {
            super(context, R.layout.transaction_layout, heroList);

            mContext = context;
            this.CartList = heroList;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            //getting the view
            View view = layoutInflater.inflate(R.layout.order_layout, null, false);

            //getting the view elements of the list from the view

            ImageView image = (ImageView) view.findViewById(R.id.imageorder);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent it = new Intent(getActivity().getApplicationContext(), ProductDescription.class);
//                    it.putExtra("id", HomeFragment.current_products.get(position));
//                    Log.d("Position", position + "");
//                    startActivity(it);
                }
            });
            TextView title = (TextView) view.findViewById(R.id.titleorder);
            TextView amount = (TextView) view.findViewById(R.id.yourBidOrder);
            amount.setText(pastItems.get(position).getBidamount());
            TextView status = (TextView) view.findViewById(R.id.statusOrder);
            status.setText("Status: "+pastItems.get(position).getStatus());
            title.setText(pastItems.get(position).getTitle());
            Button claim = (Button) view.findViewById(R.id.claimButton);

            if(pastItems.get(position).getOrderplaced().equals("1")){
                claim.setVisibility(View.GONE);
                status.setText("Status: Order placed");
            }

//            if(pastItems.get(position).getPlaced().equals("1")){
//                claim.setVisibility(View.GONE);
//            }

            claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), PlaceOrder.class);
                    i.putExtra("order", pastItems.get(position));
                    startActivity(i);
                    getActivity().finish();
                }
            });

            Glide.with(mContext)
                    .asBitmap()
                    .load(pastItems.get(position).getImage_url())
                    .into(image);

            return view;
        }
    }

    class sortTime implements Comparator<Orders> {
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
}