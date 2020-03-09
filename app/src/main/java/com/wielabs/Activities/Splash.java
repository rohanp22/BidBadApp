package com.wielabs.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.wielabs.Fragments.HomeFragment;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            i = new Intent(Splash.this, Home.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
        else {
            i = new Intent(Splash.this, Login.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }

    }

    @Override
    public void onBackPressed() {

    }
}
