package org.soma.everyonepick.common_ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import org.soma.everyonepick.common_ui.databinding.ViewCustomactionbarBinding

class CustomActionBar: ConstraintLayout {
    // TODO: 커스텀 뷰 완성 + 적용해보기 + 커스텀 액션 바 위에 버튼 달기 + 선택/취소 및 버튼 클릭 이벤트 구현
    constructor(context: Context): super(context){
        initialzeView(context)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        initialzeView(context)
        getAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs) {
        initialzeView(context)
        getAttrs(attrs)
    }

    private lateinit var binding: ViewCustomactionbarBinding

    private fun initialzeView(context: Context?) {
        binding = ViewCustomactionbarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomActionBar)
        setTypedArray(typedArray)
    }

    private fun getAttrs(attrs: AttributeSet?, defStyle: Int){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomActionBar, defStyle, 0)
        setTypedArray(typedArray)
    }

    private fun setTypedArray(typedArray: TypedArray) {
        val hasBackButton = typedArray.getBoolean(R.styleable.CustomActionBar_hasBackButton, false)
        binding.imageBackbutton.visibility = when(hasBackButton) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        val title = typedArray.getString(R.styleable.CustomActionBar_title)
        binding.textTitle.text = title

        typedArray.recycle()
    }

    fun setHasBackButton(hasBackButton: Boolean) {
        binding.imageBackbutton.visibility = when(hasBackButton) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    fun setTitle(title: String) {
        binding.textTitle.text = title
    }
}