package com.github.herokotlin.emotioninput.model

data class EmotionPage(

    // 每页的图标列表
    val emotionList: List<Emotion> = listOf(),

    // 每页有多少列
    val columns: Int = 0,

    // 每页有多少行
    val rows: Int = 0,

    // 显示宽度
    var width: Int = 0,

    // 显示高度
    var height: Int = 0,

    // 是否显示删除按钮
    val hasDeleteButton: Boolean = false

)