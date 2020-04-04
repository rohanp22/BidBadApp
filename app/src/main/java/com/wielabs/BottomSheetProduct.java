package com.wielabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wielabs.Models.Current_Product;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class BottomSheetProduct extends AppCompatActivity {

    String imageurl, imageurl2, imageurl3;
    SliderAdapterExample adapter;
    TextView title, entry, productdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet_product);

        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        productdesc = (TextView) findViewById(R.id.desctext);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.d("State", newState+"");
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d("bottomSheet", bottomSheet.getId()+"");
            }
        });
        title = (TextView) findViewById(R.id.titleProductdes);
        entry = (TextView) findViewById(R.id.price) ;

        String value = getIntent().getStringExtra("YourKey");
        String productId = value;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getproductinfo.php?id="+value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Current_Products");
                            for (int i = 0; i < heroArray.length(); i++) {

                                JSONObject heroObject = heroArray.getJSONObject(i);
                                String currentid = heroObject.getString("currentid");
                                Log.d("Current", currentid);
                                imageurl = heroObject.getString("image_url");
                                imageurl2 = heroObject.getString("image_url2");
                                imageurl3 = heroObject.getString("image_url3");
                                String titleString = heroObject.getString("title");
                                Log.d("Current", titleString);
                                String endtime = heroObject.getString("endtime");
                                String mrpString = heroObject.getString("mrp");
                                String sp = heroObject.getString("sp");
                                title.setText(titleString);
                                //mrp.setText("MRP: ₹"+mrpString);
                                entry.setText("Entry : ₹"+sp);
                                String h = "Hello \n world";
                                String d = heroObject.getString("description");
                                Log.d("Text", heroObject.getString("description"));
                                String s = d.replace("\\t","\t");
                                productdesc.setText(s.replace("\\n","\n"));

                                //productDescription.setText(d);
                                //adapter.notifyDataSetChanged();

//                                Glide.with(view.getContext())
//                                        .asBitmap()
//                                        .load(imageurl)
//                                        .into(sliderView);

                                Current_Product current = new Current_Product(currentid, imageurl, titleString, endtime, mrpString, sp, d, heroObject.getString("image_url2"), heroObject.getString("image_url3"));

                                SliderView sliderView = findViewById(R.id.productImages);

                                adapter = new SliderAdapterExample(BottomSheetProduct.this);
                                imageurl = getIntent().getStringExtra("imageurl");
                                sliderView.setSliderAdapter(adapter);

                                sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                                sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                                sliderView.startAutoCycle();
                                String date = endtime;


                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

//                                try {
//                                    startDate1 = simpleDateFormat.parse(endtime);
//                                    diff = startDate1.getTime() - System.currentTimeMillis();
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//
//                                final long secondsInMilli = 1000;
//                                final long minutesInMilli = secondsInMilli * 60;
//                                final long hoursInMilli = minutesInMilli * 60;
//                                final long daysInMilli = hoursInMilli * 24;
//
//                                final long finalDiff = diff;
//
//                                new CountDownTimer(finalDiff, 1000) {
//
//                                    long dif = finalDiff;
//
//                                    public void onTick(long millisUntilFinished) {
//
//                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
//
//                                        try {
//                                            startDate1 = simpleDateFormat.parse(endtime);
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        long different = Math.abs(startDate1.getTime() - System.currentTimeMillis());
//
//                                        final long elapsedDays = different / daysInMilli;
//
//                                        final long elapsedHours = different / hoursInMilli;
//                                        different = different % hoursInMilli;
//
//                                        final long elapsedMinutes = different / minutesInMilli;
//                                        different = different % minutesInMilli;
//
//                                        final long elapsedSeconds = different / secondsInMilli;
//                                        String curtime = "";
//                                        if(elapsedHours > 0) {
//                                            curtime = elapsedHours + " hr " + String.format("%02d", elapsedMinutes) + " m ";
//                                        }
//                                        else {
//                                            curtime = String.format("%02d", elapsedMinutes) + " m " + String.format("%02d", elapsedSeconds) + " s ";
//                                        }
//                                        //clock.setText(curtime);
//                                    }
//
//                                    public void onFinish() {
//
////                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/currentmove.php?id=" + current_products.get(position).getId(),
////                            new Response.Listener<String>() {
////                                @Override
////                                public void onResponse(String response) {
////                                    Toast.makeText(mContext, response, Toast.LENGTH_LONG);
////                                    initRecyclerView();
////                                }
////                            },
////                            new Response.ErrorListener() {
////                                @Override
////                                public void onErrorResponse(VolleyError error) {
////
////                                }
////                            });
////                    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
////                    requestQueue.add(stringRequest);
//                                    }
//                                }.start();

                                //ImageView i2 = getView().findViewById(R.id.productImage);

//                                Glide.with(getView().getContext())
//                                        .asBitmap()
//                                        .load(imageurl)
//                                        .into(i2);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(BottomSheetProduct.this);
        requestQueue.add(stringRequest);

//        int id = SharedPrefManager.getInstance()).getUser().getId();
//        place.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showBottomSheet();
//                id = SharedPrefManager.getInstance(view.getContext()).getUser().getId();
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/checkbalance.php?id="+id,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                try {
//                                    JSONObject obj = new JSONObject(response);
//                                    bal = Integer.parseInt(obj.getString("balance"));
//                                    if(bal > Integer.parseInt(sp)){
//                                        Intent i = new Intent(v.getContext(), PlaceBid.class);
//                                        i.putExtra("id", current);
//                                        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/updatewallet.php?id="+id+"&value="+Integer.parseInt(sp)*-1+"&type=Paid to enter the bidding for "+titleString+"&image_url="+imageurl,
//                                                new Response.Listener<String>() {
//                                                    @Override
//                                                    public void onResponse(String response) {
//
//                                                    }
//                                                },
//                                                new Response.ErrorListener() {
//                                                    @Override
//                                                    public void onErrorResponse(VolleyError error) {
//                                                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//
//                                        RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
//                                        requestQueue2.add(stringRequest2);
//                                        startActivity(i);
//                                        getActivity().finish();
//                                    }
//                                    else
//                                        Toast.makeText(getActivity(),"Insuffecient Balance, add money to wallet",Toast.LENGTH_LONG).show();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//                requestQueue.add(stringRequest);
//                    //EditText myBid = (EditText) dialog.findViewById(R.id.bidAmount);
            }
//        });
//    }
//
//
//
//
//
//

    public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

        private Context context;

        public SliderAdapterExample(Context context) {
            this.context = context;
        }

        @Override
        public SliderAdapterExample.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);

            return new SliderAdapterExample.SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterExample.SliderAdapterVH viewHolder, int position) {
            //viewHolder.textViewDescription.setText("This is slider item " + position);

            switch (position) {
                case 0:
                    Glide.with(viewHolder.itemView)
                            .load(imageurl)
                            .into(viewHolder.imageViewBackground);
                    break;
                case 1:
                    Glide.with(viewHolder.itemView)
                            .load(imageurl2)
                            .into(viewHolder.imageViewBackground);
                    break;
                case 2:
                    Glide.with(viewHolder.itemView)
                            .load(imageurl3)
                            .into(viewHolder.imageViewBackground);
                    break;
                default:
                    Glide.with(viewHolder.itemView)
                            .load("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
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
}
