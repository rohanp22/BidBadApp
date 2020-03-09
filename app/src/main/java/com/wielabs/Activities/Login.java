package com.wielabs.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wielabs.Others.AccountKitLoginActivity;
import com.wielabs.R;
import com.wielabs.Others.RequestHandler;
import com.wielabs.Others.SharedPrefManager;
import com.wielabs.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    private TextView welcome;
    private TextView signup, forgotpassword;
    private Button login;
    private EditText etmobile, etpassword;
    private TextView mobile,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etmobile = (EditText) findViewById(R.id.et_mno);
        etpassword = (EditText) findViewById(R.id.et_pass);
        welcome = (TextView) findViewById(R.id.txt_wel);



        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/round.otf");

        welcome.setTypeface(custom_font);

        signup = (TextView) findViewById(R.id.txt_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, AccountKitLoginActivity.class);
                startActivity(intent);
            }
        });

        forgotpassword = (TextView) findViewById(R.id.txt_fp);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

        login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {

        final String mobile = etmobile.getText().toString();
        final String password = etpassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(mobile)) {
            etmobile.setError("Please enter Mobile Number");
            etmobile.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etpassword.setError("Please enter Password");
            etpassword.requestFocus();
            return;
        }

        class UserLogin extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");
                        Log.d("ID ", userJson.getInt("id")+"");
                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("email"),
                                userJson.getString("firstname"),
                                userJson.getString("lastname"),
                                userJson.getString("address"),
                                userJson.getString("mobile"),
                                userJson.getString("pincode")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid mobile number or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile", "+91"+mobile);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest("http://easyvela.esy.es/AndroidAPI/Login/Api.php?apicall=login", params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    @Override
    public void onBackPressed() {

    }
}
