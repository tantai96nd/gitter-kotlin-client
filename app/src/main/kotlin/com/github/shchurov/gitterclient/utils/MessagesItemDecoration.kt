package com.github.shchurov.gitterclient.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.R

class MessagesItemDecoration() : RecyclerView.ItemDecoration() {

    companion object {
        private val WIDTH_DP = 1
        private val COLOR_ID = R.color.grey2
    }

    private val width: Int
    private val paint: Paint = Paint()

    init {
        width = Utils.dpToPx(WIDTH_DP)
        paint.color = App.context.resources.getColor(COLOR_ID)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State?) {
        if (isFirstItem(parent, view))
            return
        outRect.top = width
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = 0f
        val right = parent.width.toFloat()
        for (i in 0..parent.childCount - 1) {
            val child = parent.getChildAt(i)
            if (isFirstItem(parent, child))
                continue
            val top = child.top.toFloat()
            val bottom = top + WIDTH_DP
            c.drawRect(left, top, right, bottom, paint)
        }
    }

    private fun isFirstItem(parent: RecyclerView, view: View) =
            when (parent.getChildLayoutPosition(view)) {
                0, RecyclerView.NO_POSITION -> true
                else -> false
            }

}