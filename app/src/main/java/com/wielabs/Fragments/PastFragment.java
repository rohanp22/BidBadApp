package com.wielabs.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wielabs.Models.PastProducts;
import com.bumptech.glide.Glide;
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

public class PastFragment extends Fragment {

    public ArrayList<PastProducts> pastItems, cartItems;
    RecyclerView pastList, cartList;
    Button all, mybids;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_past, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pastItems = new ArrayList<>();
        cartItems = new ArrayList<>();
        pastList = (RecyclerView) view.findViewById(R.id.pastItemsList);

        all = view.findViewById(R.id.all);
        mybids = view.findViewById(R.id.onlymybids);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadList(view);
            }
        });
        mybids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMyBids(view);
            }
        });
        loadList(view);
        view.findViewById(R.id.progressBarPast).setVisibility(View.GONE);
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
                                        heroObject.getString("image_url3")
                                );
                                if (!(a1.compareTo(new java.sql.Date(System.currentTimeMillis())) > 0))
                                    cartItems.add(c);
                            }
                            cartItems.sort(new sortTime());
                            PastAdapterRecycler walletAdapter = new PastAdapterRecycler(view.getContext(), cartItems);
                            pastList.setAdapter(walletAdapter);
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

    void loadList(final View view){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/pastproducts.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            pastItems.clear();
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
                                pastItems.add(c);
                            }
                            pastItems.sort(new sortTime());
                            pastList.setLayoutManager(new LinearLayoutManager(view.getContext()));
                            PastAdapterRecycler walletAdapter = new PastAdapterRecycler(view.getContext(), pastItems);
                            pastList.setAdapter(walletAdapter);
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
}

class PastAdapterRecycler extends RecyclerView.Adapter<PastAdapterRecycler.ViewHolder>{

    ArrayList<PastProducts> CartList;
    Context mContext;

    public PastAdapterRecycler(Context context, ArrayList<PastProducts> heroList) {

        mContext = context;
        this.CartList = heroList;
    }

    BidHistoryAdapter adapter;
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_product, parent, false);
        this.adapter = new BidHistoryAdapter(mContext, mobileArray);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastAdapterRecycler.ViewHolder holder, int position) {
        holder.title.setText(CartList.get(position).getTitle());

        boolean isExpanded = CartList.get(position).isExpanded();

        holder.sp.setText(CartList.get(position).getSp());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Yes", Toast.LENGTH_LONG).show();
            }
        });
        Log.d("Title", CartList.get(position).getTitle());
        Glide.with(mContext)
                .load(CartList.get(position).getImage_url())
                .into(holder.image);
    }


    @Override
    public int getItemCount() {
        return CartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, sp;
        RelativeLayout rel;
        //RecyclerView listView;
        //RelativeLayout expandedLayout;
        //RelativeLayout showHistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //listView = itemView.findViewById(R.id.bidHistory);
            image = itemView.findViewById(R.id.bidImage);
            title = itemView.findViewById(R.id.bidTitle);
            sp = itemView.findViewById(R.id.bidPrice);
            rel = itemView.findViewById(R.id.relative);
            //showHistory = itemView.findViewById(R.id.showHistory);
            //expandedLayout = itemView.findViewById(R.id.expandedLayout);

//            showHistory.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    PastProducts movie = CartList.get(getAdapterPosition());
//                    movie.setExpanded(!movie.isExpanded());
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });
        }
    }
}

class BidHistoryAdapter extends RecyclerView.Adapter<BidHistoryAdapter.ViewHolder>{

    Context context;
    String[] bids;

    public BidHistoryAdapter(Context context, String[] bids) {
        this.bids = bids;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bid_history_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BidHistoryAdapter.ViewHolder holder, int position) {
        holder.t.setText(bids[position]);
    }

    @Override
    public int getItemCount() {
        return bids.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView t;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t = itemView.findViewById(R.id.label);
        }
    }
}


