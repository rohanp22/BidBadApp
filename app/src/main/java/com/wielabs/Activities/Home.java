package com.wielabs.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.wielabs.Fragments.PrivacyPolicy;
import com.wielabs.Fragments.SendFeedback;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ActionBottomDialogFragment.ItemClickListener, FeedbackBottomDialogFragment.ItemClickListener {

    Fragment fragment;
    public BottomNavigationView navigation;
    FloatingActionButton fab;
    BottomAppBar nav;
    BottomAppBar bottomAppBar;
    TextView homeText, resultsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("Token", token);
                        sendToken(token);
                        //Toast.makeText(Home.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        fragment = new HomeFragment();
        loadFragment(new HomeFragment(), "Home");
//        navigation = findViewById(R.id.nav_view);
//        navigation.setOnNavigationItemSelectedListener(this);
        nav = findViewById(R.id.bar);


        homeText = findViewById(R.id.homeText);
        resultsText = findViewById(R.id.resultsText);

        fab = findViewById(R.id.fabhome);
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

        LinearLayout home = findViewById(R.id.homeLayout);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new HomeFragment(), "home");
            }
        });

        LinearLayout completed = findViewById(R.id.completedLayout);
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new PastFragment(), "past");
            }
        });

        Window window = Home.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Home.this, R.color.colorPrimary));

            bottomAppBar = (BottomAppBar) findViewById(R.id.bar);
//        bottomAppBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_home_black_24dp));


        setSupportActionBar(bottomAppBar);

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
        //getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
//        menu.add("Settings");
//        menu.add("Help");
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

    private boolean loadFragment(Fragment fragment, String name) {

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
