package com.github.herokotlin.emotioninput.view

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.herokotlin.emotioninput.R
import com.github.herokotlin.emotioninput.model.EmotionIcon
import kotlinx.android.synthetic.main.emotion_input_toolbar.view.*
import kotlinx.android.synthetic.main.emotion_input_toolbar_icon.view.*

internal class EmotionToolbar : LinearLayout {

    lateinit var onIconClick: (icon: EmotionIcon) -> Unit

    lateinit var onSubmitClick: () -> Unit

    var emotionIconList = listOf<EmotionIcon>()

        set(value) {
            field = value
            iconList.adapter?.notifyDataSetChanged()
        }

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

        LayoutInflater.from(context).inflate(R.layout.emotion_input_toolbar, this)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        iconList.layoutManager = layoutManager

        // 性能更好
        iconList.setHasFixedSize(true)

        iconList.adapter = IconListAdapter(context)

        submitButton.setOnClickListener {
            onSubmitClick()
        }

        disableSubmitButton()

    }

    fun enableSubmitButton() {

        submitButton.isEnabled = true
        submitButton.setTextColor(ContextCompat.getColor(context, R.color.emotion_input_submit_button_text_color_enabled))
        submitButton.setBackgroundResource(R.drawable.emotion_input_submit_button_enabled)

    }

    fun disableSubmitButton() {

        submitButton.isEnabled = false
        submitButton.setTextColor(ContextCompat.getColor(context, R.color.emotion_input_submit_button_text_color_disabled))
        submitButton.setBackgroundResource(R.drawable.emotion_input_submit_button_disabled)

    }

    inner class IconListAdapter(private val context: Context) : RecyclerView.Adapter<IconViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.emotion_input_toolbar_icon, null)
            return IconViewHolder(view)
        }

        override fun getItemCount(): Int {
            return emotionIconList.count()
        }

        override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
            holder.bind(position)
        }

    }

    inner class IconViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var dividerView = view.dividerView
        private var imageView = view.imageView

        init {
            view.setOnClickListener {
                onIconClick(emotionIconList[layoutPosition])
            }
        }

        fun bind(position: Int) {

            if (position > 0) {
                dividerView.visibility = View.VISIBLE
            }
            else {
                dividerView.visibility = View.GONE
            }

            val icon = emotionIconList[position]

            imageView.setImageResource(icon.localImage)

            val bgColor = if (icon.selected) {
                R.color.emotion_input_toolbar_cell_bg_color_pressed
            }
            else {
                R.color.emotion_input_toolbar_bg_color
            }

            itemView.setBackgroundColor(
                ContextCompat.getColor(context, bgColor)
            )

        }

    }

}