package com.bidbad.Activities;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bidbad.Fragments.PrivacyPolicy;
import com.bidbad.Fragments.ProfileFragment;
import com.bidbad.Fragments.SendFeedback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.bidbad.Fragments.ActionBottomDialogFragment;
import com.bidbad.Fragments.FeedbackBottomDialogFragment;
import com.bidbad.Fragments.HomeFragment;
import com.bidbad.Fragments.MyBidsFragment;
import com.bidbad.Fragments.MyOrdersFragment;
import com.bidbad.Fragments.PastFragment;
import com.bidbad.Fragments.SettingsFragment;
import com.bidbad.Fragments.TermsAndConditions;
import com.bidbad.Others.SharedPrefManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        blank = (ImageView) findViewById(R.id.blankIcon);

        fab = findViewById(R.id.fabhome);
        fragment = new HomeFragment();
        loadFragment(new HomeFragment(), "Home");
        ImageView home = (ImageView) findViewById(R.id.homeIcon);
        reward = (ImageView) findViewById(R.id.rewardsIcon);
        profile = (ImageView) findViewById(R.id.profileIcon);
        results = (ImageView) findViewById(R.id.resultIcon);

        results.setBackgroundTintList(getColorStateList(R.color.grey_titn));
        profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black_24dp));
        reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_greycolor_01));
        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reward.setImageDrawable(getResources().getDrawable(R.drawable.rewars_icon));
                final AnimatedVectorDrawable rewardanimation = (AnimatedVectorDrawable) reward.getDrawable();
                rewardanimation.start();
                results.setBackgroundTintList(getColorStateList(R.color.grey_titn));
                profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black_24dp));

//                fab.animate()
//                        .rotationBy(360)        // rest 180 covered by "shrink" animation
//                        .setDuration(1000)
//                        .scaleX(0.7f)           //Scaling to 110%
//                        .scaleY(0.7f)           //Scaling to 110%
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                ValueAnimator m1 = ValueAnimator.ofFloat(5, 4); //fromWeight, toWeight
//                                m1.setDuration(1000);
//                                m1.setInterpolator(new LinearInterpolator());
//                                m1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                    @Override
//                                    public void onAnimationUpdate(ValueAnimator animation) {
//                                        linearLayout.setWeightSum((float) animation.getAnimatedValue());
//                                        linearLayout.requestLayout();
//                                    }
//                                });
//                                //m1.start();
//                                blank.setVisibility(View.GONE);
//                                linearLayout.setWeightSum(4);
//
//                                fab.setVisibility(View.GONE);
//                                //Chaning the icon by the end of animation
//                            }
//                        })
//                        .start();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_greycolor_01));
                results.setBackgroundTintList(getColorStateList(R.color.grey_titn));
                loadFragment(new HomeFragment(), "home");
            }
        });

        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_greycolor_01));
                loadFragment(new PastFragment(), "past");
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                results.setBackgroundTintList(getColorStateList(R.color.grey_titn));
                profile.setImageDrawable(getResources().getDrawable(R.drawable.data));
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

        bottomAppBar = (BottomAppBar) findViewById(R.id.bar);
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
