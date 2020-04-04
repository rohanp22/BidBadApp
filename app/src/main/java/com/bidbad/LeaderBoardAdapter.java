package com.bidbad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bidbad.Models.Leader;
import com.wielabs.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardViewHolder> {

    Context context;
    ArrayList<Leader> leaders;

    public LeaderBoardAdapter(Context context, ArrayList<Leader> leaders) {
        this.context = context;
        this.leaders = leaders;
    }

    @NotNull
    @Override
    public LeaderBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderBoardViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LeaderBoardViewHolder holder, int position) {
        holder.name.setText(leaders.get(position).getName());
        holder.numberofbids.setText(leaders.get(position).getNoOfBids()+"");
        holder.badge.setText((position+1)+"");
    }

    @Override
    public int getItemCount() {
        return leaders.size();
    }

    static class LeaderBoardViewHolder extends RecyclerView.ViewHolder {

        TextView name, numberofbids, badge;

        LeaderBoardViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.userNameLeaderBoard);
            numberofbids = (TextView) itemView.findViewById(R.id.bidsCountLeaderBoard);
            badge = (TextView) itemView.findViewById(R.id.textBadge);
        }
    }
}
