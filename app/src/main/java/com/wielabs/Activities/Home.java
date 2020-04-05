package com.wielabs.Activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wielabs.Fragments.BidsHistory;
import com.wielabs.Fragments.PrivacyPolicy;
import com.wielabs.Fragments.ProfileFragment;
import com.wielabs.Fragments.SendFeedback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.wielabs.Fragments.ActionBottomDialogFragment;
import com.wielabs.Fragments.FeedbackBottomDialogFragment;
import com.wielabs.Fragments.HomeFragment;
import com.wielabs.Fragments.MyBidsFragment;
import com.wielabs.Fragments.MyOrdersFragment;
import com.wielabs.Fragments.PastFragment;
import com.wielabs.Fragments.SettingsFragment;
import com.wielabs.Fragments.TermsAndConditions;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ActionBottomDialogFragment.ItemClickListener, FeedbackBottomDialogFragment.ItemClickListener {

    Fragment fragment;
    public BottomNavigationView navigation;
    FloatingActionButton fab;
    BottomAppBar nav;
    BottomAppBar bottomAppBar;
    ImageView reward, profile, results;
    ImageView blank;
    LinearLayout linearLayout;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        blank = findViewById(R.id.blankIcon);
        linearLayout = findViewById(R.id.linearLayout);
        fab = findViewById(R.id.fabhome);
        bottomAppBar = findViewById(R.id.bar);
        fab.setScaleX(0.0f);
        fab.setScaleY(0.0f);

        new Handler().postDelayed(new Runnable() {
            @SuppressLint("RestrictedApi")
            @Override
            public void run() {

                fab.setVisibility(View.VISIBLE);
                ValueAnimator m1 = ValueAnimator.ofFloat(0, 1); //fromWeight, toWeight
                        m1.setDuration(500);
                        m1.setInterpolator(new LinearInterpolator());
                        m1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) blank.getLayoutParams();
                                l.weight = (float) animation.getAnimatedValue();
                                blank.setLayoutParams(l);
                            }
                        });
                        m1.start();

                fab.animate()
                        .setDuration(200)
                        .scaleX(1.0f)           //Scaling to 110%
                        .scaleY(1.0f)
                        .start();

                //loadFragment(new HomeFragment(), "home");
            }
        }, 1000);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadFragment(new HomeFragment(), "home");
//            }
//        }, 1000);

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

        home = findViewById(R.id.homeIcon);
        TextView homeText = findViewById(R.id.homeText);
        final TextView rewardText = findViewById(R.id.rewardsText);
        reward = findViewById(R.id.rewardsIcon);
        profile = findViewById(R.id.profileIcon);
        results = findViewById(R.id.resultIcon);

        profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black_24dp, null));
        reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_greycolor_01, null));

        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rewardText.setCompoundDrawablesRelative(getResources().getDrawable(R.drawable.circular_bg, null), null, null, null);
                rewardText.setText(null);
                reward.setImageDrawable(getResources().getDrawable(R.drawable.rewards_animated, null));
                final AnimatedVectorDrawable rewardanimation = (AnimatedVectorDrawable) reward.getDrawable();
                rewardanimation.start();
                profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black_24dp, null));
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_greycolor_01));
                loadFragment(new HomeFragment(), "home");
            }
        });

        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_greycolor_01));
                loadFragment(new BidsHistory(), "past");
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                results.setBackgroundTintList(getColorStateList(R.color.grey_titn));
                profile.setImageDrawable(getResources().getDrawable(R.drawable.profile_animated));
                reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_greycolor_01));
                final AnimatedVectorDrawable v = (AnimatedVectorDrawable) profile.getDrawable();
                v.start();
                loadFragment(new ProfileFragment(), "profile");
            }
        });

        nav = findViewById(R.id.bar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new MyBidsFragment())
                        .commit();
                //navigation.setSelectedItemId(R.id.mybid);
            }
        });

//        final LinearLayout home = findViewById(R.id.homeLayout);
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                YoYo.with(Techniques.RotateInUpLeft)
//                        .duration(700)
//                        .playOn(home);
//                loadFragment(new HomeFragment(), "home");
//            }
//        });
//
//        LinearLayout completed = findViewById(R.id.completedLayout);
//        completed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadFragment(new PastFragment(), "past");
//            }
//        });

        Window window = Home.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Home.this, R.color.colorPrimary));

//        bottomAppBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_home_black_24dp));

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("home", "hh");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }
        });
    }

    void sendToken(String token){
        int id = SharedPrefManager.getInstance(Home.this).getUser().getId();
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/updateToken.php?id="+id+"&token="+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue2 = Volley.newRequestQueue(Home.this);
        requestQueue2.add(stringRequest2);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem item = menu.findItem(R.id.navigation_home);
//        item.setEnabled(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    String name;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragment = null;
        name = null;

        item.setChecked(true);
        switch (item.getItemId()) {
//            case R.id.navigation_home:
//                fragment = new HomeFragment();
//                name = "home";
//                break;

//            case R.id.navigation_mybids:
//                fragment = new MyBidsFragment();
//                name = "mybids";
//                break;
//
//            case R.id.navigation_notifications:
//                fragment = new NotificationsFragment();
//                name = "past";
//                break;
//
//            case R.id.navigation_settings:
//                fragment = new SettingsFragment();
//                name = "settings";
//                break;
        }
        return loadFragment(fragment, name);
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
        if(currentFragment instanceof ProductDescription){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            bottomAppBar.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        }

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
        } else if (currentFragment instanceof MyOrdersFragment || currentFragment instanceof SendFeedback || currentFragment instanceof TermsAndConditions || currentFragment instanceof PrivacyPolicy) {
            Log.v("frags", "find the current fragment");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        }
        else if(currentFragment instanceof MyBidsFragment || currentFragment instanceof PastFragment || currentFragment instanceof SettingsFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            bottomAppBar.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(String item) {
        Log.d("Bottom", item);
    }
}
