package org.soma.everyonepick.common_ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import org.soma.everyonepick.common_ui.databinding.ViewCustomActionBarBinding

class CustomActionBar: ConstraintLayout {
    constructor(context: Context): super(context){
        initializeView(context)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        initializeView(context)
        getAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs) {
        initializeView(context)
        getAttrs(attrs)
    }

    lateinit var binding: ViewCustomActionBarBinding

    private fun initializeView(context: Context?) {
        binding = ViewCustomActionBarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomActionBar)
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