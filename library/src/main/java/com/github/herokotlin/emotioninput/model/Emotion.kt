package com.github.herokotlin.emotioninput.model

data class Emotion(
    // 表情值
    val code: String = "",

    // 显示在图片下方的文本
    val name: String = "",

    // 表情对应的本地图片
    val imageId: Int = 0,

    // 表情对应的网络图片
    val imageUrl: String = "",

    // 是否支持在输入框显示
    val inline: Boolean = true
)