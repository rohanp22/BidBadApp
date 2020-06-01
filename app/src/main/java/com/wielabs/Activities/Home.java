package com.wielabs.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.wielabs.BuildConfig;
import com.wielabs.Download;
import com.wielabs.Fragments.ActionBottomDialogFragment;
import com.wielabs.Fragments.BidsHistory;
import com.wielabs.Fragments.FeedbackBottomDialogFragment;
import com.wielabs.Fragments.HomeFragment;
import com.wielabs.Fragments.MyBidsFragment;
import com.wielabs.Fragments.ProfileFragment;
import com.wielabs.Fragments.RewardFragment;
import com.wielabs.Others.RequestHandler;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ActionBottomDialogFragment.ItemClickListener, FeedbackBottomDialogFragment.ItemClickListener {

    FloatingActionButton fab;
    Fragment fragmentBefore, fragmentAfter;
    BottomAppBar bottomAppBar;
    ImageView reward, profile, results, indicator, home, blank;
    TextView homeText, rewardText, resultText, profileText;
    int TIME_OUT = 250;
    int dotPosition = 0;
    String ad;
    int noofbids = 0;
    private ConnectivityManager connectivityManager;
    private boolean isUpdateChecked = false;

    private int[] getDeviceDimensions() {
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;

        return new int[]{width, height};
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoactionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        connectivityManager.registerDefaultNetworkCallback(
                new ConnectivityManager.NetworkCallback() {
                    /**
                     * @param network
                     */
                    @Override
                    public void onAvailable(Network network) {
                        if (!isUpdateChecked)
                            checkUpdate(BuildConfig.VERSION_NAME);
                    }

                    /**
                     * @param network
                     */
                    @Override
                    public void onLost(Network network) {
                    }
                });


        blank = findViewById(R.id.blank);
        fab = findViewById(R.id.fabhome);
        loadFragment(new HomeFragment(), "home");

        bottomAppBar = findViewById(R.id.bar);
        home = findViewById(R.id.homeIcon);
        reward = findViewById(R.id.rewardsIcon);
        profile = findViewById(R.id.profileIcon);
        results = findViewById(R.id.resultIcon);
        homeText = findViewById(R.id.homeText);
        rewardText = findViewById(R.id.rewardsText);
        resultText = findViewById(R.id.resultsText);
        profileText = findViewById(R.id.profileText);
        indicator = (ImageView) findViewById(R.id.indicator);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

            }
        });

        rewardText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rewardsPressed();
            }
        });

        homeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homePressed();
            }
        });

        resultText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultsPressed();
            }
        });

        profileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePressed();
            }
        });

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
                rewardsPressed();
            }
//                if (fab.getVisibility() == View.GONE || fab.getVisibility() == View.INVISIBLE) {
//                    fab.show();
//                    blank.setVisibility(View.VISIBLE);
//                }
//                else {
//                    fab.hide();
//                    blank.setVisibility(View.GONE);
//                }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homePressed();
            }
        });

        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultsPressed();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                profilePressed();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new MyBidsFragment(), "mybids");
                indicator.setVisibility(View.INVISIBLE);
                homeText.setVisibility(View.VISIBLE);
                resultText.setVisibility(View.VISIBLE);
                rewardText.setVisibility(View.VISIBLE);
                profileText.setVisibility(View.VISIBLE);
                home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_gray, null));
                results.setImageDrawable(getResources().getDrawable(R.drawable.ic_results_gray, null));
                reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_rewards_gray, null));
            }
        });
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
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }


    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof HomeFragment) {
            if (doubleBackToExitPressedOnce) {
                this.finish();
                super.onBackPressed();
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
        } else {
            super.onBackPressed();
            fragmentBefore = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            Log.d("Fragment", fragmentBefore.getClass().getSimpleName());
            if (fragmentBefore.getClass().getSimpleName().equals("HomeFragment")) {
                home.setImageDrawable(getResources().getDrawable(R.drawable.ic_artboard_home, null));

                indicator.setVisibility(View.VISIBLE);
                moveLeft(home, indicator);
                dotPosition = 0;
                homeText.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rewardText.setVisibility(View.VISIBLE);
                        resultText.setVisibility(View.VISIBLE);
                        profileText.setVisibility(View.VISIBLE);
                        reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_rewards_gray, null));
                        results.setImageDrawable(getResources().getDrawable(R.drawable.ic_results_gray, null));
                    }
                }, TIME_OUT);
            } else if (fragmentBefore.getClass().getSimpleName().equals("BidsHistory")) {
                results.setImageDrawable(getResources().getDrawable(R.drawable.ic_results_new, null));

                indicator.setVisibility(View.VISIBLE);
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
                        profileText.setVisibility(View.VISIBLE);
                        reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_rewards_gray, null));
                        home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_gray, null));
                    }
                }, TIME_OUT);
            } else if (fragmentBefore.getClass().getSimpleName().equals("ProfileFragment")) {
                indicator.setVisibility(View.VISIBLE);
                Window window = Home.this.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(Home.this, android.R.color.transparent));
                moveRight(profile, indicator);
                dotPosition = 3;
                profileText.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        homeText.setVisibility(View.VISIBLE);
                        resultText.setVisibility(View.VISIBLE);
                        rewardText.setVisibility(View.VISIBLE);
                        home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_gray, null));
                        results.setImageDrawable(getResources().getDrawable(R.drawable.ic_results_gray, null));
                        reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_rewards_gray, null));
                    }
                }, TIME_OUT);
            } else if (fragmentBefore.getClass().getSimpleName().equals("RewardFragment")) {
                reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_medal, null));

                indicator.setVisibility(View.VISIBLE);
                if (dotPosition > 2)
                    moveLeft(reward, indicator);
                else
                    moveRight(reward, indicator);
                dotPosition = 2;
                rewardText.setVisibility(View.INVISIBLE);

                //animatefab();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        homeText.setVisibility(View.VISIBLE);
                        resultText.setVisibility(View.VISIBLE);
                        profileText.setVisibility(View.VISIBLE);
                        home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_gray, null));
                        results.setImageDrawable(getResources().getDrawable(R.drawable.ic_results_gray, null));
                    }
                }, TIME_OUT);
            } else if (fragmentBefore.getClass().getSimpleName().equals("MyBidsFragment")) {
                indicator.setVisibility(View.INVISIBLE);
                homeText.setVisibility(View.VISIBLE);
                resultText.setVisibility(View.VISIBLE);
                rewardText.setVisibility(View.VISIBLE);
                profileText.setVisibility(View.VISIBLE);
                home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_gray, null));
                results.setImageDrawable(getResources().getDrawable(R.drawable.ic_results_gray, null));
                reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_rewards_gray, null));
            }
        }
    }

    void homePressed() {
        home.setImageDrawable(getResources().getDrawable(R.drawable.ic_artboard_home, null));

        indicator.setVisibility(View.VISIBLE);
        moveLeft(home, indicator);
        dotPosition = 0;
        homeText.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rewardText.setVisibility(View.VISIBLE);
                resultText.setVisibility(View.VISIBLE);
                profileText.setVisibility(View.VISIBLE);
                reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_rewards_gray, null));
                results.setImageDrawable(getResources().getDrawable(R.drawable.ic_results_gray, null));
                loadFragment(new HomeFragment(), "home");
            }
        }, TIME_OUT);
    }

    void resultsPressed() {
        results.setImageDrawable(getResources().getDrawable(R.drawable.ic_results_new, null));

        indicator.setVisibility(View.VISIBLE);
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
                profileText.setVisibility(View.VISIBLE);
                reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_rewards_gray, null));
                home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_gray, null));
                loadFragment(new BidsHistory(), "bidhistoy");
            }
        }, TIME_OUT);
    }

    void profilePressed() {
        indicator.setVisibility(View.VISIBLE);
//        Window window = Home.this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(ContextCompat.getColor(Home.this, android.R.color.transparent));
        moveRight(profile, indicator);
        dotPosition = 3;
        profileText.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                homeText.setVisibility(View.VISIBLE);
                resultText.setVisibility(View.VISIBLE);
                rewardText.setVisibility(View.VISIBLE);
                home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_gray, null));
                results.setImageDrawable(getResources().getDrawable(R.drawable.ic_results_gray, null));
                reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_rewards_gray, null));

                loadFragment(new ProfileFragment(), "profile");
            }
        }, TIME_OUT);
    }

    void rewardsPressed() {
        reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_medal, null));

        indicator.setVisibility(View.VISIBLE);
        if (dotPosition > 2)
            moveLeft(reward, indicator);
        else
            moveRight(reward, indicator);
        dotPosition = 2;
        rewardText.setVisibility(View.INVISIBLE);

        //animatefab();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                homeText.setVisibility(View.VISIBLE);
                resultText.setVisibility(View.VISIBLE);
                profileText.setVisibility(View.VISIBLE);
                home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_gray, null));
                results.setImageDrawable(getResources().getDrawable(R.drawable.ic_results_gray, null));
                loadFragment(new RewardFragment(), "rewards");
            }
        }, TIME_OUT);
    }

    @Override
    public void onItemClick(String item) {
        Log.d("Bottom", item);
    }

    private void moveLeft(ImageView target, ImageView imageView) {
        imageView.animate().setDuration(TIME_OUT).translationXBy(-(imageView.getX() - target.getX() + target.getWidth() / 6) + target.getWidth() / 2).start();
    }

    private void moveRight(ImageView target, ImageView imageView) {
        imageView.animate().setDuration(TIME_OUT).translationXBy(target.getX() + target.getWidth() / 2 - imageView.getX() - target.getWidth() / 6).start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    private int dpToPx(View view, int dp) {
        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void checkUpdate(String versionName) {
        UpdateTask updateTask = new UpdateTask();
        updateTask.execute(versionName);

    }

    class UpdateTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("version", strings[0]);

            return requestHandler.sendPostRequest("http://easyvela.esy.es/Update/checkupdate.php", params);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.d("onPostExecute", "Executed");
                JSONObject jsonObject = new JSONObject(s);
                boolean isUpdateAvailable = jsonObject.getBoolean("update_available");
                if (isUpdateAvailable) {
                    final String fileName = jsonObject.getString("package_name");
                    final String downloadUrl = jsonObject.getString("update_url");
                    final String fileSize = jsonObject.getString("package_size");
                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this)
                            .setCancelable(false)
                            .setMessage("New update available")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Do nothing
                                }
                            });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.setMessage("Downloading Bidbad update...");
                            Download download = new Download(Home.this, dialog);
                            download.execute(downloadUrl, fileName, fileSize);
                            isUpdateChecked = true;
                        }
                    });
                } else
                    return;
            } catch (JSONException e) {
                isUpdateChecked = false;
                e.printStackTrace();
            }
        }
    }
}
