package org.soma.everyonepick.common_ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val MAX_SCALE = 2f
private const val ANIMATION_DURATION = 300L
private const val INDICATOR_MARGIN_BOTTOM = 30f
private const val INDICATOR_WIDTH = 130f
private const val INDICATOR_HEIGHT = 100f

open class IndicatorBottomNavigationView: BottomNavigationView {
    private var animator: ValueAnimator? = null

    private val indicator = RectF()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.primary_blue)
    }

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
            val cornerRadius = 0.5f * indicator.height()
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

        // default로 시작하지 않는 것은, 이전 애니메이션이 끝나지 않았을 때를 대비하기 위함입니다.
        animator = ValueAnimator.ofFloat(startScale, MAX_SCALE, 1f).apply {
            addUpdateListener {
                val distanceTravelled = linearInterpolation(it.animatedFraction, startCenterX, itemView.centerX)
                val indicatorWidth = INDICATOR_WIDTH * (it.animatedValue as Float)

                val top = height - INDICATOR_MARGIN_BOTTOM - INDICATOR_HEIGHT
                val bottom = height - INDICATOR_MARGIN_BOTTOM
                val left = distanceTravelled - indicatorWidth / 2f
                val right = distanceTravelled + indicatorWidth / 2f

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

    private val View.centerX get() = left + width / 2f

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelAnimator(true)
    }
}