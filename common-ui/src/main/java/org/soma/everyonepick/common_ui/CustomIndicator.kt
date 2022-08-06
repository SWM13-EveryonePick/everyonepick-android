package org.soma.everyonepick.common_ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.viewpager2.widget.ViewPager2
import org.soma.everyonepick.common_ui.databinding.ViewCustomIndicatorBinding

/**
 * CustomIndicator는 [ViewPager2]와 결합되어 사용됩니다.
 * 각 indicator는 ViewPager2의 위치에 따라서 색상과 길이가 달라집니다.
 */
class CustomIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): FrameLayout(context, attrs, defStyle) {

    private var indicatorSize = 20
    private var indicatorMargin = 15
    private var indicatorRadius = indicatorSize*0.5f
    private var selectedIndicatorWidthScale = 2

    private var indicatorColor = ContextCompat.getColor(context, R.color.light_gray)
    private var selectedIndicatorColor = ContextCompat.getColor(context, R.color.primary_blue)

    private lateinit var binding: ViewCustomIndicatorBinding
    private var dots = mutableListOf<ImageView>()
    private lateinit var viewPager2: ViewPager2

    init {
        initializeView(context)
        getAttrs(attrs)
    }

    private fun initializeView(context: Context) {
        binding = ViewCustomIndicatorBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomIndicator)
        setTypedArray(typedArray)
    }

    private fun setTypedArray(typedArray: TypedArray) {
        indicatorSize = typedArray.getDimensionPixelSize(R.styleable.CustomIndicator_customIndicatorSize, indicatorSize)
        indicatorRadius = indicatorSize * 0.5f
        selectedIndicatorWidthScale = typedArray.getDimensionPixelSize(R.styleable.CustomIndicator_selectedIndicatorWidthScale, selectedIndicatorWidthScale)
        indicatorMargin = typedArray.getDimensionPixelSize(R.styleable.CustomIndicator_indicatorMargin, indicatorMargin)

        indicatorColor = typedArray.getColor(R.styleable.CustomIndicator_indicatorColor, indicatorColor)
        selectedIndicatorColor = typedArray.getColor(R.styleable.CustomIndicator_selectedIndicatorColor, selectedIndicatorColor)

        typedArray.recycle()
    }


    fun setupViewPager2(viewPager2: ViewPager2, currentPosition: Int) {
        this.viewPager2 = viewPager2

        val itemCount = viewPager2.adapter?.itemCount?: 0
        for(i in 0 until itemCount) addDot(i, currentPosition)

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                val absoluteOffset = position + positionOffset
                val firstIndex = kotlin.math.floor(absoluteOffset).toInt()
                val secondIndex = kotlin.math.ceil(absoluteOffset).toInt()

                // firstIndex == secondIndex일 때 position이 갱신되는데,
                // 이때 아래 함수를 호출하면 문제가 생겨서 해당 경우는 처리하지 않습니다.
                if (firstIndex != secondIndex) {
                    refreshTwoDotColor(firstIndex, secondIndex, positionOffset)
                    refreshTwoDotWidth(firstIndex, secondIndex, positionOffset)
                }
            }
        })
    }

    private fun addDot(index: Int, currentPosition: Int) {
        val dot = LayoutInflater.from(context).inflate(R.layout.layout_dot, this, false)
        dot.layoutDirection = View.LAYOUT_DIRECTION_LTR

        // 색상
        val imageView = dot.findViewById<ImageView>(R.id.image_dot)
        imageView.background = (imageView.background as GradientDrawable).apply {
            setColor(if (index == currentPosition) selectedIndicatorColor else indicatorColor)
        }

        // 크기, 마진
        val params = imageView.layoutParams as LinearLayout.LayoutParams
        params.height = indicatorSize
        params.width = if (index == currentPosition) selectedIndicatorWidthScale*indicatorSize else indicatorSize
        params.setMargins(indicatorMargin, 0, 0, 0)
        params.setMargins(0, 0, indicatorMargin, 0)

        // Rectangle에 radius를 두어 원으로 보이게 합니다.
        (imageView.background as GradientDrawable).cornerRadius = indicatorRadius

        dots.add(imageView)
        binding.layout.addView(dot)
    }

    private fun refreshTwoDotColor(
        firstIndex: Int,
        secondIndex: Int,
        positionOffset: Float
    ) {
        // positionOffset에 따라 selectedIndicatorColor와 indicatorColor를 적당히 섞어 적용합니다.
        val firstColor = ColorUtils.blendARGB(selectedIndicatorColor, indicatorColor, positionOffset)
        val secondColor = ColorUtils.blendARGB(indicatorColor, selectedIndicatorColor, positionOffset)

        dots[firstIndex].background = (dots[firstIndex].background as GradientDrawable).apply { setColor(firstColor) }
        dots[secondIndex].background = (dots[secondIndex].background as GradientDrawable).apply { setColor(secondColor) }
    }

    private fun refreshTwoDotWidth(
        firstIndex: Int,
        secondIndex: Int,
        positionOffset: Float
    ) {
        val firstWidth = calculateWidthByOffset(positionOffset)
        val secondWidth = calculateWidthByOffset(1 - positionOffset)

        val firstParams = dots[firstIndex].layoutParams
        val secondParams = dots[secondIndex].layoutParams
        firstParams.width = firstWidth.toInt()
        secondParams.width = secondWidth.toInt()

        dots[firstIndex].layoutParams = firstParams
        dots[secondIndex].layoutParams = secondParams
    }

    private fun calculateWidthByOffset(offset: Float) = indicatorSize * offset + indicatorSize * selectedIndicatorWidthScale * (1-offset)
}