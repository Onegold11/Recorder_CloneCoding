package com.clonecoding.recorder_clonecoding

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton

/**
 * 커스텀 녹음 버튼
 *
 * AppCompat, 이전 버전을 지원하기 위해 맵핑을 도와주는 라이브러리
 * xml 에서는 자동으로 적용해줘서 안적어줘도 됨
 * 코드는 자동으로 안돼서 직접해줘야함
 */
class RecordButton(
    context: Context,
    attrs: AttributeSet
) : AppCompatImageButton(context, attrs) {

    init {

        setBackgroundResource(R.drawable.shape_oval_button)
    }

    fun updateIconWithState(state: State) {

        when (state) {
            State.BEFORE_RECORDING -> {
                setImageResource(R.drawable.ic_record)
            }
            State.ON_RECORDING -> {
                setImageResource(R.drawable.ic_stop)
            }
            State.AFTER_RECORDING -> {
                setImageResource(R.drawable.ic_play)
            }
            State.ON_PLAYING -> {
                setImageResource(R.drawable.ic_stop)
            }
        }
    }
}