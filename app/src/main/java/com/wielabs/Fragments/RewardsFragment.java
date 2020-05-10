package com.wielabs.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wielabs.R;
import com.wielabs.ScratchCard;

import java.util.ArrayList;

public class RewardsFragment extends Fragment {

    Dialog dialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        dialog = new Dialog(view.getContext());
//        ScratchView scratchView = view.findViewById(R.id.scratch_view);
//        scratchView.setRevealListener(new ScratchView.IRevealListener() {
//            @Override
//            public void onRevealed(ScratchView scratchView) {
//                Toast.makeText(getApplicationContext(), "Reveled", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
//                if (percent >= 0.5) {
//                    Log.d("Reveal Percentage", "onRevealPercentChangedListener: " + String.valueOf(percent));
//                    scratchView.setVisibility(View.GONE);
//                }
//            }
//        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRewards);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.setAdapter(new ScratchCardAdapter(view.getContext()));

        return view;
    }

    public class ScratchCardAdapter extends RecyclerView.Adapter<ScratchCardAdapter.ScratchCardHolderView> {

        public Context context;
        ArrayList<String> colors;

        public ScratchCardAdapter(Context context) {
            this.context = context;
            colors = new ArrayList<>();
            colors.add("#32CD32");
            colors.add("#0000A0");
            colors.add("#FFA500");
        }

        @NonNull
        @Override
        public ScratchCardAdapter.ScratchCardHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ScratchCardAdapter.ScratchCardHolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.scratch_card, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ScratchCardHolderView holder, int position) {
//            ((GradientDrawable)holder.imageView.getDrawable()).setColor(Color.parseColor(colors.get(position%3)));
            if (position == 2 || position == 1 || position == 0) {
                holder.textView.setVisibility(View.VISIBLE);
            }
            if (position == 5) {
                holder.textView.setVisibility(View.VISIBLE);
                holder.textView.setText("You won 5 Bid coins");
                //      ((GradientDrawable)holder.imageView.getDrawable()).setColor(Color.parseColor("#e1e1e1"));
            }
        }


        @Override
        public int getItemCount() {
            return 12;
        }

        class ScratchCardHolderView extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView textView;
            ScratchCard scratchView;

            public ScratchCardHolderView(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textunlock);
                imageView = itemView.findViewById(R.id.imageScratch);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.setContentView(R.layout.dialog_scratch);
                        scratchView = dialog.findViewById(R.id.scratch_view);
                        scratchView.setmDrawable(getResources().getDrawable(R.drawable.ic_wishlist, null));
                        dialog.show();
                    }
                });
            }
        }
    }
}