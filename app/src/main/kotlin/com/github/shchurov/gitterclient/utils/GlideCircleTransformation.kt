package com.github.shchurov.gitterclient.utils

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.github.shchurov.gitterclient.App

object GlideCircleTransformation : BitmapTransformation(App.context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int):
            Bitmap {
        val size = calcSize(toTransform)
        val bitmap = reuseOrCreateBitmap(size, pool)
        val shader = createShader(toTransform, size)
        paint.shader = shader
        draw(bitmap)
        return bitmap
    }

    private fun calcSize(original: Bitmap) = Math.min(original.width, original.height)

    private fun reuseOrCreateBitmap(size: Int, pool: BitmapPool) =
            pool.get(size, size, Bitmap.Config.ARGB_8888)
                    ?: Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

    private fun createShader(original: Bitmap, size: Int): Shader {
        val shader = BitmapShader(original, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val matrix = createSquareMatrix(original, size)
        shader.setLocalMatrix(matrix)
        return shader
    }

    private fun createSquareMatrix(original: Bitmap, size: Int): Matrix {
        val cutWidth = (original.width - size) / 2
        val cutHeight = (original.height - size) / 2
        val matrix = Matrix()
        matrix.setTranslate((-cutWidth).toFloat(), (-cutHeight).toFloat())
        return matrix
    }

    private fun draw(bitmap: Bitmap) {
        val canvas = Canvas(bitmap)
        val r = bitmap.width / 2f
        canvas.drawCircle(r, r, r, paint)
    }

    override fun getId(): String {
        return "circle"
    }

}