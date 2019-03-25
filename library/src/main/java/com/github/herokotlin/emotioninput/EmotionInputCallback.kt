package com.github.herokotlin.emotioninput

import com.github.herokotlin.emotioninput.model.Emotion

interface EmotionInputCallback {

    fun onEmotionClick(emotion: Emotion) { }

    fun onDeleteClick() { }

    fun onSubmitClick() { }

}