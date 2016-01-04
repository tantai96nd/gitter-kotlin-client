package com.github.shchurov.gitterclient.utils

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.github.shchurov.gitterclient.App

object GlideCircleTransformation : BitmapTransformation(App.context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int):
            Bitmap {
        val size = Math.min(toTransform.width, toTransform.height)
        val cutWidth = (toTransform.width - size) / 2
        val cutHeight = (toTransform.height - size) / 2
        var bitmap = pool.get(size, size, Bitmap.Config.ARGB_8888)
                ?: Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        if (cutWidth != 0 || cutHeight != 0) {
            val matrix = Matrix()
            matrix.setTranslate((-cutWidth).toFloat(), (-cutHeight).toFloat())
            shader.setLocalMatrix(matrix)
        }
        paint.setShader(shader)
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        return bitmap
    }

    override fun getId(): String {
        return "circle"
    }

}