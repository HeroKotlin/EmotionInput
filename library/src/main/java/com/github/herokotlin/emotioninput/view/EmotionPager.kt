package com.github.herokotlin.emotioninput.view

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.herokotlin.emotioninput.EmotionInputCallback
import com.github.herokotlin.emotioninput.EmotionInputConfiguration
import com.github.herokotlin.emotioninput.R
import com.github.herokotlin.emotioninput.model.EmotionIcon
import com.github.herokotlin.emotioninput.model.EmotionSet
import kotlinx.android.synthetic.main.emotion_input_pager.view.*

class EmotionPager: LinearLayout {

    lateinit var configuration: EmotionInputConfiguration

    lateinit var callback: EmotionInputCallback

    /**
     * 多套表情
     */
    var emotionSetList: List<EmotionSet> = listOf()

        set(value) {
            field = value
            emotionSetIndex = 0
            pager.adapter?.notifyDataSetChanged()
        }

    /**
     * 当前在第几套表情
     */
    var emotionSetIndex = 0

        set(value) {

            field = value

            if (emotionSetList.count() > value) {
                val emotionSet = emotionSetList[value]
                if (emotionSet.hasIndicator) {
                    showIndicatorView()
                    indicator.index = 0
                    indicator.count = emotionSet.emotionPageList.count()
                    indicator.invalidate()
                }
                else {
                    hideIndicatorView()
                }
            }
            else {
                hideIndicatorView()
            }

            var index = 0
            toolbar.emotionIconList = emotionSetList.map {
                EmotionIcon(index, it.localImage, index++ == value)
            }

        }

    var isSendButtonEnabled = false

        set(value) {

            field = value

            if (value) {
                toolbar.enableSendButton()
            }
            else {
                toolbar.disableSendButton()
            }

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

        LayoutInflater.from(context).inflate(R.layout.emotion_input_pager, this)

        pager.adapter = object: PagerAdapter() {

            override fun instantiateItem(container: ViewGroup, position: Int): Any {

                val view = EmotionGrid(context)

                view.callback = callback
                view.configuration = configuration

                checkRange(position) { setIndex, pageIndex ->
                    view.emotionPage = emotionSetList[setIndex].emotionPageList[pageIndex]
                }

                container.addView(view)

                return view
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int {
                var count = 0
                emotionSetList.forEach {
                    count += it.emotionPageList.count()
                }
                return count
            }
        }

        pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                checkRange(position) { setIndex, pageIndex ->
                    emotionSetIndex = setIndex
                    indicator.index = pageIndex
                    indicator.invalidate()
                }
            }

        })

        toolbar.onIconClick = {
            var count = 0
            for (i in 0 until emotionSetList.count()) {
                if (i == it.index) {
                    pager.setCurrentItem(count, false)
                    break
                }
                count += emotionSetList[i].emotionPageList.count()
            }
        }

        toolbar.onSendClick = {
            callback.onSendClick()
        }

    }

    private fun showIndicatorView() {
        indicator.visibility = View.VISIBLE
    }

    private fun hideIndicatorView() {
        indicator.visibility = View.GONE
    }

    /**
     * 绝对 index 到相对 index 的转换
     */
    private fun checkRange(index: Int, callback: (setIndex: Int, pageIndex: Int) -> Unit) {
        var from = 0
        for (i in 0 until emotionSetList.count()) {
            val to = from + emotionSetList[i].emotionPageList.count()
            if (index in from until to) {
                callback(i, index - from)
                break
            }
            from = to
        }
    }

}