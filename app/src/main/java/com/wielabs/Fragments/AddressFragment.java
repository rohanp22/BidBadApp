package com.wielabs.Fragments;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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

import java.util.HashMap;
import java.util.Map;

public class AddressFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    RecyclerView addressRecyclerview;
    ExtendedFloatingActionButton addnewaddress;
    ConstraintLayout address1, address2, address3, address4;
    TextView addressField1, addressField2, addressField3, addressField4;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    ImageView delete1, delete2, delete3, delete4;
    ImageView edit1, edit2, edit3, edit4;
    int addresscount = 0;
    String a1, a2, a3, a4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_address, container, false);
        addressRecyclerview = view.findViewById(R.id.addressrecyclerview);
        addressRecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));
        addnewaddress = view.findViewById(R.id.addnewaddress);

        address1 = view.findViewById(R.id.address1);
        address2 = view.findViewById(R.id.address2);
        address3 = view.findViewById(R.id.address3);
        address4 = view.findViewById(R.id.address4);

        delete1 = view.findViewById(R.id.deleteAddress1);
        delete2 = view.findViewById(R.id.deleteAddress2);
        delete3 = view.findViewById(R.id.deleteAddress3);
        delete4 = view.findViewById(R.id.deleteAddress4);

        edit1 = view.findViewById(R.id.editAddress1);
        edit2 = view.findViewById(R.id.editAddress2);
        edit3 = view.findViewById(R.id.editAddress3);
        edit4 = view.findViewById(R.id.editAddress4);

        addressField1 = view.findViewById(R.id.addressfield1);
        addressField2 = view.findViewById(R.id.addressfield2);
        addressField3 = view.findViewById(R.id.addressfield3);
        addressField4 = view.findViewById(R.id.addressfield4);

        radioButton1 = view.findViewById(R.id.radioAddress1);
        radioButton2 = view.findViewById(R.id.radioAddress2);
        radioButton3 = view.findViewById(R.id.radioAddress3);
        radioButton4 = view.findViewById(R.id.radioAddress4);

        delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddress(1, view);
            }
        });

        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddress(2, view);
            }
        });

        delete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddress(3, view);
            }
        });

        delete4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddress(4, view);
            }
        });

        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAddress(1);
            }
        });

        edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAddress(2);
            }
        });

        edit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAddress(3);
            }
        });

        edit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAddress(4);
            }
        });

        addnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addresscount > 4) {
                    Toast.makeText(view.getContext(), "No more addresses can be added", Toast.LENGTH_SHORT).show();
                } else if (a2.equals("null")) {
                    Fragment fragment = new EditAddressFragment();
                    Bundle b = new Bundle();
                    b.putInt("addressno", 2);
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                } else if (a1.equals("null")) {
                    Fragment fragment = new EditAddressFragment();
                    Bundle b = new Bundle();
                    b.putInt("addressno", 1);
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                } else if (a3.equals("null")) {
                    Fragment fragment = new EditAddressFragment();
                    Bundle b = new Bundle();
                    b.putInt("addressno", 3);
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                } else if (a4.equals("null")) {
                    Fragment fragment = new EditAddressFragment();
                    Bundle b = new Bundle();
                    b.putInt("addressno", 4);
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                }
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton1.setChecked(true);
                radioButton4.setChecked(false);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                SharedPrefManager.getInstance(view.getContext()).setKeyAddress(addressField1.getText().toString());
            }
        };

        radioButton1.setOnClickListener(clickListener);
        address1.setOnClickListener(clickListener);

        View.OnClickListener clickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton2.setChecked(true);
                radioButton1.setChecked(false);
                radioButton4.setChecked(false);
                radioButton3.setChecked(false);
                SharedPrefManager.getInstance(view.getContext()).setKeyAddress(addressField2.getText().toString());
            }
        };

        radioButton2.setOnClickListener(clickListener2);
        address2.setOnClickListener(clickListener2);

        View.OnClickListener clickListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton3.setChecked(true);
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                radioButton4.setChecked(false);
                SharedPrefManager.getInstance(view.getContext()).setKeyAddress(addressField3.getText().toString());
            }
        };

        radioButton3.setOnClickListener(clickListener3);
        address3.setOnClickListener(clickListener3);


        View.OnClickListener clickListener4 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton4.setChecked(true);
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                SharedPrefManager.getInstance(view.getContext()).setKeyAddress(addressField4.getText().toString());
            }
        };

        radioButton4.setOnClickListener(clickListener4);
        address4.setOnClickListener(clickListener4);

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
                                    addressField1.setText(a1);
                                    address1.setVisibility(View.VISIBLE);
                                    addresscount++;
                                }

                                if (!heroObject.getString("address2").equals("") && !heroObject.getString("address2").equals("null")) {
                                    addressField2.setText(a2);
                                    address2.setVisibility(View.VISIBLE);
                                    addresscount++;
                                }

                                if (!heroObject.getString("address3").equals("") && !heroObject.getString("address3").equals("null")) {
                                    addressField3.setText(a3);
                                    address3.setVisibility(View.VISIBLE);
                                    addresscount++;
                                }

                                if (!heroObject.getString("address4").equals("") && !heroObject.getString("address4").equals("null")) {
                                    addressField4.setText(a4);
                                    address4.setVisibility(View.VISIBLE);
                                    addresscount++;
                                }

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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        return view;
    }

    void deleteAddress(final int num, final View view) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(view.getContext());
        String url = "http://easyvela.esy.es/AndroidAPI/addaddress.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(); // <----enter your post url here
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddressFragment()).commit();
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
                params.put("addressno", num + "");
                return params;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }

    void editAddress(final int num) {
        Fragment fragment = new EditAddressFragment();
        Bundle b = new Bundle();
        b.putInt("addressno", num);
        fragment.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }
}
