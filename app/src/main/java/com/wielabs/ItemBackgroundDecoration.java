package com.wielabs;

import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;

public class ItemBackgroundDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        CircularImageView circularImageView = view.findViewById(R.id.profilePicLeaderBoard);

        switch (parent.getChildAdapterPosition(view)) {
            case 0:
                circularImageView.setBorderColor(Color.parseColor("#ffd94a"));
                break;

            case 1:
                circularImageView.setBorderColor(Color.parseColor("#d7d8c8"));
                break;

            case 2:
                circularImageView.setBorderColor(Color.parseColor("#f4aa6b"));
                break;

            default:
                circularImageView.setBorderColor(Color.parseColor("#f9f9f9"));
        }

//        switch (parent.getChildAdapterPosition(view)){
//            case 0 : ((GradientDrawable)view.findViewById(R.id.constraintLayout).getBackground()).setColor(Color.parseColor("#ffd94a"));
//            break;
//
//            case 1 : ((GradientDrawable)view.findViewById(R.id.constraintLayout).getBackground()).setColor(Color.parseColor("#d7d8c8"));
//            break;
//
//            case 2 : ((GradientDrawable)view.findViewById(R.id.constraintLayout).getBackground()).setColor(Color.parseColor("#f4aa6b"));
//            break;
//
//            default:((GradientDrawable)view.findViewById(R.id.constraintLayout).getBackground()).setColor(Color.parseColor("#f9f9f9"));
//        }
    }
}
