package com.github.herokotlin.emotioninput.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

// https://stackoverflow.com/questions/25628258/align-text-around-imagespan-center-vertical
class EmotionSpan(drawable: Drawable): ImageSpan(drawable) {

    override fun getSize(paint: Paint?, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {

        val rect = drawable.bounds

        if (fm != null) {

            val pfm = paint?.fontMetricsInt

            if (pfm != null) {
                // keep it the same as paint's fm
                fm.ascent = pfm.ascent
                fm.descent = pfm.descent
                fm.top = pfm.top
                fm.bottom = pfm.bottom
            }

        }

        return rect.right

    }

    override fun draw(canvas: Canvas?, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint?) {

        if (canvas != null) {

            val pfm = paint?.fontMetricsInt

            if (pfm != null) {

                canvas.save()

                var transY = bottom - drawable.bounds.bottom

                transY -= paint.fontMetricsInt.descent / 2

                canvas.translate(x, transY.toFloat())

                drawable.draw(canvas)

                canvas.restore()

            }


        }


    }
}