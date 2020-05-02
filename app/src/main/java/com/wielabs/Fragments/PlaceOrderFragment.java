package com.wielabs.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().findViewById(R.id.fabhome).setVisibility(View.GONE);
        getActivity().findViewById(R.id.bar).setVisibility(View.GONE);
        final View view = inflater.inflate(R.layout.fragment_place_order, container, false);
        placeOrderAddress = view.findViewById(R.id.orderDeliveryAddress);
        placeOrderAddress.setText(SharedPrefManager.getInstance(view.getContext()).getUser().getAddress());
        placeOrderButton = view.findViewById(R.id.placeOrderButton);
        id = getArguments().getString("id");

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                RequestQueue MyRequestQueue = Volley.newRequestQueue(view.getContext());
//                    String url = "http://easyvela.esy.es/AndroidAPI/placeorder.php?id="+ SharedPrefManager.getInstance(view.getContext()).getUser().getId(); // <----enter your post url here
//                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            //This code is executed if the server responds, whether or not the response contains data.
//                            //The String 'response' contains the server's response.
//                            Toast.makeText(view.getContext(), "Order placed", Toast.LENGTH_SHORT).show();
//                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//                        }
//                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            //This code is executed if there is an error.
//                        }
//                    }) {
//                        protected Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("productid", id);
//                            params.put("status", "Order placed");
//                            params.put("address", SharedPrefManager.getInstance(view.getContext()).getUser().getAddress());
//                            return params;
//                        }
//                    };
//                    MyRequestQueue.add(MyStringRequest);
            }
        });

        return view;
    }
}
