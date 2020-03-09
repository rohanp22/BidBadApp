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
import android.widget.ArrayAdapter;
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
import com.wielabs.Models.CartItems;
import com.wielabs.Models.Current_Product;
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

public class PastFragment extends Fragment {

    public ArrayList<Current_Product> pastItems;
    RecyclerView pastList;

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
        loadList(view);
    }

    void loadList(final View view){
        pastList = (RecyclerView) view.findViewById(R.id.pastItemsList);

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
                                Current_Product c = new Current_Product(
                                        heroObject.getString("currentid"),
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
                            view.findViewById(R.id.progressBarPast).setVisibility(View.GONE);
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

//    class PastAdapter extends ArrayAdapter<Current_Product> {
//
//        ArrayList<Current_Product> CartList;
//        Context mContext;
//
//        public PastAdapter(Context context, ArrayList<Current_Product> heroList) {
//            super(context, R.layout.transaction_layout, heroList);
//
//            mContext = context;
//            this.CartList = heroList;
//        }
//
//        @NonNull
//        @Override
//        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
//            //getting the view
//            View view = layoutInflater.inflate(R.layout.past_product, null, false);
//
//            //getting the view elements of the list from the view
//
//            ImageView image = (ImageView) view.findViewById(R.id.bidImage);
//            image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    Intent it = new Intent(getActivity().getApplicationContext(), ProductDescription.class);
////                    it.putExtra("id", HomeFragment.current_products.get(position));
////                    Log.d("Position", position + "");
////                    startActivity(it);
//                }
//            });
//            TextView title = (TextView) view.findViewById(R.id.bidTitle);
//            TextView amount = (TextView) view.findViewById(R.id.bidPrice);
//            amount.setText(pastItems.get(position).getSp());
//            //TextView date = (TextView) view.findViewById(R.id.timepast);
//            //date.setText(pastItems.get(position).getEnd_date());
//            title.setText(pastItems.get(position).getTitle());
//
//            Glide.with(mContext)
//                    .asBitmap()
//                    .load(pastItems.get(position).getImage_url())
//                    .into(image);
//
//            return view;
//        }
//    }




    class sortTime implements Comparator<Current_Product> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Current_Product a, Current_Product b) {
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

    ArrayList<Current_Product> CartList;
    Context mContext;

    public PastAdapterRecycler(Context context, ArrayList<Current_Product> heroList) {

        mContext = context;
        this.CartList = heroList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastAdapterRecycler.ViewHolder holder, int position) {
        holder.title.setText(CartList.get(position).getTitle());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.bidImage);
            title = itemView.findViewById(R.id.bidTitle);
            sp = itemView.findViewById(R.id.bidPrice);
        }
    }
}
