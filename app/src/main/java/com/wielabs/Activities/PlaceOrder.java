package com.wielabs.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wielabs.Models.Orders;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlaceOrder extends AppCompatActivity {

    Orders c;
    Dialog dialog;
    EditText name, mobile, houseno, street, city, state, pincode;
    String address;
    int id;
    int bal;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        c = (Orders) getIntent().getSerializableExtra("order");
        TextView amount = findViewById(R.id.placeorderamount);
        amount.setText("₹"+c.getBidamount());
        name = (EditText) findViewById(R.id.nameplaceorder);
        houseno = (EditText) findViewById(R.id.housenoplaceorder);
        mobile = (EditText) findViewById(R.id.mobileplaceorder);
        street = (EditText) findViewById(R.id.streetplaceorder);
        state = (EditText) findViewById(R.id.stateplaceorder);
        city = (EditText) findViewById(R.id.cityplaceorder);
        pincode = (EditText) findViewById(R.id.pinplaceorder);

        Button placeBid = (Button) findViewById(R.id.placeOrder);
        placeBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(name.getText().toString())) {
                    name.setError("Please enter Name");
                    name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(mobile.getText().toString())) {
                    mobile.setError("Please enter mobile");
                    mobile.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(houseno.getText().toString())) {
                    houseno.setError("Please enter house no");
                    houseno.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(street.getText().toString())) {
                    street.setError("Enter a street");
                    street.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(city.getText().toString())) {
                    city.setError("Enter a password");
                    city.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(state.toString())) {
                    state.setError("Enter Address");
                    state.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pincode.toString())) {
                    pincode.setError("Enter Pincode");
                    pincode.requestFocus();
                    return;
                }

                address = name.getText().toString()+"\n"+
                        houseno.getText().toString()+"\n"+
                        street.getText().toString()+"\n"+
                        city.getText().toString()+"\n"+
                        state.getText().toString()+"\n"+
                        pincode.getText().toString()+"\n"+
                        mobile.getText().toString();

                id = SharedPrefManager.getInstance(PlaceOrder.this).getUser().getId();
                Log.d("Balance", SharedPrefManager.getInstance(view.getContext()).getUser().getId()+"");
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/checkbalance.php?id="+id,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    bal = Integer.parseInt(obj.getString("balance"));
                                    if(bal > Integer.parseInt(c.getBidamount())){
                                        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/updatewallet.php?id="+id+"&value="+Integer.parseInt(c.getBidamount())*-1+"&type=Paid to order "+c.getTitle()+"&image_url="+c.getImage_url(),
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        placeOrder();
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        //Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        RequestQueue requestQueue2 = Volley.newRequestQueue(PlaceOrder.this);
                                        requestQueue2.add(stringRequest2);
                                    }
                                    else
                                        Toast.makeText(PlaceOrder.this,"Insuffecient Balance, add money to wallet",Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(PlaceOrder.this);
                requestQueue.add(stringRequest);

                dialog = new Dialog(PlaceOrder.this);
                dialog.setContentView(R.layout.order_placed_dialog);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_bg));
                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(PlaceOrder.this, Home.class);
                        startActivity(i);
                        finish();
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PlaceOrder.this, Home.class));
        finish();
    }

    public void placeOrder(){

        RequestQueue MyRequestQueue = Volley.newRequestQueue(PlaceOrder.this);
        String url = "http://easyvela.esy.es/AndroidAPI/placeorder.php?id="+ SharedPrefManager.getInstance(PlaceOrder.this).getUser().getId(); // <----enter your post url here
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("productid", c.getId());
                params.put("status", "Order placed");
                params.put("address", address);
                return params;
            }
        };

        MyRequestQueue.add(MyStringRequest);
    }

}
