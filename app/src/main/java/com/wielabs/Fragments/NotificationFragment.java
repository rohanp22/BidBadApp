package com.wielabs.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wielabs.Models.NotificationModel;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    ArrayList<NotificationModel> notificationModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_notification, container, false);
        getActivity().findViewById(R.id.fabhome).setVisibility(View.GONE);
        getActivity().findViewById(R.id.bar).setVisibility(View.GONE);
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.notificationRecyclerview);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.notificationdivider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        notificationModels = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getnotifications.php?id=" + SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            notificationModels.clear();
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Notifications");
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                NotificationModel c = new NotificationModel(
                                        heroObject.getString("notificationtitle"),
                                        heroObject.getString("notificationbody"),
                                        heroObject.getString("time")
                                );
                                notificationModels.add(c);
                            }
                            NotificationAdapter wishlistAdapter = new NotificationAdapter(view.getContext(), notificationModels);
                            recyclerView.setAdapter(wishlistAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

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
        return view;
    }

    class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.BidHistoryViewHolder> {

        Context context;
        ArrayList<NotificationModel> heroList;

        NotificationAdapter(Context context, ArrayList<NotificationModel> heroList) {
            this.context = context;
            this.heroList = heroList;
        }

        @NonNull
        @Override
        public NotificationAdapter.BidHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NotificationAdapter.BidHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationmodel, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BidHistoryViewHolder holder, int position) {
            Date date1;
            if (getItemCount() == 0) {
                //    progressBar.setVisibility(View.GONE);
            }
            //progressBar.setVisibility(View.GONE);
            try {
                date1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").parse(heroList.get(position).getTime());
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
                String strDate = formatter.format(date1);
                holder.time.setText(strDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.title.setText(heroList.get(position).getTitle());
            holder.message.setText(heroList.get(position).getMessage());
        }

        @Override
        public int getItemCount() {
            if (heroList.size() == 0) {
                //progressBar.setVisibility(View.GONE);
            }
            Log.d("Size", heroList.size() + "");
            return heroList.size();
        }

        class BidHistoryViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView message;
            TextView time;

            BidHistoryViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.notificationTitle);
                time = itemView.findViewById(R.id.notificationTime);
                message = itemView.findViewById(R.id.notificationMessage);
            }
        }
    }

}
