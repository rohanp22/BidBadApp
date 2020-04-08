package com.wielabs.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.CountDownTimer;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wielabs.Activities.AddMoney;
import com.wielabs.Activities.ProductDescription;
import com.wielabs.BottomSheetProduct;
import com.wielabs.ListenerImage;
import com.wielabs.Models.CartItems;
import com.wielabs.Models.Current_Product;
import com.wielabs.Models.PastProducts;
import com.bumptech.glide.Glide;
import com.wielabs.DetailsTransition;
import com.wielabs.R;
import com.wielabs.Others.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<Current_Product> current_products = new ArrayList<>();
    private Dialog dialog;
    private ProgressBar p;
    private View view;
    private Context c;
    public RecyclerViewAdapterCurrent adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        p = view.findViewById(R.id.progressBarHome);
        c = view.getContext();
        dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.bid_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loadCurrentProducts(view);
        return view;
    }

    @Override
    public void onViewCreated(final @NonNull View view, @Nullable Bundle savedInstanceState) {

        ImageView walletimg = view.findViewById(R.id.walletimg);
        RecyclerView sliderView = view.findViewById(R.id.sliderview2);
        sliderView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        SliderAdapter adapter = new SliderAdapter(view.getContext(), sliderView);
        sliderView.setAdapter(adapter);

        walletimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(view.getContext(), AddMoney.class);
                startActivity(t);
                getActivity().finish();
                ((Activity) view.getContext()).overridePendingTransition(0, 0);
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void loadCurrentProducts(final View view) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/currentproducts.php?id="+SharedPrefManager.getInstance(getContext()).getUser().getId(),
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
                            initRecyclerView(view);
                            mSwipeRefreshLayout.setRefreshing(false);
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

        RequestQueue requestQueue = Volley.newRequestQueue(this.c);
        requestQueue.add(stringRequest);
    }

    private void initRecyclerView(final View view) {
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL );
        RecyclerView recyclerViewCurrent = view.findViewById(R.id.recyclerviewCurrent);

        if(current_products.size() == 0)
            recyclerViewCurrent.setVisibility(View.GONE);

        Drawable verticalDivider = ContextCompat.getDrawable(view.getContext(), R.drawable.vertical_divider);
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(recyclerViewCurrent.getContext(),
                DividerItemDecoration.HORIZONTAL);
        verticalDecoration.setDrawable(verticalDivider);
        Drawable horizontalDivider = ContextCompat.getDrawable(view.getContext(), R.drawable.horizontal_divider);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerViewCurrent.getContext(),
                DividerItemDecoration.VERTICAL);
        horizontalDecoration.setDrawable(horizontalDivider);

        recyclerViewCurrent.setLayoutManager(gridLayoutManager);
        adapter = new RecyclerViewAdapterCurrent(view.getContext(), current_products);

        view.findViewById(R.id.progressBarHome).setVisibility(View.GONE);
        recyclerViewCurrent.setAdapter(null);
        recyclerViewCurrent.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        loadCurrentProducts(view);
    }

    public class RecyclerViewAdapterCurrent extends RecyclerView.Adapter<RecyclerViewAdapterCurrent.ViewHolder> {

        int position;
        Date startDate1;
        String date;
        ArrayList<Current_Product> current_products;
        private Context mContext;

        public RecyclerViewAdapterCurrent(Context context, ArrayList<Current_Product> current_product) {
            this.current_products = current_product;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_horizontal, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapterCurrent.ViewHolder holder,final int position) {

            long diff = 0;
            this.position = position;
            holder.setIsRecyclable(false);
            date = current_products.get(position).getEnd_date();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

            try {
                startDate1 = simpleDateFormat.parse(current_products.get(position).getEnd_date());
                diff = startDate1.getTime() - System.currentTimeMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Glide.with(mContext)
                    .asBitmap()
                    .load(current_products.get(position).getImage_url())
                    .into(holder.image);

            holder.name.setText(current_products.get(position).getTitle());

            ViewCompat.setTransitionName(holder.image, String.valueOf(position) + "_image");

            holder.sp.setText("Entry : ₹"+current_products.get(position).getSp());

            final long secondsInMilli = 1000;
            final long minutesInMilli = secondsInMilli * 60;
            final long hoursInMilli = minutesInMilli * 60;
            final long daysInMilli = hoursInMilli * 24;

            final long finalDiff = diff;

            new CountDownTimer(finalDiff, 1000) {

                long dif = finalDiff;

                public void onTick(long millisUntilFinished) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

                    try {
                        startDate1 = simpleDateFormat.parse(current_products.get(position).getEnd_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long different = startDate1.getTime() - System.currentTimeMillis();

                    if(different > 0) {

                        final long elapsedDays = different / daysInMilli;

                        final long elapsedHours = different / hoursInMilli;
                        different = different % hoursInMilli;

                        final long elapsedMinutes = different / minutesInMilli;
                        different = different % minutesInMilli;

                        final long elapsedSeconds = different / secondsInMilli;

                        String curtime;

                        if(elapsedHours > 0) {
                            curtime = elapsedHours + "hr " + String.format("%02d", elapsedMinutes) + "m";
                            holder.time.setText(curtime);
                        }
                        else {
                            holder.time.setVisibility(View.GONE);
                            holder.r.setVisibility(View.VISIBLE);
                            holder.time2.setVisibility(View.VISIBLE);
                            curtime = String.format("%02d", elapsedMinutes) + "m " + String.format("%02d", elapsedSeconds) + "s";
                            holder.time2.setText(curtime);
                        }

                    }
                }

                public void onFinish() {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                }

            }.start();

            holder.bid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheet(current_products.get(position).getId(),
                            current_products.get(position).getImage_url(),
                            current_products.get(position).getTitle(),
                            current_products.get(position).getSp()
                    );
                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i  = new Intent(view.getContext(), BottomSheetProduct.class);
                    i.putExtra("imageurl", current_products.get(position).getImage_url());
                    i.putExtra("YourKey", current_products.get(position).getId());
                    startActivity(i);
                }
            });
        }

        public void showBottomSheet(String productId, String imageurl, String titleString, String sp) {
            Log.d("yes","yes");
            ActionBottomDialogFragment addPhotoBottomDialogFragment =
                    ActionBottomDialogFragment.newInstance(productId, imageurl, titleString, sp);
            addPhotoBottomDialogFragment.show(getFragmentManager(),
                    ActionBottomDialogFragment.TAG);
        }

        @Override
        public int getItemCount() {
            return current_products.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            Button bid;
            ImageView image;
            TextView name;
            TextView time;
            TextView sp;
            TextView time2;
            RelativeLayout r;

            public ViewHolder(View itemView) {
                super(itemView);
                bid = itemView.findViewById(R.id.bidbtn);
                sp = itemView.findViewById(R.id.sprp);
                r = itemView.findViewById(R.id.flash_layout);
                time = itemView.findViewById(R.id.time);
                time2 = itemView.findViewById(R.id.time2);
                image = itemView.findViewById(R.id.image_view);
                name = itemView.findViewById(R.id.name);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return 1;
        }
    }

    class sortTime implements Comparator<Current_Product>
    {
        public int compare(Current_Product a, Current_Product b)
        {
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

    class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder>{

        ArrayList<PastProducts> CartList;
        Context mContext;
        RecyclerView recyclerView;

        public SliderAdapter(Context context, RecyclerView recyclerView) {
            mContext = context;
            this.recyclerView = recyclerView;
        }

        @NonNull
        @Override
        public SliderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_home, parent, false);
            int width = recyclerView.getWidth();
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = (int)(width * 0.7);
            view.setLayoutParams(params);
            return new SliderAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SliderAdapter.ViewHolder holder, int position) {
            String url = "http://easyvela.esy.es/CurrentProductImages/1-01.jpg";
            switch (position) {
                case 0:
                    Glide.with(holder.itemView)
                            .load(url)
                            .into(holder.image);
                    break;
                case 1:
                    Glide.with(holder.itemView)
                            .load("http://easyvela.esy.es/CurrentProductImages/1-02.jpg")
                            .into(holder.image);
                    break;
                case 2:
                    Glide.with(holder.itemView)
                            .load("http://easyvela.esy.es/CurrentProductImages/1-03.jpg")
                            .into(holder.image);
                    break;
                default:
                    Glide.with(holder.itemView)
                            .load(url)
                            .into(holder.image);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image_view);

            }
        }
    }
}
