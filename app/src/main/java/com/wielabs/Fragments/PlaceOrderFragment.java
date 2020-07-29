package com.wielabs.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.wielabs.Models.WonItem;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOrderFragment extends Fragment {

    public PlaceOrderFragment() {
        // Required empty public constructor
    }

    String id;
    Button placeOrderButton;
    TextView placeOrderAddress;
    TextView subtotal, shippingcost, discount, total, wallet, insuffecientBalance;
    MaterialButton changeAddress;
    int bal;
    WonItem wonItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().findViewById(R.id.fabhome).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.bar).setVisibility(View.INVISIBLE);
        final View view = inflater.inflate(R.layout.fragment_place_order, container, false);
        subtotal = view.findViewById(R.id.orderDeliverySubtotal);
        shippingcost = view.findViewById(R.id.orderDeliveryShippingFees);
        discount = view.findViewById(R.id.orderDeliveryDiscount);
        total = view.findViewById(R.id.orderDeliveryTotal);
        changeAddress = view.findViewById(R.id.changeAddress);
        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddressFragment2()).addToBackStack(null).commit();
            }
        });
        wallet = view.findViewById(R.id.orderDeliveryPayment1);
        insuffecientBalance = view.findViewById(R.id.textInsuffecientBalance);
        placeOrderAddress = view.findViewById(R.id.orderDeliveryAddress);
        placeOrderAddress.setText(SharedPrefManager.getInstance(view.getContext()).getUser().getAddress());
        placeOrderButton = view.findViewById(R.id.placeOrderButton);
        id = getArguments().getString("id");
        wonItem = (WonItem) getArguments().getSerializable("object");

        subtotal.setText(getResources().getString(R.string.ruppesymbol) + wonItem.getBidamount());
        shippingcost.setText(getResources().getString(R.string.ruppesymbol) + "0");
        discount.setText(getResources().getString(R.string.ruppesymbol) + "0");
        total.setText(getResources().getString(R.string.ruppesymbol) + wonItem.getBidamount());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/checkbalance.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            bal = Integer.parseInt(obj.getString("balance"));
                            wallet.setText("Wallet Balance : ₹ " + String.format("%,d", bal));
                            if (bal > Integer.parseInt(wonItem.getBidamount())) {
                                insuffecientBalance.setVisibility(View.GONE);
                            } else {
                                insuffecientBalance.setVisibility(View.VISIBLE);
                            }
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

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (bal > Integer.parseInt(wonItem.getBidamount())) {
                    RequestQueue MyRequestQueue = Volley.newRequestQueue(view.getContext());
                    String url = "http://easyvela.esy.es/AndroidAPI/placeorder.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(); // <----enter your post url here
                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //This code is executed if the server responds, whether or not the response contains data.
                            //The String 'response' contains the server's response.
                            Toast.makeText(view.getContext(), "Order placed", Toast.LENGTH_SHORT).show();
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                        }
                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //This code is executed if there is an error.
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("productid", id);
                            params.put("status", "Order placed");
                            params.put("address", SharedPrefManager.getInstance(view.getContext()).getUser().getAddress());
                            return params;
                        }
                    };
                    MyRequestQueue.add(MyStringRequest);
                }
            }
        });

        return view;
    }
}
