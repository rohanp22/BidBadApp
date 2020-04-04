package com.bidbad.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wielabs.R;

public class ForgotPassword extends AppCompatActivity {

    private TextView title;
    private Button submit;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        title = (TextView) findViewById(R.id.txt_fp);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/round.otf");
        title.setTypeface(custom_font);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotPassword.this, Login.class);
        startActivity(intent);
        finish();
    }
}
