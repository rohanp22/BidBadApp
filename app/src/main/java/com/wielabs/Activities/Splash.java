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
        Window window = Splash.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Splash.this, R.color.colorPrimary));

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
