package com.wielabs.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AddMoney extends Fragment implements PaymentResultListener {

    EditText e;
    final int UPI_PAYMENT = 0;
    int bal;
    String upiId = "bandaruharsha57@oksbi";
    String name = "BidBad";
    String note = "Add money to wallet";
    TextView t;
    int amount;
    View view;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_add_money, container, false);
        Checkout.preload(view.getContext());

//        view.findViewById(R.id.backAddMoney).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

        t = (TextView) view.findViewById(R.id.walletbalance);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/checkbalance.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            bal = Integer.parseInt(obj.getString("balance"));
                            t.setText("₹" + String.format("%,d", bal));
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

        e = (EditText) view.findViewById(R.id.enteramount);
        e.setEnabled(false);
        Button b1 = (Button) view.findViewById(R.id.add50);
        Button b2 = (Button) view.findViewById(R.id.add100);
        Button b3 = (Button) view.findViewById(R.id.add500);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //e.setText("50");
                e.setText("100");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //e.setText("100");
                e.setText("300");
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //e.setText("500");
                e.setText("500");
            }
        });

        Button b = (Button) view.findViewById(R.id.addmoney);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(e.getText().toString())) {
                    e.setError("Please enter Valid Amount");
                    e.requestFocus();
                    return;
                } else {
                    amount = Integer.parseInt(e.getText().toString());
                    payUsingUpi(e.getText().toString(), upiId, name, note);
                }

            }
        });
        return view;
    }

//    @Override
//    void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_money);
//
//    }

    void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        Log.d("Chooser", chooser.toString());

        String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
        int GOOGLE_PAY_REQUEST_CODE = 123;


//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);

        if (null != chooser.resolveActivity(getActivity().getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(getView().getContext(), "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("requestCode", requestCode + "");
        switch (123) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable()) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                int id = SharedPrefManager.getInstance(view.getContext()).getUser().getId();
                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/updatewallet.php?id=" + id + "&value=" + amount + "&type=" + "Added money to wallet",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //startActivity(new Intent(view.getContext(), AddMoney.class));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                RequestQueue requestQueue2 = Volley.newRequestQueue(view.getContext());
                requestQueue2.add(stringRequest2);
                Toast.makeText(view.getContext(), "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: " + approvalRefNo);
                updateWallet();
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(view.getContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(view.getContext(), "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isConnectionAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    void updateWallet(){

    }

    @Override
    public void onPaymentSuccess(String s) {

    }

    @Override
    public void onPaymentError(int i, String s) {

    }

    public void startPayment() {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        checkout.setKeyID("rzp_test_t2yxXTkKs8CXud");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = getActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "BidBad");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");
            options.put("amount", "200");
            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("Addmoney", "Error in starting Razorpay Checkout", e);
        }
    }
}
