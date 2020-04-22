package com.wielabs.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.wielabs.Fragments.HomeFragment;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!SharedPrefManager.getInstance(Splash.this).isLoggedIn()){
            startActivity(new Intent(Splash.this, Login.class));
        }

        else{
            startActivity(new Intent(Splash.this, Home.class));
        }
    }
}
