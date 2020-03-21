package com.example.bidbadsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wielabs.R
import kotlinx.android.synthetic.main.fragment_leaderboard.view.*

/**
 * A simple [Fragment] subclass.
 */
class LeaderBoardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.leaderBoardRecyclerView.apply {
            addItemDecoration(ItemBackgroundDecoration())
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = LeaderBoardAdapter()
        }
    }
}
