package com.wielabs.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wielabs.Fragments.SettingsFragment;
import com.wielabs.Models.SettingObj;
import com.wielabs.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private List<SettingObj> heroList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        heroList = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.profilelist);

        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "How to bid"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "Send Feedback"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "Terms and conditions"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "Privacy Policy"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "Logout"));

        MyListAdapter adapter = new MyListAdapter(ProfileActivity.this, R.layout.profile_list_layout, heroList);

        //attaching adapter to the listview
        listView.setAdapter(adapter);
    }

    public class MyListAdapter extends ArrayAdapter<SettingObj> {

        //the list values in the List of type hero
        List<SettingObj> heroList;

        //activity context
        Context context;

        //the layout resource file for the list items
        int resource;

        //constructor initializing the values
        MyListAdapter(Context context, int resource, List<SettingObj> heroList) {
            super(context, resource, heroList);
            this.context = context;
            this.resource = resource;
            this.heroList = heroList;
        }

        //this will return the ListView Item as a View
        @NonNull
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            //we need to get the view of the xml for our list item
            //And for this we need a layoutinflater
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            //getting the view
            @SuppressLint("ViewHolder") View view = layoutInflater.inflate(resource, null, false);

            //getting the view elements of the list from the view
            ImageView imageView = view.findViewById(R.id.imageSetting);
            TextView textViewName = view.findViewById(R.id.textSetting);

            //getting the hero of the specified position
            SettingObj hero = heroList.get(position);

            //adding values to the list item
            imageView.setImageDrawable(context.getResources().getDrawable(hero.getImage()));
            textViewName.setText(hero.getName());

            //finally returning the view
            return view;
        }
    }
}
