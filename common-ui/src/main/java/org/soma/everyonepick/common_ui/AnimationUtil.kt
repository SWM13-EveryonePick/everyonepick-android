package org.soma.everyonepick.common_ui

import android.animation.ValueAnimator
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class AnimationUtil {
    companion object {
        fun startShowingUpAnimation(view: View, yOffset: Int, animationDuration: Long) {
            ValueAnimator.ofFloat(view.y + yOffset, view.y).apply {
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