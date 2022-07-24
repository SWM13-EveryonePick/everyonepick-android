package org.soma.everyonepick.common_ui.bottomnavigationview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.soma.everyonepick.common_ui.R

private const val MAX_SCALE = 1.5f
private const val ANIMATION_DURATION = 300L
private const val INDICATOR_WIDTH = 170f
private const val INDICATOR_HEIGHT = 120f

open class IndicatorBottomNavigationView: BottomNavigationView {
    private var animator: ValueAnimator? = null

    private val indicator = RectF()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.primary_blue)
    }

    private val View.centerX get() = left + width / 2f
    private val View.centerY get() = height / 2f

    constructor(context: Context): super(context, null)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs, com.google.android.material.R.attr.bottomNavigationStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr, com.google.android.material.R.style.Widget_Design_BottomNavigationView)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doOnPreDraw {
            // 최초에는 애니메이션을 제거한 채로 이동시킵니다.
            startIndicatorAnimation(selectedItemId, animated = false)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if(isLaidOut) {
            val cornerRadius = indicator.height() / 2f
            canvas.drawRoundRect(indicator, cornerRadius, cornerRadius, paint)
        }
        // indicator를 먼저 그린 뒤에 나머지 뷰를 그리게 됩니다.
        super.dispatchDraw(canvas)
    }

    fun startIndicatorAnimation(itemId: Int, animated: Boolean = true) {
        if (!isLaidOut) return
        cancelAnimator(false)

        val itemView = findViewById<View>(itemId) ?: return
        val startCenterX = indicator.centerX()
        val startScale = indicator.width() / INDICATOR_WIDTH

        // nextX: startCenterX -> 선택된 아이템의 centerX
        // indicatorWidth: width -> width * MAX_SCALE -> width
        animator = ValueAnimator.ofFloat(startScale, MAX_SCALE, 1f).apply {
            addUpdateListener {
                val nextX = linearInterpolation(it.animatedFraction, startCenterX, itemView.centerX)
                val indicatorWidth = INDICATOR_WIDTH * (it.animatedValue as Float)

                val top = centerY - INDICATOR_HEIGHT / 2f
                val bottom = centerY + INDICATOR_HEIGHT / 2f
                val left = nextX - indicatorWidth / 2f
                val right = nextX + indicatorWidth / 2f

                indicator.set(left, top, right, bottom)
                invalidate()
            }
            duration = if(animated) ANIMATION_DURATION else 0L

            start()
        }
    }

    private fun cancelAnimator(shouldAnimatorEnd: Boolean) = animator?.let {
        if(shouldAnimatorEnd) it.end()
        else it.cancel()

        it.removeAllUpdateListeners()
        animator = null
    }

    // t == 0.3 -> return 0.7a + 0.3b
    private fun linearInterpolation(t: Float, a: Float, b: Float) = (1 - t) * a + t * b

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelAnimator(true)
    }
}