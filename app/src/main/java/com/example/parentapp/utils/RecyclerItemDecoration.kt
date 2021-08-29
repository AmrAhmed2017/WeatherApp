package com.example.parentapp.utils

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemDecoration(private val itemOffset: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        super.getItemOffsets(outRect, itemPosition, parent)
        outRect.set(itemOffset, itemOffset, itemOffset, itemOffset)
    }
}