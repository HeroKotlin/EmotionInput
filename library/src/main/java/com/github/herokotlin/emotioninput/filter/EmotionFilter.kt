package com.github.herokotlin.emotioninput.filter

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.widget.EditText
import android.widget.TextView
import com.github.herokotlin.emotioninput.model.Emotion
import com.github.herokotlin.emotioninput.view.EmotionSpan
import java.util.regex.Pattern

abstract class EmotionFilter(pattern: String, private val emotionList: List<Emotion>) {

    private var textSize2EmotionSizes = HashMap<Float, Int>()

    // 构造函数传 list 内部转成 map
    private val emotionCode2ImageId: Map<String, Int> by lazy {
        val map = HashMap<String, Int>()
        for (emotion in emotionList) {
            map[emotion.code] = emotion.localImage
        }
        map
    }

    private val pattern: Pattern by lazy {
        Pattern.compile(pattern)
    }

    fun filter(textView: TextView, text: CharSequence) {

        val spannable = SpannableString(text)

        filter(textView, spannable, text)

        textView.text = spannable

    }

    fun filter(textView: TextView, spannable: Spannable, text: CharSequence) {

        val context = textView.context

        match(text) { emotionCode, emotionStart, emotionEnd ->
            val drawable = getDrawable(context, emotionCode)
            if (drawable != null) {
                setSpan(spannable, emotionStart, emotionEnd, drawable, textView.textSize)
            }
        }

    }

    fun insert(textInput: EditText, emotion: Emotion): Boolean {

        val context = textInput.context

        val drawable = getDrawable(context, emotion.code)
        if (drawable != null) {
            val spannable = SpannableString(emotion.code)
            setSpan(spannable, 0, emotion.code.length, drawable, textInput.textSize)
            textInput.text.insert(textInput.selectionStart, spannable)
            return true
        }

        return false

    }

    private fun getDrawable(context: Context, emotionCode: String): Drawable? {
        val drawableId = emotionCode2ImageId[ emotionCode ]
        if (drawableId != null && drawableId > 0) {
            return ContextCompat.getDrawable(context, drawableId)
        }
        return null
    }

    /**
     * 用正则匹配字符串中的表情
     */
    private fun match(text: CharSequence, callback: (text: String, start: Int, end: Int) -> Unit) {
        val matcher = pattern.matcher(text)
        if (matcher != null) {
            while (matcher.find()) {
                callback(matcher.group().toString(), matcher.start(), matcher.end())
            }
        }
    }

    private fun setSpan(spannable: Spannable, start: Int, end: Int, drawable: Drawable, textSize: Float) {
        var emotionSize = textSize2EmotionSizes[textSize]
        if (emotionSize == null) {
            val paint = Paint()
            paint.textSize = textSize
            emotionSize = (paint.fontMetrics.bottom - paint.fontMetrics.top).toInt()
            textSize2EmotionSizes[textSize]= emotionSize
        }
        drawable.setBounds(0, 0, emotionSize, emotionSize)
        spannable.setSpan(EmotionSpan(drawable), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

}