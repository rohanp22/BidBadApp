package com.wielabs.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferFragment extends Fragment {

    public ReferFragment() {
        // Required empty public constructor
    }

    MaterialTextView shareRefer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_refer, container, false);
        shareRefer = view.findViewById(R.id.referShare);

        shareRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share Application");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at \n http://easyvela.esy.es/CurrentProductImages/download.html \n And use the code " + SharedPrefManager.getInstance(view.getContext()).getUser().getId() + " to get Rs.50 bonus");
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "choose one"));
            }
        });
        return view;
    }
}
