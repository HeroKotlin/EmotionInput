package com.github.herokotlin.emotioninput.filter

import com.github.herokotlin.emotioninput.model.Emotion

class BracketFilter(emotionList: List<Emotion>) : EmotionFilter("\\[[ a-zA-Z0-9\\u4e00-\\u9fa5]+\\]", emotionList)