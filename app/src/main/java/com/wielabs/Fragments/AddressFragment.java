package com.wielabs.Fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    RecyclerView addressRecyclerview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_address, container, false);
        addressRecyclerview = view.findViewById(R.id.addressrecyclerview);
        addressRecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));

        final ArrayList<AddressItem> addressList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getaddresses.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Address");
                            Log.d("Address", response);
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                Log.d("address", heroObject.getString("address"));
                                if(!heroObject.getString("address").equals(""))
                                    addressList.add(new AddressItem(heroObject.getString("address")));

                                if(!heroObject.getString("address2").equals(""))
                                    addressList.add(new AddressItem(heroObject.getString("address2")));

                                if(!heroObject.getString("address3").equals(""))
                                    addressList.add(new AddressItem(heroObject.getString("address3")));

                                if(!heroObject.getString("address4").equals(""))
                                    addressList.add(new AddressItem(heroObject.getString("address4")));

                            }

                            AddressAdapter a = new AddressAdapter(view.getContext(), addressList);
                            addressRecyclerview.setAdapter(a);

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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.divider));
        addressRecyclerview.addItemDecoration(dividerItemDecoration);

        return view;
    }

    class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.BidHistoryViewHolder> {

        Context context;
        ArrayList<AddressItem> heroList;
        public int mSelectedItem = -1;

        AddressAdapter(Context context, ArrayList<AddressItem> heroList) {
            this.context = context;
            this.heroList = heroList;
        }

        @NonNull
        @Override
        public AddressAdapter.BidHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AddressAdapter.BidHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BidHistoryViewHolder holder, int position) {
            holder.address.setText(heroList.get(position).getAddress());
            holder.radioButton.setChecked(position == mSelectedItem);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditAddressFragment()).addToBackStack(null).commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return heroList.size();
        }

        class BidHistoryViewHolder extends RecyclerView.ViewHolder {

            TextView address;
            RadioButton radioButton;
            ImageView edit, delete;

            BidHistoryViewHolder(View itemView) {
                super(itemView);
                radioButton = itemView.findViewById(R.id.radioAddress);
                address = itemView.findViewById(R.id.addressfield);
                edit = itemView.findViewById(R.id.editAddress);
                delete = itemView.findViewById(R.id.deleteAddress);

                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSelectedItem = getAdapterPosition();
                        notifyDataSetChanged();
                    }
                };

                radioButton.setOnClickListener(clickListener);
                itemView.setOnClickListener(clickListener);
            }
        }
    }

    class AddressItem{

        String address;

        public AddressItem(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }
    }
}
