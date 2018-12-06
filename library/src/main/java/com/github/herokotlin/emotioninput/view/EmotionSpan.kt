package com.github.herokotlin.emotioninput.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

/**
 * https://stackoverflow.com/questions/25628258/align-text-around-imagespan-center-vertical
 */

class EmotionSpan(drawable: Drawable): ImageSpan(drawable) {

    override fun getSize(paint: Paint?, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return drawable.bounds.bottom
    }

    override fun draw(canvas: Canvas?, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint?) {

        val fm = paint?.fontMetricsInt

        if (canvas != null && fm != null) {

            val image = drawable

            // y 是基线，ascent 是负数，descent 是正数
            val textTop = y + fm.ascent
            val textBottom = y + fm.descent

            // 文本的中点 - 图片的中点
            val transY = (textTop + textBottom) / 2 - image.bounds.bottom / 2

            canvas.save()

            canvas.translate(x, transY.toFloat())

            drawable.draw(canvas)

            canvas.restore()

        }

    }
}