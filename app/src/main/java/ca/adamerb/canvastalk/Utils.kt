@file:Suppress("NOTHING_TO_INLINE")

package ca.adamerb.canvastalk

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Color.*
import android.util.Log

val PixelScale = AppContext.resources.displayMetrics.xdpi / 144f
inline fun dp(pixels: Int): Float = pixels * PixelScale

fun mixColor(c0: Int, c1: Int, amount: Float): Int =
    rgb(
        (red(c0) + (red(c1) - red(c0)) * amount).toInt(),
        (green(c0) + (green(c1) - green(c0)) * amount).toInt(),
        (blue(c0) + (blue(c1) - blue(c0)) * amount).toInt()
    )



fun runAnimation(
    duration: Long = 500,
    onEnd: (ValueAnimator) -> Unit = {},
    onUpdate: (Float) -> Unit = {}
) {
    ValueAnimator.ofFloat(0f, 1f).apply {
        val callbacks = object : ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                onEnd(animation as ValueAnimator)
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationUpdate(animation: ValueAnimator) {
                onUpdate.invoke(animatedFraction)
            }

        }
        addUpdateListener(callbacks)
        addListener(callbacks)
        this.duration = duration
    }.start()
}

fun logd(any: Any?) {
    Log.d("CanvasTalk", any.toString())
}