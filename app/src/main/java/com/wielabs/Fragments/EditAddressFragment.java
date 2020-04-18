package com.wielabs.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wielabs.R;

public class EditAddressFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_address_fragment, container, false);

        BottomAppBar b = getActivity().findViewById(R.id.bar);
        b.setVisibility(View.GONE);

        FloatingActionButton f = getActivity().findViewById(R.id.fabhome);
        getActivity().findViewById(R.id.shadowview).setVisibility(View.GONE);
        f.setVisibility(View.GONE);

        return view;
    }
}
