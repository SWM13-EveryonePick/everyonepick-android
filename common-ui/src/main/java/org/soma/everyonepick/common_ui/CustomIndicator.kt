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
 * 각 indicator는 ViewPager2의 위치에 따라서 색상과 길이가 달라지며,
 * 특히 늘어나는 원을 구현하기 위해 layout_dot에 (왼쪽 반원) + (직사각형) + (오른쪽 반원)을 두었고,
 * 오프셋에 따라 가운데 있는 직사각형의 width가 변함으로써 원이 늘어나는 효과를 구현하였습니다.
 */
class CustomIndicator: FrameLayout {
    constructor(context: Context): super(context) {
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

    private val indicatorSize = 20
    private var selectedIndicatorCenterWidth = 40
    private var indicatorMargin = 10

    private var indicatorColor = ContextCompat.getColor(context, R.color.light_gray)
    private var selectedIndicatorColor = ContextCompat.getColor(context, R.color.primary_blue)

    private lateinit var binding: ViewCustomIndicatorBinding
    private var dots = mutableListOf<Triple<ImageView, ImageView, ImageView>>()
    private lateinit var viewPager2: ViewPager2

    private fun initializeView(context: Context) {
        binding = ViewCustomIndicatorBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomIndicator)
        setTypedArray(typedArray)
    }

    private fun setTypedArray(typedArray: TypedArray) {
        selectedIndicatorCenterWidth = typedArray.getDimensionPixelSize(R.styleable.CustomIndicator_selectedIndicatorCenterWidth, selectedIndicatorCenterWidth)
        indicatorMargin = typedArray.getDimensionPixelSize(R.styleable.CustomIndicator_indicatorMargin, indicatorMargin)

        indicatorColor = typedArray.getColor(R.styleable.CustomIndicator_indicatorColor, indicatorColor)
        selectedIndicatorColor = typedArray.getColor(R.styleable.CustomIndicator_selectedIndicatorColor, selectedIndicatorColor)

        typedArray.recycle()
    }

    fun setupViewPager2(viewPager2: ViewPager2) {
        this.viewPager2 = viewPager2

        val itemCount = viewPager2.adapter?.itemCount?: 0
        for(i in 0 until itemCount) addDot(i)

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

    private fun addDot(index: Int) {
        val dot = LayoutInflater.from(context).inflate(R.layout.layout_dot, this, false)
        dot.layoutDirection = View.LAYOUT_DIRECTION_LTR

        val imageLeft = dot.findViewById<ImageView>(R.id.image_left)
        val imageCenter = dot.findViewById<ImageView>(R.id.image_center)
        val imageRight = dot.findViewById<ImageView>(R.id.image_right)

        // 색상 초기화
        val color = if (index == 0) selectedIndicatorColor else indicatorColor
        imageLeft.background = (imageLeft.background as GradientDrawable).apply { setColor(color) }
        imageCenter.background = (imageCenter.background as GradientDrawable).apply { setColor(color) }
        imageRight.background = (imageRight.background as GradientDrawable).apply { setColor(color) }

        val leftParams = imageLeft.layoutParams as LinearLayout.LayoutParams
        val centerParams = imageCenter.layoutParams as LinearLayout.LayoutParams
        val rightParams = imageRight.layoutParams as LinearLayout.LayoutParams

        // 높이 초기화
        leftParams.height = indicatorSize
        centerParams.height = indicatorSize
        rightParams.height = indicatorSize

        // center item 길이 초기화
        centerParams.width = if (index == 0) selectedIndicatorCenterWidth else 0

        // 마진 초기화
        leftParams.setMargins(indicatorMargin, 0, 0, 0)
        rightParams.setMargins(0, 0, indicatorMargin, 0)

        dots.add(Triple(imageLeft, imageCenter, imageRight))
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

        dots[firstIndex].first.background = (dots[firstIndex].first.background as GradientDrawable).apply { setColor(firstColor) }
        dots[secondIndex].first.background = (dots[secondIndex].first.background as GradientDrawable).apply { setColor(secondColor) }
        dots[firstIndex].second.background = (dots[firstIndex].second.background as GradientDrawable).apply { setColor(firstColor) }
        dots[secondIndex].second.background = (dots[secondIndex].second.background as GradientDrawable).apply { setColor(secondColor) }
        dots[firstIndex].third.background = (dots[firstIndex].third.background as GradientDrawable).apply { setColor(firstColor) }
        dots[secondIndex].third.background = (dots[secondIndex].third.background as GradientDrawable).apply { setColor(secondColor) }
    }

    private fun refreshTwoDotWidth(
        firstIndex: Int,
        secondIndex: Int,
        positionOffset: Float
    ) {
        val firstWidth = calculateWidthByOffset(positionOffset)
        val secondWidth = calculateWidthByOffset(1 - positionOffset)

        val firstParams = dots[firstIndex].second.layoutParams
        val secondParams = dots[secondIndex].second.layoutParams
        firstParams.width = firstWidth.toInt()
        secondParams.width = secondWidth.toInt()

        dots[firstIndex].second.layoutParams = firstParams
        dots[secondIndex].second.layoutParams = secondParams
    }

    private fun calculateWidthByOffset(offset: Float) = selectedIndicatorCenterWidth*(1-offset)
}