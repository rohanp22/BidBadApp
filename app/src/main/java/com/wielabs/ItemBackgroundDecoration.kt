package com.example.bidbadsample

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemBackgroundDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (parent.getChildAdapterPosition(view)) {
            0 -> {
                (view.background as GradientDrawable).color =
                    ColorStateList.valueOf(Color.parseColor("#ffd94a"))
            }
            1 -> {
                (view.background as GradientDrawable).color =
                    ColorStateList.valueOf(Color.parseColor("#d7d8c8"))
            }
            2 -> {
                (view.background as GradientDrawable).color =
                    ColorStateList.valueOf(Color.parseColor("#f4aa6b"))
            }
            else -> {
                (view.background as GradientDrawable).color =
                    ColorStateList.valueOf(Color.parseColor("#f9f9f9"))
            }
        }
    }
}