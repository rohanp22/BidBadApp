package com.wielabs.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.wielabs.Models.ScratchCardModel;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;
import com.wielabs.ScratchCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RewardsFragment extends Fragment {

    Dialog dialog;
    RecyclerView recyclerView;
    int noofbids;
    View view;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        this.view = view;
        dialog = new Dialog(view.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getmyposition.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Leaders");
                            JSONObject heroObject = heroArray.getJSONObject(0);
                            noofbids = Integer.parseInt(heroObject.getString("numberofbids"));
                            loadCurrentProducts();

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
        return view;
    }

    int count;
    ArrayList<ScratchCardModel> scratchCards;

    private void loadCurrentProducts() {
        scratchCards = new ArrayList<>();
        scratchCards.add(new ScratchCardModel(null, "lock", "lock", "orange"));
        scratchCards.add(new ScratchCardModel(null, "lock", "lock", "green"));
        scratchCards.add(new ScratchCardModel(null, "lock", "lock", "blue"));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getscratchcards.php?id=" + SharedPrefManager.getInstance(getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Scratch_Cards");
                            Log.d("output", response);
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                ScratchCardModel c = new ScratchCardModel(
                                        heroObject.getString("id"),
                                        heroObject.getString("type"),
                                        heroObject.getString("text"),
                                        heroObject.getString("color")
                                );
                                scratchCards.add(c);
                            }
                            count = scratchCards.size();
                            Log.d("Count", count + "");
                            loadScratchedCards();
//                            recyclerView = view.findViewById(R.id.recyclerViewRewards);
//                            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
//                            recyclerView.setAdapter(new ScratchCardAdapter(view.getContext(), scratchCards));
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

    private void loadScratchedCards() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getscratchedcards.php?id=" + SharedPrefManager.getInstance(getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Scratch_Cards");
                            Log.d("output", response);
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                ScratchCardModel c = new ScratchCardModel(
                                        heroObject.getString("id"),
                                        heroObject.getString("type"),
                                        heroObject.getString("text"),
                                        heroObject.getString("color")
                                );
                                scratchCards.add(c);
                            }
                            recyclerView = view.findViewById(R.id.recyclerViewRewards);
                            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
                            recyclerView.setAdapter(new ScratchCardAdapter(view.getContext(), scratchCards));
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

    public class ScratchCardAdapter extends RecyclerView.Adapter<ScratchCardAdapter.ScratchCardHolderView> {

        public Context context;
        ArrayList<String> colors;
        ArrayList<ScratchCardModel> scratchCardModels;

        public ScratchCardAdapter(Context context, ArrayList<ScratchCardModel> scratchCardModels) {
            this.context = context;
            this.scratchCardModels = scratchCardModels;
            colors = new ArrayList<>();
            colors.add("#5932CD32"); //green
            colors.add("#594F8FFF"); //aqua
            colors.add("#59FFA500"); //orange
        }

        @NonNull
        @Override
        public ScratchCardAdapter.ScratchCardHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ScratchCardAdapter.ScratchCardHolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.scratch_card, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ScratchCardHolderView holder, int position) {
            Log.d("Position", position + " " + scratchCardModels.get(position).getText());
            if (position >= count) {
                holder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_party));
                holder.scratchText.setText(scratchCardModels.get(position).getText());
                holder.logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
            } else if (position < count) {
                holder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_greyparty));
                holder.scratchText.setText("Scratch the reward card to get gift from company");
                holder.logo.setImageDrawable(getResources().getDrawable(R.drawable.ic_bidbad_grey_icon));
            }
            if (!scratchCardModels.get(position).getText().equals("lock")) {
                holder.materialCardView.setVisibility(View.GONE);
                holder.lockIcon.setVisibility(View.GONE);
            } else {
                holder.materialCardView.setVisibility(View.VISIBLE);
                holder.lockIcon.setVisibility(View.VISIBLE);
                if (scratchCardModels.get(position).getColor().equals("orange")) {
                    holder.progressBar.setMax(5);
                    holder.progressBar.setProgress(noofbids % 5);
                    holder.progress.setText("(" + noofbids % 5 + "/5)");
                } else {
                    holder.progressBar.setMax(5);
                    holder.progressBar.setProgress(1);
                    holder.progress.setText("(1/5)");
                }
            }
            if (position < count) {
                switch (scratchCardModels.get(position).getColor()) {
                    case "orange":
                        holder.scratchCardColor.setBackgroundColor(Color.parseColor(colors.get(2)));
                        break;

                    case "green":
                        holder.scratchCardColor.setBackgroundColor(Color.parseColor(colors.get(0)));
                        break;

                    case "blue":
                        holder.scratchCardColor.setBackgroundColor(Color.parseColor(colors.get(1)));
                        break;
                }
            } else if (position >= count) {
                holder.scratchCardColor.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }

        @Override
        public int getItemCount() {
            return scratchCardModels.size();
        }

        class ScratchCardHolderView extends RecyclerView.ViewHolder {

            ImageView imageView;
            ImageView scratchCardColor;
            ImageView logo;
            ProgressBar progressBar;
            MaterialCardView materialCardView;
            ImageView lockIcon;
            ScratchCard scratchView;
            TextView progress;
            TextView scratchText;

            public ScratchCardHolderView(@NonNull final View itemView) {
                super(itemView);
                materialCardView = itemView.findViewById(R.id.lockCard);
                lockIcon = itemView.findViewById(R.id.lockIcon);
                progressBar = itemView.findViewById(R.id.progressBar);
                imageView = itemView.findViewById(R.id.imageScratch);
                progress = itemView.findViewById(R.id.progressText);
                scratchCardColor = itemView.findViewById(R.id.scratchcardcolor);
                scratchText = itemView.findViewById(R.id.scratchText);
                logo = itemView.findViewById(R.id.scratchCardMiddleLogo);

                Log.d("Adapter", getAdapterPosition() + "  " + count);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if (getAdapterPosition() < 3) {
                            Toast.makeText(itemView.getContext(), "Complete task to unlock scratch card", Toast.LENGTH_SHORT).show();
                            if (scratchCardModels.get(getAdapterPosition()).getColor().equals("orange")) {
                                progressBar.setMax(5);
                                progressBar.setProgress(noofbids % 5);
                                progress.setText("(" + noofbids % 5 + "/5)");
                            }
                        } else if (getAdapterPosition() < count) {
                            dialog.setContentView(R.layout.dialog_scratch);
                            TextView scratchCardText = dialog.findViewById(R.id.scratchCardText);
                            scratchCardText.setText(scratchCardModels.get(getAdapterPosition()).getText());
                            scratchView = dialog.findViewById(R.id.scratch_view);
                            scratchView.setOnScratchListener(new ScratchCard.OnScratchListener() {
                                @Override
                                public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                                    if (visiblePercent > 0.3) {
                                        scratchView.setVisibility(View.GONE);
                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/redeemscratchcard.php?id=" + scratchCardModels.get(getAdapterPosition()).getId(),
                                                new Response.Listener<String>() {
                                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.d("Response", response);
                                                        if (response.equals("success")) {
                                                            loadCurrentProducts();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        //Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                                        requestQueue.add(stringRequest);
                                    }
                                    Log.d("visible", visiblePercent + "");
                                }
                            });
                            LayerDrawable layer = (LayerDrawable) getResources().getDrawable(R.drawable.scratch_card_drawable, null);
                            layer.mutate();
                            ColorDrawable cd = null;

                            switch (scratchCardModels.get(getAdapterPosition()).getColor()) {
                                case "orange":
                                    cd = new ColorDrawable(Color.parseColor(colors.get(2)));
                                    break;

                                case "green":
                                    cd = new ColorDrawable(Color.parseColor(colors.get(0)));
                                    break;

                                case "blue":
                                    cd = new ColorDrawable(Color.parseColor(colors.get(1)));
                                    break;
                            }

                            layer.setDrawableByLayerId(R.id.itemScratchCard, cd);
                            layer.invalidateSelf();
                            scratchView.setmDrawable(layer);
                            dialog.show();
                        } else if (getAdapterPosition() > count) {

                        }
                    }
                });
            }
        }
    }
}