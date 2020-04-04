package com.wielabs.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wielabs.Models.Current_Product;
import com.wielabs.Models.NotificationModel;
import com.wielabs.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    public ArrayList<Current_Product> pastItems;
    public List<String> listt;
    ListView pastList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notifications, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listt = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("bidbadsharedpref", Context.MODE_PRIVATE);
        String csvList = sharedPreferences.getString("myList", null);
        assert csvList != null;
        String[] items = csvList.split(",");
        List<String> list = new ArrayList<String>();
        for(int i=0; i < items.length; i++){
            list.add(items[i]);
            Log.d("items", items[i]);
        }


    }


}

class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> {


    private List<NotificationModel> heroList;
    private Context context;

    private static int currentPosition = 0;

    public HeroAdapter(List<NotificationModel> heroList, Context context) {
        this.heroList = heroList;
        this.context = context;
    }

    @Override
    public HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationlayout, parent, false);
        return new HeroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HeroViewHolder holder, final int position) {
        NotificationModel hero = heroList.get(position);
        holder.textViewName.setText(hero.getHeader());
        holder.textViewMessage.setText(hero.getMessage());

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

            //toggling visibility
            holder.linearLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.linearLayout.startAnimation(slideDown);
        }

        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloding the list
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    class HeroViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewMessage;
        LinearLayout linearLayout;

        HeroViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewMessage = (TextView) itemView.findViewById(R.id.notification_message);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}

