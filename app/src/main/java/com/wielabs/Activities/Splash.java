package com.wielabs.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            startActivity(new Intent(Splash.this, Home.class));
        }
        else {
            startActivity(new Intent(Splash.this, Login.class));
        }

    }

    @Override
    public void onBackPressed() {

    }
}
