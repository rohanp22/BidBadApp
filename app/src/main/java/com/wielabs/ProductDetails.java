package com.wielabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetails extends Fragment {

    public ProductDetails() {
        // Required empty public constructor
    }

    String des;
    ArrayList<String> details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        details = new ArrayList<>();
        // Inflate the layout for this fragment
        if(getArguments() != null)
            des = getArguments().getString("Description");

        StringTokenizer defaultTokenizer = new StringTokenizer(des, "\n");

        System.out.println("Total number of tokens found : " + defaultTokenizer.countTokens());

        while (defaultTokenizer.hasMoreTokens())
        {
            details.add(defaultTokenizer.nextToken());
        }

        final RecyclerView productDescListRecycler = view.findViewById(R.id.recyclerProductDesc);
        productDescListRecycler.post(new Runnable() {
            @Override
            public void run() {
                productDescListRecycler.setAdapter(new ProductDetailsAdapter(details));
                productDescListRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }
}
