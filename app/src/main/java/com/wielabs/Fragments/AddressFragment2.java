package com.wielabs.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressFragment2 extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    RecyclerView addressRecyclerview;
    ExtendedFloatingActionButton addnewaddress;
    int addresscount = 0;
    String a1, a2, a3, a4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_address, container, false);
        addressRecyclerview = view.findViewById(R.id.addressrecyclerview);
        addressRecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));
        addnewaddress = view.findViewById(R.id.addnewaddress);

        addnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addresscount > 4) {
                    Toast.makeText(view.getContext(), "No more addresses can be added", Toast.LENGTH_SHORT).show();
                } else if (a2.equals("null")) {
                    Fragment fragment = new EditAddressFragment2();
                    Bundle b = new Bundle();
                    b.putInt("addressno", 2);
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                } else if (a1.equals("null")) {
                    Fragment fragment = new EditAddressFragment2();
                    Bundle b = new Bundle();
                    b.putInt("addressno", 1);
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                } else if (a3.equals("null")) {
                    Fragment fragment = new EditAddressFragment2();
                    Bundle b = new Bundle();
                    b.putInt("addressno", 3);
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                } else if (a4.equals("null")) {
                    Fragment fragment = new EditAddressFragment2();
                    Bundle b = new Bundle();
                    b.putInt("addressno", 4);
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                }
            }
        });

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
                                Log.d("address", heroObject.getString("address2"));
                                a1 = heroObject.getString("address");
                                a2 = heroObject.getString("address2");
                                a3 = heroObject.getString("address3");
                                a4 = heroObject.getString("address4");


                                if (!heroObject.getString("address").equals("") && !heroObject.getString("address").equals("null")) {
                                    addressList.add(new AddressItem(heroObject.getString("address"), 1));
                                    addresscount++;
                                }

                                if (!heroObject.getString("address2").equals("") && !heroObject.getString("address2").equals("null")) {
                                    addressList.add(new AddressItem(heroObject.getString("address2"), 2));
                                    addresscount++;
                                }

                                if (!heroObject.getString("address3").equals("") && !heroObject.getString("address3").equals("null")) {
                                    addressList.add(new AddressItem(heroObject.getString("address3"), 3));
                                    addresscount++;
                                }

                                if (!heroObject.getString("address4").equals("") && !heroObject.getString("address4").equals("null")) {
                                    addressList.add(new AddressItem(heroObject.getString("address4"), 4));
                                    addresscount++;
                                }

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
        public int mSelectedItem = 0;

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
        public void onBindViewHolder(@NonNull BidHistoryViewHolder holder, final int position) {
            holder.address.setText(heroList.get(position).getAddress());
            holder.radioButton.setChecked(position == mSelectedItem);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestQueue MyRequestQueue = Volley.newRequestQueue(view.getContext());
                    String url = "http://easyvela.esy.es/AndroidAPI/addaddress.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(); // <----enter your post url here
                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //This code is executed if the server responds, whether or not the response contains data.
                            //The String 'response' contains the server's response.
                            SharedPrefManager.getInstance(context).setKeyAddress(null);
                            Fragment frg = null;
                            frg = getFragmentManager().findFragmentById(R.id.fragment_container);
                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(frg);
                            ft.attach(frg);
                            ft.commit();
                        }
                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //This code is executed if there is an error.
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("address", "null");
                            params.put("addressno", heroList.get(position).getAddressno() + "");
                            return params;
                        }
                    };
                    MyRequestQueue.add(MyStringRequest);
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new EditAddressFragment2();
                    Bundle b = new Bundle();
                    b.putInt("addressno", heroList.get(position).getAddressno());
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
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

            BidHistoryViewHolder(final View itemView) {
                super(itemView);
                radioButton = itemView.findViewById(R.id.radioAddress);
                address = itemView.findViewById(R.id.addressfield);
                edit = itemView.findViewById(R.id.editAddress);
                delete = itemView.findViewById(R.id.deleteAddress);

                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSelectedItem = getAdapterPosition();
                        SharedPrefManager.getInstance(itemView.getContext()).setKeyAddress(heroList.get(getAdapterPosition()).getAddress());
                        notifyDataSetChanged();
                    }
                };

                radioButton.setOnClickListener(clickListener);
                itemView.setOnClickListener(clickListener);
            }
        }
    }

    class AddressItem {

        String address;
        int addressno;

        public AddressItem(String address, int addressno) {
            this.address = address;
            this.addressno = addressno;
        }

        public String getAddress() {
            return address;
        }

        public int getAddressno() {
            return addressno;
        }
    }
}
