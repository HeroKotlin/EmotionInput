package com.github.herokotlin.emotioninput

import android.widget.ImageView

abstract class EmotionInputConfiguration {

    /**
     * 加载图片
     */
    abstract fun loadImage(imageView: ImageView, url: String)

}