package com.wielabs.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wielabs.R;

public class ProfileFragment extends Fragment {

    TextView HowToPlay, TermsandConditions, SendFeedback, Privacypolicy, Support, logout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        HowToPlay = (TextView) view.findViewById(R.id.profileHowToBid);
        HowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        TermsandConditions = (TextView) view.findViewById(R.id.profileTermsAndConditions);
        SendFeedback = (TextView) view.findViewById(R.id.profileSendFeedback);
        Privacypolicy = (TextView) view.findViewById(R.id.profilePrivacyPolicy);
        Support = (TextView) view.findViewById(R.id.profileSupport);
        logout = (TextView) view.findViewById(R.id.profileLogout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }



}
