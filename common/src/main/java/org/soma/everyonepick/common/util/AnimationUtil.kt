package org.soma.everyonepick.common.util

import android.animation.ValueAnimator
import android.view.View

class AnimationUtil {
    companion object {
        fun startShowingUpAnimation(view: View, yOffset: Int, animationDuration: Long): ValueAnimator {
            return ValueAnimator.ofFloat(view.y + yOffset, view.y).apply {
                addUpdateListener { valueAnimator ->
                    view.alpha = animatedFraction
                    view.y = valueAnimator.animatedValue as Float
                }
                duration = animationDuration
                start()
            }
        }
    }
}