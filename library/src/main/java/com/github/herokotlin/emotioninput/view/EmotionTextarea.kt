package com.github.herokotlin.emotioninput.view

import android.content.ClipboardManager
import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.EditText
import com.github.herokotlin.emotioninput.filter.EmotionFilter
import com.github.herokotlin.emotioninput.model.Emotion

class EmotionTextarea: EditText {

    /**
     * 表情和文本的高度比例
     */
    var emotionTextHeightRatio = 1f

    var onTextChange: (() -> Unit)? = null

    var plainText = ""

        get() {
            return text.toString()
        }

    private val filters = arrayListOf<EmotionFilter>()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                if (string != null) {
                    for (filter in filters) {
                        filter.filter(this@EmotionTextarea, text, string, emotionTextHeightRatio)
                    }
                    onTextChange?.invoke()
                }
            }
        })
    }

    fun addFilter(filter: EmotionFilter) {
        filters.add(filter)
    }

    fun removeFilter(filter: EmotionFilter) {
        filters.remove(filter)
    }

    fun insertEmotion(emotion: Emotion) {
        for (filter in filters) {
            if (filter.insert(this, emotion, emotionTextHeightRatio)) {
                break
            }
        }
    }

    fun insertText(text: CharSequence) {
        val spannable = SpannableStringBuilder(text)
        for (filter in filters) {
            filter.filter(this, spannable, text, emotionTextHeightRatio)
        }
        this.text.insert(selectionStart, spannable)
    }

    // 命名跟 ios 保持一致
    fun deleteBackward() {
        val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)
        onKeyDown(KeyEvent.KEYCODE_DEL, event)
    }

    fun clear() {
        text.clear()
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        if (id == android.R.id.paste) {
            paste()
            return true
        }
        return super.onTextContextMenuItem(id)
    }

    private fun paste() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        insertText(
            clipboard.primaryClip.getItemAt(0).text
        )
    }

}