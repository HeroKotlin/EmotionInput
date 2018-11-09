package com.github.herokotlin.emotioninput.model

data class EmotionSet(
    // 底部栏图标
    val iconId: Int,
    // 该套表情的所有表情
    val emotionPageList: List<EmotionPage>,
    // 是否需要导航指示器
    val hasIndicator: Boolean
) {

    companion object {

        fun build(
            iconId: Int,
            emotionList: List<Emotion>,
            columns: Int,
            rows: Int,
            width: Int,
            height: Int,
            hasDeleteButton: Boolean,
            hasIndicator: Boolean
        ): EmotionSet {

            val emotionPageList = arrayListOf<EmotionPage>()

            var pageSize = columns * rows
            if (hasDeleteButton) {
                pageSize -= 1
            }

            val count = emotionList.count()
            val totalPage = Math.ceil(count.toDouble() / pageSize).toInt()

            var start = 0

            for (i in 1..totalPage) {
                var end = start + pageSize
                if (end > count) {
                    end = count
                }

                // 该页的表情列表
                val subList = arrayListOf<Emotion>()

                for (j in start until end) {
                    subList.add(emotionList[ j ])
                }

                // 如果表情数量没满，需补齐数量，以免 grid view 高度塌陷
                val blankCount = columns * rows - subList.count()
                if (blankCount > 0) {
                    for (j in 1..blankCount) {
                        subList.add(Emotion())
                    }
                }

                emotionPageList.add(
                    EmotionPage(
                        subList,
                        columns,
                        rows,
                        width,
                        height,
                        hasDeleteButton
                    )
                )

                start = end

            }

            return EmotionSet(iconId, emotionPageList, hasIndicator)

        }
    }
}

