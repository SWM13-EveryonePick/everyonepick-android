package org.soma.everyonepick.common_ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import org.soma.everyonepick.common_ui.databinding.LayoutCustomIndicatorBinding

/**
 * A customized indicator view for [ViewPager2]
 */
class CustomIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): FrameLayout(context, attrs, defStyle) {

    private lateinit var binding: LayoutCustomIndicatorBinding

    private var indicatorSize = 20
    private var indicatorMargin = 15
    private var indicatorRadius = indicatorSize*0.5f
    // Determines how large the selected indicator will be compared to the others.
    private var selectedIndicatorWidthScale = 2

    private var indicatorColor = ContextCompat.getColor(context, R.color.light_gray)
    private var selectedIndicatorColor = ContextCompat.getColor(context, R.color.primary_blue)

    private var indicators = mutableListOf<ImageView>()

    init {
        initializeView(context)
        getAttrs(attrs)
    }

    private fun initializeView(context: Context) {
        binding = LayoutCustomIndicatorBinding.inflate(LayoutInflater.from(context), this, true)
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


    /**
     * @param viewPager2 A [ViewPager2] which [CustomIndicator] refers to.
     * @param startPosition A start position of [ViewPager2].
     */
    fun setupViewPager2(viewPager2: ViewPager2, startPosition: Int) {
        val itemCount = viewPager2.adapter?.itemCount?: 0
        for (i in 0 until itemCount) addIndicator(i, startPosition)

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

                // To fix an ui problem which occurs when firstIndex is same as secondIndex
                if (firstIndex != secondIndex) {
                    refreshTwoIndicatorColor(firstIndex, secondIndex, positionOffset)
                    refreshTwoIndicatorWidth(firstIndex, secondIndex, positionOffset)
                }
            }
        })
    }

    private fun addIndicator(index: Int, selectedPosition: Int) {
        val indicator = LayoutInflater.from(context).inflate(R.layout.layout_indicator, this, false)

        // Set color
        val imageView = indicator.findViewById<ImageView>(R.id.image_indicator)
        imageView.background = (imageView.background as GradientDrawable).apply {
            setColor(if (index == selectedPosition) selectedIndicatorColor else indicatorColor)
        }

        // Set size, margin
        val params = imageView.layoutParams as LinearLayout.LayoutParams
        params.height = indicatorSize
        params.width = if (index == selectedPosition) selectedIndicatorWidthScale * indicatorSize else indicatorSize
        params.setMargins(indicatorMargin, 0, 0, 0)
        params.setMargins(0, 0, indicatorMargin, 0)

        // Set cornerRadius to the rectangle background then it will be shown as a circle
        (imageView.background as GradientDrawable).cornerRadius = indicatorRadius

        indicators.add(imageView)
        binding.layout.addView(indicator)
    }

    private fun refreshTwoIndicatorColor(
        firstIndex: Int,
        secondIndex: Int,
        positionOffset: Float
    ) {
        if (!(0 <= firstIndex && secondIndex <= indicators.count() - 1)) return

        // Blend selectedIndicatorColor and indicatorColor by positionOffset
        val firstColor = ColorUtils.blendARGB(selectedIndicatorColor, indicatorColor, positionOffset)
        val secondColor = ColorUtils.blendARGB(indicatorColor, selectedIndicatorColor, positionOffset)

        indicators[firstIndex].background = (indicators[firstIndex].background as GradientDrawable).apply { setColor(firstColor) }
        indicators[secondIndex].background = (indicators[secondIndex].background as GradientDrawable).apply { setColor(secondColor) }
    }

    private fun refreshTwoIndicatorWidth(
        firstIndex: Int,
        secondIndex: Int,
        positionOffset: Float
    ) {
        if (!(0 <= firstIndex && secondIndex <= indicators.count() - 1)) return

        val firstWidth = calculateWidthByOffset(positionOffset)
        val secondWidth = calculateWidthByOffset(1 - positionOffset)

        val firstParams = indicators[firstIndex].layoutParams
        val secondParams = indicators[secondIndex].layoutParams
        firstParams.width = firstWidth.toInt()
        secondParams.width = secondWidth.toInt()

        indicators[firstIndex].layoutParams = firstParams
        indicators[secondIndex].layoutParams = secondParams
    }

    private fun calculateWidthByOffset(offset: Float) = indicatorSize * offset + indicatorSize * selectedIndicatorWidthScale * (1-offset)

    /**
     * @param recyclerView A [RecyclerView] which [CustomIndicator] refers to.
     * @param pagerSnapHelper A [PagerSnapHelper] attached to [recyclerView].
     * @param itemCount Item count of [recyclerView].
     * @param startPosition A start position of [RecyclerView].
     */
    fun setupRecyclerView(
        recyclerView: RecyclerView,
        pagerSnapHelper: PagerSnapHelper,
        itemCount: Int,
        startPosition: Int
    ) {
        for (i in 0 until itemCount) addIndicator(i, startPosition)

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                pagerSnapHelper.findSnapView(recyclerView.layoutManager)?.let { itemView ->
                    val position = recyclerView.layoutManager?.getPosition(itemView)?: 0
                    val offset = itemView.x / itemView.width
                    val firstIndex = if (offset < 0) position else position - 1
                    val secondIndex = if (offset < 0) position + 1 else position
                    val positionOffset = if (offset < 0) -offset else 1 - offset
                    refreshTwoIndicatorColor(firstIndex, secondIndex, positionOffset)
                    refreshTwoIndicatorWidth(firstIndex, secondIndex, positionOffset)
                }
            }
        })
    }
}