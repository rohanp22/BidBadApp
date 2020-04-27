package com.wielabs.Fragments;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.wielabs.Activities.AddMoney;
import com.wielabs.HomeGridAdapter1;
import com.wielabs.Models.Current_Product;
import com.wielabs.Models.PastProducts;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;
import com.wielabs.SliderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    int deviceWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().findViewById(R.id.fabhome).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.bar).setVisibility(View.VISIBLE);
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        recyclerView = view.findViewById(R.id.homeRecyclerView);

        view.findViewById(R.id.walleticon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddMoney.class));
            }
        });

        deviceWidth = getDeviceWidth();
        if (getArguments() != null)
            deviceWidth = getArguments().getInt("width");
        Log.d("HomeFragment", deviceWidth + "");
        loadCurrentProducts(view);
        loadPastProducts(view, getDeviceWidth());
        return view;
    }

    int getDeviceWidth(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;

        return width;
    }

    int getDeviceHeight(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;

        return height;
    }

    private ArrayList<Current_Product> current_products;

    private void loadCurrentProducts(final View view) {
        current_products = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/currentproducts.php?id=" + SharedPrefManager.getInstance(getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            current_products.clear();
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Current_Products");
                            Log.d("output", response);
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
                                current_products.add(c);
                            }
                            current_products.sort(new sortTime());
                            recyclerView.setAdapter(new HomeGridAdapter1(deviceWidth, current_products, getFragmentManager()));
                            StaggeredGridLayoutManager l = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
                            recyclerView.setLayoutManager(l);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }

    class sortTime implements Comparator<Current_Product> {
        public int compare(Current_Product a, Current_Product b) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            Date a1 = null, b1 = null;
            try {
                a1 = simpleDateFormat.parse(a.getEnd_date());
                b1 = simpleDateFormat.parse(b.getEnd_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return a1.compareTo(b1);
        }
    }

    private ArrayList<PastProducts> pastProducts;

    private void loadPastProducts(final View view,final int deviceWidth){
        pastProducts = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/pastproducts.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pastProducts.clear();
                            Log.d("output", response);
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Current_Products");
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                PastProducts c = new PastProducts(
                                        heroObject.getString("past_id"),
                                        heroObject.getString("image_url"),
                                        heroObject.getString("title"),
                                        heroObject.getString("endtime"),
                                        heroObject.getString("mrp"),
                                        heroObject.getString("sp"),
                                        heroObject.getString("description"),
                                        heroObject.getString("image_url2"),
                                        heroObject.getString("image_url3"),
                                        heroObject.getString("firstname"),
                                        heroObject.getString("bidamount")
                                );
                                pastProducts.add(c);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                pastProducts.sort(new sortTime2());
                            }
                            loadSlideProducts(view);

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

    class sortTime2 implements Comparator<PastProducts> {
        public int compare(PastProducts a, PastProducts b) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            Date a1 = null, b1 = null;
            try {
                a1 = simpleDateFormat.parse(a.getEnd_date());
                b1 = simpleDateFormat.parse(b.getEnd_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return a1.compareTo(b1);
        }
    }

    void loadSlideProducts(View view){
        final RecyclerView sliderRecyclerView = view.findViewById(R.id.sliderRecyclerView);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(sliderRecyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        sliderRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                sliderRecyclerView.setAdapter(new SliderAdapter(sliderRecyclerView, pastProducts));
                sliderRecyclerView.setLayoutManager(layoutManager);
                //sliderRecyclerView.getLayoutManager().scrollToPosition(Integer.MAX_VALUE/2);
                layoutManager.scrollToPositionWithOffset(Integer.MAX_VALUE/2, (int) (getDeviceWidth() * .175));
                sliderRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            int position = layoutManager.findFirstVisibleItemPosition() + 1;
                            int nextPosition = position + 1;
                            int previousPosition = position - 1;
                            Log.d("Frag", previousPosition + " " + position + " " + nextPosition);
                            ConstraintLayout nextSliderDetail = null;
                            ConstraintLayout previousSliderDetail = layoutManager.findViewByPosition(previousPosition).findViewById(R.id.sliderDetailRoot);
                            ConstraintLayout currentSliderDetail = layoutManager.findViewByPosition(position).findViewById(R.id.sliderDetailRoot);
                                MaterialCardView cardView = (MaterialCardView) layoutManager.findViewByPosition(nextPosition);
                                if (cardView != null)
                                    nextSliderDetail = cardView.findViewById(R.id.sliderDetailRoot);
                                if (previousSliderDetail != null) {
                                    previousSliderDetail.animate().alpha(0.0f).start();
                                    previousSliderDetail.setVisibility(View.GONE);
                                }
                                if (nextSliderDetail != null) {
                                    nextSliderDetail.animate().alpha(0.0f).start();
                                    nextSliderDetail.setVisibility(View.GONE);
                                } else {
                                    currentSliderDetail.animate().alpha(0.0f).start();
                                    currentSliderDetail.setVisibility(View.GONE);
                                    previousSliderDetail.setVisibility(View.VISIBLE);
                                    previousSliderDetail.animate().alpha(1.0f).start();
                                    return;
                                }
                                currentSliderDetail.setVisibility(View.VISIBLE);
                                currentSliderDetail.animate().alpha(1.0f).start();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Slider recyclerview setup
    }
}
