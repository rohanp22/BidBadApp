package com.wielabs.Fragments;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView wishlist, invite, feedback, bids, orders, name, noofbids, noofwins, noofrewards, wonbids;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.profileName);
        noofbids = view.findViewById(R.id.profileBids);
        noofwins = view.findViewById(R.id.profileWins);
        noofrewards = view.findViewById(R.id.profileRewards);
        wonbids = view.findViewById(R.id.profileNotifications);

        name.setText(SharedPrefManager.getInstance(view.getContext()).getUser().getFirstname());

        orders = view.findViewById(R.id.profileOrders);
        wishlist = view.findViewById(R.id.profileWishList);
        invite = view.findViewById(R.id.profileInvite);
        feedback = view.findViewById(R.id.profileSendFeedback);
        bids = view.findViewById(R.id.profileBids);

        wonbids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new EmptyFragment("")).addToBackStack(null).commit();
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getmyposition.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Leaders");
                            JSONObject heroObject = heroArray.getJSONObject(0);
                            noofbids.setText(heroObject.getString("numberofbids"));
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

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyOrdersFragment()).addToBackStack(null).commit();
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet(view);
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReferFragment()).addToBackStack(null).commit();
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Profile", "CLicked");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new Wishlist()).addToBackStack(null).commit();
            }
        });

        TextView profileLogout = view.findViewById(R.id.profileLogout);
        profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(view.getContext()).logout();
            }
        });

        wonbids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new WonBidsFagment()).addToBackStack(null).commit();
            }
        });


        ImageView profileEdit = view.findViewById(R.id.profileEdit);
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new EditFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    public void showBottomSheet(View view) {
        FeedbackBottomDialogFragment addPhotoBottomDialogFragment =
                FeedbackBottomDialogFragment.newInstance();
        addPhotoBottomDialogFragment.show(getFragmentManager(),
                FeedbackBottomDialogFragment.TAG);
    }

}
