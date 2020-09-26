package com.wielabs.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;

import com.wielabs.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderSummary extends Fragment {

    ImageView productImage;
    TextView productTitle;
    TextView orderid;
    TextView dateOfOrder;
    TextView deliverystatus;
    TextView address;
    TextView subtotal, charges, total;

    public OrderSummary() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_summary, container, false);
        productImage = view.findViewById(R.id.summaryOrderImage);
        productTitle = view.findViewById(R.id.summaryOrderTitle);
        orderid = view.findViewById(R.id.summaryOrderId);
        dateOfOrder = view.findViewById(R.id.summaryOrderDate);
        deliverystatus = view.findViewById(R.id.summaryOrderStatus);
        address = view.findViewById(R.id.summaryOrderAddress);
        subtotal = view.findViewById(R.id.summaryOrderMerchandiseAmount);
        charges = view.findViewById(R.id.summaryOrderShippingAmount);
        total = view.findViewById(R.id.summaryOrderShippingTotalAmount);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView summaryImage = view.findViewById(R.id.summaryOrderImage);
        AppCompatSeekBar seekBar = view.findViewById(R.id.summaryOrderSeekbar);
        getActivity().findViewById(R.id.fabhome).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.bar).setVisibility(View.INVISIBLE);
        seekBar.setPadding(0, 0, 0, 0);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }
}
