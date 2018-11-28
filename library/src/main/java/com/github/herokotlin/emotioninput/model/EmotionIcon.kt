package com.github.herokotlin.emotioninput.model

data class EmotionIcon(

    // 当前是第几套图标
    val index: Int = 0,

    // 本地图标
    val localImage: Int = 0,

    // 是否是选中状态
    val selected: Boolean = false

)