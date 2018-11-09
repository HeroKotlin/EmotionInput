package com.github.herokotlin.emotioninput.filter

import com.github.herokotlin.emotioninput.model.Emotion

// 参考 https://stackoverflow.com/questions/24840667/what-is-the-regex-to-extract-all-the-emojis-from-a-string
class EmojiFilter(emotionList: List<Emotion>) : EmotionFilter("[${emotionList.map { it.code }.joinToString("")}]", emotionList)