package com.wielabs.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView profileLogout = view.findViewById(R.id.profileLogout);
        profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(view.getContext()).logout();
            }
        });

        ImageView profileEdit = view.findViewById(R.id.profileEdit);
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new EditFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

}
