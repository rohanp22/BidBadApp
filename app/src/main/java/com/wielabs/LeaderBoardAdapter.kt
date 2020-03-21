package com.example.bidbadsample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wielabs.R

class LeaderBoardAdapter : RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderBoardViewHolder {
        return LeaderBoardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_leaderboard, parent, false))
    }

    override fun getItemCount(): Int {
        return 15
    }

    override fun onBindViewHolder(holder: LeaderBoardViewHolder, position: Int) {
    }

    class LeaderBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}