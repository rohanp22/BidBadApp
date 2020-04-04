package com.wielabs.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
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

        AppUpdater appUpdater = new AppUpdater(Splash.this);
        appUpdater.setDisplay(Display.DIALOG);
        appUpdater.setUpdateJSON("http://easyvela.esy.es/AndroidAPI/update.json");
        appUpdater.setTitleOnUpdateAvailable("Update available")
                .setContentOnUpdateAvailable("Check out the latest version available of my app!")
                .setTitleOnUpdateNotAvailable("Update not available")
                .setContentOnUpdateNotAvailable("No update available. Check for updates again later!")
                .setButtonUpdate("Update now?")
	            .setButtonDismiss("Maybe later")
	            .setButtonDoNotShowAgain("Huh, not interested")
	            .setIcon(R.drawable.logo) // Notification icon
                .setCancelable(false);

        appUpdater.start();

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
