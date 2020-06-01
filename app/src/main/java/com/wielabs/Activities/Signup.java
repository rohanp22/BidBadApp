package com.wielabs.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wielabs.Models.User;
import com.wielabs.Others.RequestHandler;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    private TextView create, terms;
    private ImageView back;
    private EditText firstName, lastName, email, password, address, pincode, referral;
    String phoneNumberString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstName = (EditText) findViewById(R.id.et_name_firstname);
        lastName = (EditText) findViewById(R.id.et_name_lastname);
        email = (EditText) findViewById(R.id.et_email_signup);
        password = (EditText) findViewById(R.id.et_password_signup);
        address = (EditText) findViewById(R.id.et_address);
        pincode = (EditText) findViewById(R.id.et_pincode);
        create = (TextView) findViewById(R.id.txt_create);
        referral = (EditText) findViewById(R.id.et_referral);
        Button btn = (Button) findViewById(R.id.signupbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/round.otf");

        create.setTypeface(custom_font);
        phoneNumberString = getIntent().getStringExtra("mobile");

        terms = (TextView) findViewById(R.id.txt_terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bidbad.com"));
                startActivity(intent);
                finish();
            }
        });

        back = (ImageView) findViewById(R.id.img_backS);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void registerUser() {

        if (TextUtils.isEmpty(firstName.getText().toString())) {
            firstName.setError("Please enter Name");
            firstName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(firstName.getText().toString())) {
            firstName.setError("Please enter last Name");
            firstName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Please enter your email");
            email.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Enter a password");
            password.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(address.toString())) {
            address.setError("Enter Address");
            address.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pincode.toString())) {
            pincode.setError("Enter Pincode");
            pincode.requestFocus();
            return;
        }

        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile", phoneNumberString);
                params.put("email", email.getText().toString());
                params.put("password", password.getText().toString());
                params.put("firstname", firstName.getText().toString());
                params.put("lastname", lastName.getText().toString());
                params.put("address", address.getText().toString());
                params.put("pincode", pincode.getText().toString());
                params.put("referral", referral.getText().toString());

                //returing the response
                return requestHandler.sendPostRequest("http://easyvela.esy.es/AndroidAPI/Login/Api.php?apicall=signup", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                Log.d("String msg", s);
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Log.d("Message", obj.getString("message"));
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("email"),
                                userJson.getString("firstname"),
                                userJson.getString("lastname"),
                                userJson.getString("address"),
                                userJson.getString("pincode"),
                                userJson.getString("mobile")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        startActivity(new Intent(Signup.this, Home.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }
    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
