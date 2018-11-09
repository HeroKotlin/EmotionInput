package com.github.herokotlin.emotioninput

import android.content.Context
import android.widget.ImageView

interface EmotionInputConfiguration {

    /**
     * 加载图片
     */
    fun loadImage(context: Context, imageView: ImageView, url: String)

}