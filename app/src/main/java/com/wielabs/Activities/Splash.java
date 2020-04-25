package com.wielabs.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.wielabs.Others.SharedPrefManager;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!SharedPrefManager.getInstance(Splash.this).isLoggedIn()){
            startActivity(new Intent(Splash.this, Login.class));
            finish();
        }

        else{
            startActivity(new Intent(Splash.this, Home.class));
            finish();
        }
    }
}
