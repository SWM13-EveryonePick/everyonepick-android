package org.soma.everyonepick.common_ui

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.StyleRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.round

private const val DEFAULT_INDICATOR_SIZE = 20
private const val DEFAULT_SELECTED_INDICATOR_CENTER_WIDTH = 40
private const val DEFAULT_MARGIN = 10

class CustomIndicator: FrameLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    private var linearLayout: LinearLayout = LinearLayout(context)
    private var dots = mutableListOf<Triple<ImageView, ImageView, ImageView>>()
    private lateinit var viewPager2: ViewPager2

    private var defaultColor = ContextCompat.getColor(context, R.color.light_gray)
    private var selectedColor = ContextCompat.getColor(context, R.color.primary_blue)

    init {
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.gravity = Gravity.CENTER_HORIZONTAL
        addView(linearLayout, WRAP_CONTENT, WRAP_CONTENT)
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

                if (firstIndex == secondIndex) {
                    if (firstIndex - 1 >= 0) {
                        val params = dots[firstIndex - 1].second.layoutParams
                        params.width = 0
                        dots[firstIndex - 1].second.layoutParams = params
                    }
                    if (firstIndex + 1 <= itemCount - 1) {
                        val params = dots[firstIndex + 1].second.layoutParams
                        params.width = 0
                        dots[firstIndex + 1].second.layoutParams = params
                    }
                } else {
                    refreshTwoDotColor(firstIndex, secondIndex, positionOffset)
                    refreshTwoDotWidth(firstIndex, secondIndex, positionOffset)
                }
            }
        })
    }

    private fun addDot(index: Int) {
        val dot = LayoutInflater.from(context).inflate(R.layout.layout_dot, this, false)
        val imageLeft = dot.findViewById<ImageView>(R.id.image_left)
        val imageCenter = dot.findViewById<ImageView>(R.id.image_center)
        val imageRight = dot.findViewById<ImageView>(R.id.image_right)

        val leftParams = imageLeft.layoutParams as LinearLayout.LayoutParams
        val centerParams = imageCenter.layoutParams as LinearLayout.LayoutParams
        val rightParams = imageRight.layoutParams as LinearLayout.LayoutParams

        dot.layoutDirection = View.LAYOUT_DIRECTION_LTR

        imageLeft.background = (imageLeft.background as GradientDrawable).apply {
            setColor(if (index == 0) selectedColor else defaultColor)
        }
        imageCenter.background = (imageCenter.background as GradientDrawable).apply {
            setColor(if (index == 0) selectedColor else defaultColor)
        }
        imageRight.background = (imageRight.background as GradientDrawable).apply {
            setColor(if (index == 0) selectedColor else defaultColor)
        }

        centerParams.width = if (index == 0) DEFAULT_SELECTED_INDICATOR_CENTER_WIDTH else 0
        centerParams.height = DEFAULT_INDICATOR_SIZE
        leftParams.height = DEFAULT_INDICATOR_SIZE
        rightParams.height = DEFAULT_INDICATOR_SIZE
        leftParams.setMargins(DEFAULT_MARGIN, 0, 0, 0)
        rightParams.setMargins(0, 0, DEFAULT_MARGIN, 0)

        dots.add(Triple(imageLeft, imageCenter, imageRight))
        linearLayout.addView(dot)
    }

    private fun refreshTwoDotColor(
        firstIndex: Int,
        secondIndex: Int,
        positionOffset: Float
    ) {
        val firstColor = ColorUtils.blendARGB(selectedColor, defaultColor, positionOffset)
        val secondColor = ColorUtils.blendARGB(defaultColor, selectedColor, positionOffset)
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

    private fun calculateWidthByOffset(offset: Float) = DEFAULT_SELECTED_INDICATOR_CENTER_WIDTH*(1-offset)
}