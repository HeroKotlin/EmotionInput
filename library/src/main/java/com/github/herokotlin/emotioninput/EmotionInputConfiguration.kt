package com.github.herokotlin.emotioninput

import android.content.Context
import android.widget.ImageView

abstract class EmotionInputConfiguration(val context: Context) {

    /**
     * 加载图片
     */
    abstract fun loadImage(imageView: ImageView, url: String)

}