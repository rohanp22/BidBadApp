package com.wielabs.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import java.util.HashMap;
import java.util.Map;

public class EditAddressFragment2 extends Fragment {

    private EditText name;
    private EditText houseno;
    private EditText area;
    private EditText city;
    private EditText state;
    private EditText pin;
    private EditText country;
    private EditText email;
    private EditText mobile;
    private Button saveAddress;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_address_fragment, container, false);

        BottomAppBar b = getActivity().findViewById(R.id.bar);
        b.setVisibility(View.GONE);

        FloatingActionButton f = getActivity().findViewById(R.id.fabhome);
//        getActivity().findViewById(R.id.shadowview).setVisibility(View.GONE);
        f.setVisibility(View.GONE);

        name = view.findViewById(R.id.nameedittext);
        houseno = view.findViewById(R.id.addressline1);
        area = view.findViewById(R.id.addressline2);
        saveAddress = view.findViewById(R.id.saveaddress);
        city = view.findViewById(R.id.addressCity);
        state = view.findViewById(R.id.addressState);
        pin = view.findViewById(R.id.addresspin);
        country = view.findViewById(R.id.addressCountry);
        email = view.findViewById(R.id.addressemail);
        mobile = view.findViewById(R.id.mobile_no);

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (TextUtils.isEmpty(name.getText())) {
                    name.setError("Enter name");
                } else if (TextUtils.isEmpty(houseno.getText())) {
                    houseno.setError("Enter House no");
                } else if (TextUtils.isEmpty(city.getText())) {
                    city.setError("Enter City name");
                } else if (TextUtils.isEmpty(state.getText())) {
                    state.setError("Enter State");
                } else if (TextUtils.isEmpty(country.getText())) {
                    country.setError("Enter Country");
                } else if (TextUtils.isEmpty(area.getText())) {
                    area.setError("Enter Area");
                } else if (TextUtils.isEmpty(email.getText())) {
                    email.setError("Enter Email");
                } else if (TextUtils.isEmpty(mobile.getText())) {
                    mobile.setError("Enter Mobile");
                } else {
                    final String address = name.getText() + "\n" + houseno.getText() + "\n" + area.getText() + "\n" + city.getText() + "\n" + state.getText() + "\n" + pin.getText() + "\n" + country.getText() + "\n" + "Mobileno : +91" + mobile.getText();

                    RequestQueue MyRequestQueue = Volley.newRequestQueue(view.getContext());
                    String url = "http://easyvela.esy.es/AndroidAPI/addaddress.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(); // <----enter your post url here
                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //This code is executed if the server responds, whether or not the response contains data.
                            //The String 'response' contains the server's response.
                            SharedPrefManager.getInstance(view.getContext()).setKeyAddress(address);
                            getFragmentManager().popBackStack();
                        }
                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //This code is executed if there is an error.
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("address", address);
                            params.put("addressno", getArguments().getInt("addressno") + "");
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
