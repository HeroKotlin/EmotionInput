package com.github.herokotlin.emotioninput.view

import android.content.Context
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.github.herokotlin.emotioninput.EmotionInputCallback
import com.github.herokotlin.emotioninput.EmotionInputConfiguration
import com.github.herokotlin.emotioninput.R
import com.github.herokotlin.emotioninput.model.Emotion
import com.github.herokotlin.emotioninput.model.EmotionPage
import kotlinx.android.synthetic.main.emotion_input_cell.view.*
import kotlinx.android.synthetic.main.emotion_input_grid.view.*

internal class EmotionGrid: FrameLayout {

    lateinit var configuration: EmotionInputConfiguration

    lateinit var callback: EmotionInputCallback

    var emotionPage = EmotionPage()

        set(value) {

            field = value

            val layoutManager = gridView.layoutManager as GridLayoutManager
            layoutManager.spanCount = value.columns

            gridView.adapter?.notifyDataSetChanged()

        }

    private val paddingVertical: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.emotion_input_emotion_grid_padding_vertical)
    }

    private val rowSpacing: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.emotion_input_emotion_grid_row_spacing)
    }

    private val columnSpacing: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.emotion_input_emotion_grid_column_spacing)
    }

    private val layoutSize: Size by lazy {

        val width = containerView.measuredWidth
        val height = containerView.measuredHeight

        val columnCount = emotionPage.columns
        var spacing = columnSpacing * (columnCount - 1)
        val cellWidth = (width - spacing) / columnCount

        // 计算 itemHeight 最大值
        val rowCount = emotionPage.rows
        spacing = paddingVertical * 2 + rowSpacing * (rowCount - 1)

        val cellMaxHeight = (height - spacing) / rowCount
        val cellHeight = Math.min(cellWidth, cellMaxHeight)

        Size(cellWidth, cellHeight, (height - rowSpacing * (rowCount - 1) - rowCount * cellHeight) / 2)

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

        LayoutInflater.from(context).inflate(R.layout.emotion_input_grid, this)

        gridView.layoutManager = GridLayoutManager(context, 1)

        gridView.adapter = GridViewAdapter(context)

        gridView.setHasFixedSize(true)

        gridView.addItemDecoration(
            CellViewDecoration()
        )

    }

    data class Size(
        val cellWidth: Int,
        val cellHeight: Int,
        val gridPaddingVertical: Int
    )

    inner class GridViewAdapter(private val context: Context) : RecyclerView.Adapter<CellViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.emotion_input_cell, null)
            view.minimumHeight = layoutSize.cellHeight
            return CellViewHolder(view)
        }

        override fun getItemCount(): Int {
            return emotionPage.emotionList.count()
        }

        override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
            val emotion = emotionPage.emotionList[position]
            if (emotionPage.hasDeleteButton && position == emotionPage.rows * emotionPage.columns - 1) {
                holder.showDelete()
            }
            else if (emotion.code != "") {
                holder.showEmotion(emotion, emotionPage.width, emotionPage.height)
            }
            else {
                holder.showNothing()
            }
        }

    }

    inner class CellViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var deleteView = view.deleteView

        private var emotionView = view.emotionView
        private var imageView = view.imageView
        private var textView = view.textView

        init {
            view.setOnClickListener {
                if (emotionView.visibility == View.VISIBLE) {

                    val index = gridView.getChildAdapterPosition(view)
                    val emotion = emotionPage.emotionList[index]

                    callback.onEmotionClick(emotion)
                }
                else if (deleteView.visibility == View.VISIBLE) {
                    callback.onDeleteClick()
                }
            }
        }

        fun showEmotion(emotion: Emotion, width: Int, height: Int) {

            var hasEmotion = false
            if (emotion.localImage > 0) {
                hasEmotion = true
                imageView.setImageResource(emotion.localImage)
            }
            else if (emotion.remoteImage.isNotBlank()) {
                hasEmotion = true
                configuration.loadImage(imageView, emotion.remoteImage)
            }
            if (hasEmotion) {

                deleteView.visibility = View.GONE
                emotionView.visibility = View.VISIBLE

                itemView.setBackgroundResource(R.drawable.emotion_input_emotion_cell)

                val layoutParams = imageView.layoutParams
                if (width > 0) {
                    layoutParams.width = width
                }
                if (height > 0) {
                    layoutParams.height = height
                }
                imageView.layoutParams = layoutParams

                if (emotion.name.isNotBlank()) {
                    textView.text = emotion.name
                    textView.visibility = View.VISIBLE
                }
                else {
                    textView.visibility = View.GONE
                }

            }
            else {
                showNothing()
            }

        }

        fun showDelete() {
            deleteView.visibility = View.VISIBLE
            emotionView.visibility = View.GONE
            itemView.setBackgroundResource(R.drawable.emotion_input_emotion_cell)
        }

        fun showNothing() {
            deleteView.visibility = View.GONE
            emotionView.visibility = View.GONE
            itemView.setBackgroundResource(0)
        }

    }

    inner class CellViewDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            val index = parent.getChildAdapterPosition(view)
            val columnCount = emotionPage.columns
            val rowIndex = index / columnCount
            val columnIndex = index % columnCount

            if (rowIndex > 0 && rowIndex < emotionPage.rows) {
                outRect.top = rowSpacing
            }

            // 水平的网格会自动均分
            if (columnCount == 2) {
                // 只有两个，各分一半
                if (columnIndex == 0) {
                    outRect.right = columnSpacing / 2
                }
                else if (columnIndex == 1) {
                    outRect.left = columnSpacing / 2
                }
            }
            else if (columnCount > 2) {
                if (columnIndex == 0) {
                    outRect.right = columnSpacing
                }
                else if (columnIndex < columnCount - 2) {
                    outRect.right = columnSpacing / 2
                }
                if (columnIndex > 1) {
                    if (columnIndex == columnCount - 1) {
                        outRect.left = columnSpacing
                    }
                    else {
                        outRect.left = columnSpacing / 2
                    }
                }
            }

        }
    }

}