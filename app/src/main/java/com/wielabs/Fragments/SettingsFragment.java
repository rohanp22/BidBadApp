package com.wielabs.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wielabs.Activities.AddMoney;
import com.wielabs.Activities.Login;
import com.wielabs.Models.SettingObj;
import com.wielabs.R;
import com.wielabs.Others.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingsFragment extends Fragment implements FeedbackBottomDialogFragment.ItemClickListener{

    private List<SettingObj> heroList;

    @Override
    public void onItemClick(String item) {
        Log.d("Selected action item " + item, "yes");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

//        Button b = (Button) view.findViewById(R.id.logoutBtn);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPrefManager.getInstance(getContext()).logout();
//                startActivity(new Intent(getActivity(), Login.class));
//                ((Activity) getActivity()).overridePendingTransition(0, 0);
//            }
//        });


        TextView name = view.findViewById(R.id.nameSetting);
        TextView mobile = view.findViewById(R.id.mobileSetting);
        String n = SharedPrefManager.getInstance(getActivity()).getUser().getFirstname();
        n = n.substring(0, 1).toUpperCase() + n.substring(1);
        name.setText(n);
        mobile.setText(SharedPrefManager.getInstance(getActivity()).getUser().getMobile());
        heroList = new ArrayList<>();
        ListView listView = (ListView) view.<View>findViewById(R.id.settingsListview);

        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "My Orders"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "My Wallet"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "Notifications"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "How to bid"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "Send Feedback"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "Invite friends"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "Help & Support"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "Terms and conditions"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "Privacy Policy"));
        heroList.add(new SettingObj(R.drawable.ic_shopping_basket_black_24dp, "Logout"));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), heroList.get(i) + "", Toast.LENGTH_LONG);
                Log.d("Setting", heroList.get(i).getName() + "");
                switch (heroList.get(i).getName()) {
                    case "My Orders":
                        MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
                        assert getFragmentManager() != null;
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, myOrdersFragment).addToBackStack(null).commit();
                        break;

                    case "Logout":
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");

                        //if the response is positive in the alert
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPrefManager.getInstance(getContext()).logout();
                                startActivity(new Intent(getActivity(), Login.class));
                                ((Activity) Objects.requireNonNull(getActivity())).overridePendingTransition(0, 0);
                            }
                        });

                        //if response is negative nothing is being done
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        //creating and displaying the alert dialog
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;

                    case "Invite friends":
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Try this app");
                        startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                        break;

                    case "Help & Support":
                        break;

                    case "Send Feedback":
                        showBottomSheet();
//                        SendFeedback ldf = new SendFeedback();
//                        assert getFragmentManager() != null;
//                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, ldf).addToBackStack(null).commit();
                        break;

                    case "Terms and conditions":
                        TermsAndConditions tac = new TermsAndConditions();
                        assert getFragmentManager() != null;
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, tac).addToBackStack(null).commit();
                        break;

                    case "Privacy Policy":
                        assert getFragmentManager() != null;
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrivacyPolicy()).addToBackStack(null).commit();
                        break;

                    case "My Wallet":
                        startActivity(new Intent(view.getContext(), AddMoney.class));
                        getActivity().finish();
                        break;
                }
            }
        });

        //creating the adapter
        MyListAdapter adapter = new MyListAdapter(getActivity(), R.layout.setting_layout, heroList);

        //attaching adapter to the listview
        listView.setAdapter(adapter);

        return view;
    }

    public void showBottomSheet() {
        Log.d("yes","yes");
        FeedbackBottomDialogFragment addPhotoBottomDialogFragment =
                FeedbackBottomDialogFragment.newInstance();
        addPhotoBottomDialogFragment.show(getFragmentManager(),
                FeedbackBottomDialogFragment.TAG);
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
