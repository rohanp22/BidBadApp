package com.wielabs;

import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemBackgroundDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        switch (parent.getChildAdapterPosition(view)){
            case 0 : view.setBackgroundColor(Color.parseColor("#ffd94a"));
            break;

            case 1 : view.setBackgroundColor(Color.parseColor("#d7d8c8"));
            break;

            case 2 : view.setBackgroundColor(Color.parseColor("#f4aa6b"));
            break;

            default:view.setBackgroundColor(Color.parseColor("#f9f9f9"));
        }
    }
}
