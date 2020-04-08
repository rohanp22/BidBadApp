package com.wielabs.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wielabs.Fragments.BidsHistory;
import com.wielabs.Fragments.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.wielabs.Fragments.ActionBottomDialogFragment;
import com.wielabs.Fragments.FeedbackBottomDialogFragment;
import com.wielabs.Fragments.HomeFragment;
import com.wielabs.Fragments.MyBidsFragment;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ActionBottomDialogFragment.ItemClickListener, FeedbackBottomDialogFragment.ItemClickListener {

    FloatingActionButton fab;
    ImageView reward, profile, results, indicator, home;
    TextView homeText, rewardText, resultText, profileText;
    int TIME_OUT = 250;
    int dotPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        home = findViewById(R.id.homeIcon);
        reward = findViewById(R.id.rewardsIcon);
        profile = findViewById(R.id.profileIcon);
        results = findViewById(R.id.resultIcon);
        homeText = findViewById(R.id.homeText);
        rewardText = findViewById(R.id.rewardsText);
        resultText = findViewById(R.id.resultsText);
        profileText = findViewById(R.id.profileText);
        indicator = (ImageView) findViewById(R.id.indicator);
        fab = findViewById(R.id.fabhome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFragment(new HomeFragment(), "home");
            }
        }, 500);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("Token", token);
                        sendToken(token);
                    }
                });

        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dotPosition != 2) {
                    if (dotPosition > 2)
                        moveLeft(reward, indicator);
                    else
                        moveRight(reward, indicator);
                    dotPosition = 2;
                    rewardText.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            homeText.setVisibility(View.VISIBLE);
                            resultText.setVisibility(View.VISIBLE);
                            profile.setVisibility(View.VISIBLE);
                        }
                    }, TIME_OUT);
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dotPosition != 0){
                    moveLeft(home, indicator);
                    dotPosition = 0;
                    homeText.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rewardText.setVisibility(View.VISIBLE);
                            resultText.setVisibility(View.VISIBLE);
                            profile.setVisibility(View.VISIBLE);
                            loadFragment(new HomeFragment(), "home");
                        }
                    }, TIME_OUT);
                }
            }
        });

        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dotPosition != 1) {
                    if (dotPosition > 1)
                        moveLeft(results, indicator);
                    else
                        moveRight(results, indicator);
                    dotPosition = 1;
                    resultText.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            homeText.setVisibility(View.VISIBLE);
                            rewardText.setVisibility(View.VISIBLE);
                            profile.setVisibility(View.VISIBLE);
                            loadFragment(new BidsHistory(), "bidhistoy");
                        }
                    }, TIME_OUT);
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(dotPosition != 3){
                    moveRight(profile, indicator);
                    dotPosition = 3;
                    profileText.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            homeText.setVisibility(View.VISIBLE);
                            resultText.setVisibility(View.VISIBLE);
                            rewardText.setVisibility(View.VISIBLE);
                            loadFragment(new ProfileFragment(), "profile");
                        }
                    }, TIME_OUT);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new MyBidsFragment(), "mybids");
            }
        });

        Window window = Home.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Home.this, R.color.colorPrimary));
    }

    void sendToken(String token) {
        int id = SharedPrefManager.getInstance(Home.this).getUser().getId();
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/updateToken.php?id=" + id + "&token=" + token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        RequestQueue requestQueue2 = Volley.newRequestQueue(Home.this);
        requestQueue2.add(stringRequest2);
    }

    @SuppressLint("RestrictedApi")
    private boolean loadFragment(final Fragment fragment, String name) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof HomeFragment) {
            if (doubleBackToExitPressedOnce) {
                this.finish();
                System.exit(0);
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onItemClick(String item) {
        Log.d("Bottom", item);
    }

    private void moveLeft(ImageView target, ImageView imageView) {
        imageView.animate().setDuration(1000).translationXBy(-(imageView.getX() - target.getX() + target.getWidth() / 6) + target.getWidth() / 2).start();
    }

    private void moveRight(ImageView target, ImageView imageView) {
        imageView.animate().setDuration(1000).translationXBy(target.getX() + target.getWidth() / 2 - imageView.getX() - target.getWidth() / 6).start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
