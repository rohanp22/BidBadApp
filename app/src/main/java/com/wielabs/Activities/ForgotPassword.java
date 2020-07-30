package com.wielabs.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wielabs.R;

public class ForgotPassword extends AppCompatActivity {

    private TextView title;
    private Button submit;
    private EditText email;
    TextView editTextCountryCode;
    TextView editTextPhone;
    Button buttonContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        title = (TextView) findViewById(R.id.txt_fp);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/round.otf");
        title.setTypeface(custom_font);

        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.button_submit_forgotpassword);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCountryCode.getText().toString().trim();
                String number = editTextPhone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editTextPhone.setError("Valid number is required");
                    editTextPhone.requestFocus();
                    return;
                }

                final String phoneNumber = code + number;

                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "http://easyvela.esy.es/AndroidAPI/isuserregistered.php?mobile=" + phoneNumber,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("0")) {
                                    Intent intent = new Intent(ForgotPassword.this, VerifyForgotPassword.class);
                                    intent.putExtra("phoneNumber", phoneNumber);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ForgotPassword.this, "User already Registered", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });

                RequestQueue requestQueue2 = Volley.newRequestQueue(ForgotPassword.this);
                requestQueue2.add(stringRequest2);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotPassword.this, Login.class);
        startActivity(intent);
        finish();
    }
}
