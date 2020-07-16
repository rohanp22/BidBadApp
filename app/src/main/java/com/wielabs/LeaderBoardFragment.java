package com.wielabs;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.wielabs.Models.Leader;
import com.wielabs.Others.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeaderBoardFragment extends Fragment {

    ArrayList<Leader> leaderlist;
    RecyclerView leaderboard;
    TextView textRank, textName, textNumberofbids;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        // Inflate the layout for this fragment
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_small));
        leaderboard = view.findViewById(R.id.leaderBoardRecyclerView);
        leaderboard.addItemDecoration(new ItemBackgroundDecoration());
        leaderboard.addItemDecoration(itemDecorator);
        leaderboard.setLayoutManager(new LinearLayoutManager(view.getContext()));

        leaderlist = new ArrayList<>();
        textRank = view.findViewById(R.id.textRank);
        textName = view.findViewById(R.id.someText1);
        textNumberofbids = view.findViewById(R.id.someText3);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getmyposition.php?id="+SharedPrefManager.getInstance(view.getContext()).getUser().getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            leaderlist.clear();
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Leaders");
                            JSONObject heroObject = heroArray.getJSONObject(0);
                            textName.setText(heroObject.getString("firstname"));
                            textRank.setText(heroObject.getString("position"));
                            textNumberofbids.setText(heroObject.getString("numberofbids"));
                            loadLeaderBoard(view);

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

    private void loadLeaderBoard(final View view) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/getleaderboard.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            leaderlist.clear();
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("Leaders");
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                Leader c = new Leader(
                                        heroObject.getString("id"),
                                        Integer.parseInt(heroObject.getString("numberofbids")),
                                        heroObject.getString("firstname")
                                );
                                leaderlist.add(c);
                            }
                            LeaderBoardAdapter leaderBoardAdapter = new LeaderBoardAdapter(view.getContext(), leaderlist);
                            leaderboard.setAdapter(leaderBoardAdapter);

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

}