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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.wielabs.Activities.AddMoney;
import com.wielabs.Activities.Home;
import com.wielabs.Activities.ProfileActivity;
import com.wielabs.BottomSheetProduct;
import com.wielabs.DetailsTransition;
import com.wielabs.LeaderBoardFragment;
import com.wielabs.ListenerImage;
import com.wielabs.Models.CartItems;
import com.wielabs.Models.Current_Product;
import com.wielabs.Activities.ProductDescription;
import com.wielabs.Models.PastProducts;
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

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ListenerImage{

    public ArrayList<Current_Product> current_products = new ArrayList<>();
    public List<CartItems> cartItems = new ArrayList<>();
    Dialog dialog;
    Button mButton;
    int bal;
    ProgressBar p;
    View view1;
    View view;
    Context c;
    public RecyclerViewAdapterCurrent adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        p = getActivity().findViewById(R.id.progressBarHome);
        c = getActivity();
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.bid_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        //this.view = view;
        //Home.navigation.setSelectedItemId(R.id.navigation_home);
        view1 = view;
        loadCurrentProducts(view);
        return view;
    }

    ImageView button;

    @Override
    public void onViewCreated(final @NonNull View view, @Nullable Bundle savedInstanceState) {
        //this.view = view;
        p = view.findViewById(R.id.progressBarHome);
        c = view.getContext();
        button = view.findViewById(R.id.overflow);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(view.getContext() , button);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getTitle().toString()){
                            case "Settings" : getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                            break;

                            case "Profile" : startActivity(new Intent(view.getContext(), ProfileActivity.class));
                            break;
                        }
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });

        dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.bid_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView walletimg = (ImageView) view.findViewById(R.id.walletimg);
        walletimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(view.getContext(), AddMoney.class);
                startActivity(t);
                getActivity().finish();
                ((Activity) view.getContext()).overridePendingTransition(0, 0);
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView sliderView = view.findViewById(R.id.sliderview2);
        sliderView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        SliderAdapter adapter = new SliderAdapter(view.getContext(), sliderView);
        sliderView.setAdapter(adapter);

//        SliderAdapterExample adapter2 = new SliderAdapterExample(view.getContext());
//
//        sliderView.setSliderAdapter(adapter2);
//
//        sliderView.setNextFocusRightId(1);
//        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
//        sliderView.startAutoCycle();
    }

    void loadCurrentProducts(final View view) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/currentproducts.php?id="+SharedPrefManager.getInstance(getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            current_products.clear();
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Current_Products");
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

    public RecyclerView recyclerViewCurrent;

    private void initRecyclerView(final View view) {

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL );
        recyclerViewCurrent = (RecyclerView) view.findViewById(R.id.recyclerviewCurrent);
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
//        recyclerViewCurrent.addItemDecoration(verticalDecoration);
//        recyclerViewCurrent.addItemDecoration(horizontalDecoration);
        recyclerViewCurrent.setLayoutManager(gridLayoutManager);
        adapter = new RecyclerViewAdapterCurrent(view.getContext(), current_products, this);
        view.findViewById(R.id.progressBarHome).setVisibility(View.GONE);
        recyclerViewCurrent.setAdapter(null);
        recyclerViewCurrent.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        loadCurrentProducts(view);
    }

    @Override
    public void onKittenClicked(RecyclerViewAdapterCurrent.ViewHolder holder, int position) {
        ProductDescription kittenDetails = new ProductDescription();

        Bundle args = new Bundle();
        args.putString("YourKey", current_products.get(position).getId());
        args.putString("show","yes");
        args.putString("im", current_products.get(position).getImage_url());
        kittenDetails.setArguments(args);

        // Note that we need the API version check here because the actual transition classes (e.g. Fade)
        // are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
        // ARE available in the support library (though they don't do anything on API < 21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            kittenDetails.setSharedElementEnterTransition(new DetailsTransition());
            kittenDetails.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            kittenDetails.setSharedElementReturnTransition(new DetailsTransition());
        }

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.image, "trans")
                .replace(R.id.fragment_container, kittenDetails)
                .addToBackStack(null)
                .commit();
    }

    public class RecyclerViewAdapterCurrent extends RecyclerView.Adapter<RecyclerViewAdapterCurrent.ViewHolder> {

        int id;
        int position;
        private static final String TAG = "RecyclerViewAdapterCurrent";
        long diff;
        Date startDate1;
        String date;
        ArrayList<Current_Product> current_products;
        private Context mContext;
        ListenerImage mlistener;

        public RecyclerViewAdapterCurrent(Context context, ArrayList<Current_Product> current_product, ListenerImage mlistener) {
            this.current_products = current_product;
            mContext = context;
            this.mlistener = mlistener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_horizontal, parent, false);
            return new ViewHolder(view);
        }

        int bal;

        @Override
        public void onBindViewHolder(final RecyclerViewAdapterCurrent.ViewHolder holder,final int position) {

            long diff = 0;
            this.position = position;
            holder.setIsRecyclable(false);
            date = current_products.get(position).getEnd_date();
            //loadCurrentProducts(view);


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

            //holder.image.setImageDrawable(getResources().getDrawable(R.drawable.placekitten_1));

            holder.name.setText(current_products.get(position).getTitle());

            ViewCompat.setTransitionName(holder.image, String.valueOf(position) + "_image");

            //holder.mrp.setText(current_products.get(position).getMrp());

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

                        String curtime = elapsedHours + "hr " + String.format("%02d", elapsedMinutes) + "m " + String.format("%02d", elapsedSeconds) + "s";
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
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                }
            }.start();

            holder.bid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheet(current_products.get(position).getId(), current_products.get(position).getImage_url(), current_products.get(position).getTitle(), current_products.get(position).getSp());
                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i  = new Intent(view.getContext(), BottomSheetProduct.class);
                    i.putExtra("imageurl", current_products.get(position).getImage_url());
                    i.putExtra("YourKey", current_products.get(position).getId());
                    startActivity(i);
//                    ProductDescription ldf = new ProductDescription ();
//                    Bundle args = new Bundle();
//                    args.putString("YourKey", current_products.get(position).getId());
//                    args.putString("show","yes");
//                    ldf.setArguments(args);
//                    mlistener.onKittenClicked(holder, position);
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

        public boolean updateBalance() {
            return true;
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
        // Used for sorting in ascending order of
        // roll number
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

    public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

        public SliderAdapterExample(Context context) {
        }

        @Override
        public SliderAdapterExample.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);

            return new SliderAdapterExample.SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterExample.SliderAdapterVH viewHolder, int position) {
            //viewHolder.textViewDescription.setText("This is slider item " + position);
            String url = "http://easyvela.esy.es/CurrentProductImages/1-01.jpg";
            switch (position) {
                case 0:
                    Glide.with(viewHolder.itemView)
                            .load(url)
                            .into(viewHolder.imageViewBackground);
                    break;
                case 1:
                    Glide.with(viewHolder.itemView)
                            .load("http://easyvela.esy.es/CurrentProductImages/1-02.jpg")
                            .into(viewHolder.imageViewBackground);
                    break;
                case 2:
                    Glide.with(viewHolder.itemView)
                            .load("http://easyvela.esy.es/CurrentProductImages/1-03.jpg")
                            .into(viewHolder.imageViewBackground);
                    break;
                default:
                    Glide.with(viewHolder.itemView)
                            .load(url)
                            .into(viewHolder.imageViewBackground);
                    break;

            }

        }

        @Override
        public int getCount() {
            //slider view count could be dynamic size
            return 3;
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;

            public SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.image_view);
                //textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
                this.itemView = itemView;
            }
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
                //listView = itemView.findViewById(R.id.bidHistory);
                image = itemView.findViewById(R.id.image_view);

            }
        }
    }
}
