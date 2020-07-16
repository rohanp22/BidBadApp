package com.wielabs.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
public class HomeFragment extends Fragment{

    public ArrayList<Current_Product> current_products;

    public HomeFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    RecyclerView bannerRecyclerView;
    int deviceWidth;
    int noofbids = 0;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().findViewById(R.id.fabhome).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.bar).setVisibility(View.VISIBLE);
        final View view = inflater.inflate(R.layout.fragment_home2, container, false);
        recyclerView = view.findViewById(R.id.homeRecyclerView);
        bannerRecyclerView = view.findViewById(R.id.bannerSliderView);
        fab = getActivity().findViewById(R.id.fabhome);
        if (getActivity().findViewById(R.id.blank).getVisibility() == View.GONE) {
            fab.hide();
            ((ImageView) getActivity().findViewById(R.id.blank)).setVisibility(View.GONE);
        }
        loadBanner(view);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.black, null));

        view.findViewById(R.id.walleticon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).addToBackStack(null).commit();
            }
        });
        showFab(view);

        deviceWidth = getDeviceWidth();
        if (getArguments() != null)
            deviceWidth = getArguments().getInt("width");
        Log.d("HomeFragment", deviceWidth + "");

        return view;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            current_products.add((Current_Product) intent.getExtras().get("MY_KEY"));
            h.notifyDataSetChanged();
            //or
            //exercises = ParseJSON.ChallengeParseJSON(intent.getStringExtra(MY_KEY));
        }
    };

    void animateFab(){

        if (noofbids > 0) {
            fab.show();
            ((ImageView) getActivity().findViewById(R.id.blank)).setVisibility(View.VISIBLE);
        } else {
            ((FloatingActionButton) getActivity().findViewById(R.id.fabhome)).hide();
            ((ImageView) getActivity().findViewById(R.id.blank)).setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter("FILTER"));
    }

    void showFab(final View view){
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getongoingcount.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray heroArray = obj.getJSONArray("Bids");
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                noofbids = Integer.parseInt(heroObject.getString("COUNT(*)"));
                            }
                            animateFab();
                            loadCurrentProducts(view);
                            loadPastProducts(view, deviceWidth);
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

    HomeGridAdapter1 h;

    private void loadBanner(final View view) {
        ArrayList<String> images = new ArrayList<>();
        images.add("http://easyvela.esy.es/CurrentProductImages/index.jpeg");
        images.add("http://easyvela.esy.es/CurrentProductImages/index.jpeg");
        images.add("http://easyvela.esy.es/CurrentProductImages/index.jpeg");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        bannerRecyclerView.setAdapter(new BannerAdapter(bannerRecyclerView, images));
        bannerRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.scrollToPositionWithOffset(Integer.MAX_VALUE / 2, (int) (getDeviceWidth() * .175));

    }

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
                            h = new HomeGridAdapter1(deviceWidth, current_products, getFragmentManager(), view.getContext());
                            recyclerView.setAdapter(h);
                            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                                @Override
                                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                                    int position = parent.getChildAdapterPosition(view);
                                    // Span count is 2
                                    int currentColumn = position % 2;
                                    if (currentColumn == 0)
                                        outRect.right = getResources().getDimensionPixelSize(R.dimen.grid_margin);
                                    else
                                        outRect.left = getResources().getDimensionPixelSize(R.dimen.grid_margin);
                                    outRect.top = getResources().getDimensionPixelSize(R.dimen.grid_margin);
                                    outRect.bottom = getResources().getDimensionPixelSize(R.dimen.grid_margin);
                                }
                            });
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
                                        heroObject.getString("bidamount"),
                                        heroObject.getString("id")
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

    void loadSlideProducts(final View view) {
        final RecyclerView sliderRecyclerView = view.findViewById(R.id.sliderRecyclerView);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(sliderRecyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        sliderRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                sliderRecyclerView.setAdapter(new SliderAdapter(sliderRecyclerView, pastProducts));
                sliderRecyclerView.setLayoutManager(layoutManager);
                sliderRecyclerView.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);
                layoutManager.scrollToPositionWithOffset(Integer.MAX_VALUE / 2, (int) (getDeviceWidth() * .175));
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

    public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.SliderViewHolder> {
        private RecyclerView recyclerView;
        ArrayList<String> images;
        int SIZE;

        public BannerAdapter(RecyclerView recyclerView, ArrayList<String> images) {
            this.recyclerView = recyclerView;
            this.images = images;
            SIZE = images.size();
        }

        @NonNull
        @Override
        public BannerAdapter.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_image, parent, false);
            int width = recyclerView.getWidth();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = (int) (width * .65);
            view.setLayoutParams(layoutParams);
            return new BannerAdapter.SliderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
            Glide.with(holder.imageView.getContext())
                    .load(images.get(position % SIZE))
                    .into(holder.imageView);
            Log.d("Imageurl", images.get(position % SIZE));
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

        class SliderViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;

            public SliderViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.sliderImage);
            }
        }
    }
}
