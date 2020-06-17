package com.wielabs.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView wishlist;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        wishlist = view.findViewById(R.id.profileWishList);

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Profile", "CLicked");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new Wishlist()).addToBackStack(null).commit();
            }
        });

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
