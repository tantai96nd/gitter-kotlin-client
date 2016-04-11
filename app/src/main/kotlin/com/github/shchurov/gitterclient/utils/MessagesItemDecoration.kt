package com.github.shchurov.gitterclient.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.R

@Suppress("DEPRECATION")
class MessagesItemDecoration() : RecyclerView.ItemDecoration() {

    companion object {
        private val WIDTH_DP = 1
        private val COLOR_ID = R.color.grey3
    }

    private val width = Utils.dpToPx(WIDTH_DP)
    private val paint: Paint = Paint()

    init {
        paint.color = App.context.resources.getColor(COLOR_ID)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State?) {
        if (!isFirstItem(parent, view)) {
            outRect.bottom = width
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = 0f
        val right = parent.width.toFloat()
        for (i in 0..parent.childCount - 1) {
            val child = parent.getChildAt(i)
            if (isFirstItem(parent, child))
                continue
            val top = child.translationY + child.bottom.toFloat()
            val bottom = top + WIDTH_DP
            // to keep divider alpha same as child alpha
            paint.alpha = (255 * child.alpha).toInt()
            c.drawRect(left, top, right, bottom, paint)
        }
    }

    private fun isFirstItem(parent: RecyclerView, view: View): Boolean {
        val childPosition = parent.getChildLayoutPosition(view)
        return when (childPosition) {
            0, RecyclerView.NO_POSITION -> true
            else -> false
        }
    }

}