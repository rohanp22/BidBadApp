package com.wielabs.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wielabs.Fragments.HomeFragment;
import com.wielabs.Fragments.MyBidsFragment;
import com.wielabs.Models.Current_Product;
import com.wielabs.R;
import com.wielabs.Others.RequestHandler;
import com.wielabs.Others.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PlaceBid extends AppCompatActivity {

    EditText e;
    Current_Product c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_bid);

        Intent intent = getIntent();
        c = (Current_Product) intent.getSerializableExtra("id");

        Button b = (Button) findViewById(R.id.placeyourbid);
        e = (EditText) findViewById(R.id.bidamount);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrderToDB();

            }
        });
    }

    private void sendOrderToDB() {

        if (TextUtils.isEmpty(e.getText().toString())) {
            e.setError("Please enter amount");
            e.requestFocus();
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
                params.put("id", SharedPrefManager.getInstance(PlaceBid.this).getUser().getId()+"");
                params.put("bidamount", e.getText().toString());
                params.put("productid", c.getId());
                params.put("type", "Paid to enter bidding for "+c.getTitle());


                //returing the response
                return requestHandler.sendPostRequest("http://easyvela.esy.es/AndroidAPI/addtomybids.php", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion

                try {
                    Intent i = new Intent(PlaceBid.this, Home.class);
                    i.putExtra("A", "bid");
                    startActivity(i);
                    finish();
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //Toast.makeText(PlaceBid.this, obj.getString("message"), Toast.LENGTH_LONG);

                }
                 catch (JSONException e) {
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

    }
}