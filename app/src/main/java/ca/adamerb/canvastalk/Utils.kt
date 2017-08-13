@file:Suppress("NOTHING_TO_INLINE")

package ca.adamerb.canvastalk

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color.*
import android.graphics.Paint
import android.graphics.PointF
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.view.View


val PixelScale = AppContext.resources.displayMetrics.xdpi / 144f
inline fun dp(pixels: Int): Float = pixels * PixelScale
inline fun dp(pixels: Float): Float = pixels * PixelScale

fun mixColor(c0: Int, c1: Int, amount: Float): Int =
    argb(
        (alpha(c0) + (alpha(c1) - alpha(c0)) * amount).toInt(),
        (red(c0) + (red(c1) - red(c0)) * amount).toInt(),
        (green(c0) + (green(c1) - green(c0)) * amount).toInt(),
        (blue(c0) + (blue(c1) - blue(c0)) * amount).toInt()
    )



//inline fun <T> ArrayList<T>.forEachIndexed(function: (i: Int, value: T) -> Unit) {
//    val size = this.size
//    var i = 0
//    while(i < size) {
//        function(i, this[i])
//        i++
//    }
//}

fun Bitmap.blur(radius: Float): Bitmap {
    val original = this
    val bitmap = Bitmap.createBitmap(original.width, original.height, Bitmap.Config.ARGB_8888)

    val rs = RenderScript.create(AppContext)

    val allocIn = Allocation.createFromBitmap(rs, original)
    val allocOut = Allocation.createFromBitmap(rs, bitmap)

    val blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    blur.setInput(allocIn)
    blur.setRadius(radius)
    blur.forEach(allocOut)

    allocOut.copyTo(bitmap)
    rs.destroy()
    return bitmap
}

class ActionSequence(vararg actions: (ActionSequence) -> Unit) {
    var index = -1
    val actions = actions.toMutableList()

    fun executeNext(): Boolean {
        return if(index < actions.size - 1) {
            index++
            actions[index](this)
            true
        }  else false
    }
}

class Interpolation(val from: PointF, val to: PointF) {
    private val tmp = PointF()
    private val diffX = to.x - from.x
    private val diffY = to.y - from.y
    operator fun get(t: Float): PointF {
        tmp.set(
            from.x + diffX * t,
            from.y + diffY * t
        )
        return tmp
    }
}

val BackgroundPaint by lazy(LazyThreadSafetyMode.NONE) { Paint() }

fun Canvas.drawBackground(width: Int, height: Int, color: Int) {
    BackgroundPaint.color = color
    RectBuffer.set(0, 0, width, height)
    drawRect(RectBuffer, BackgroundPaint)
}

var runningAnimations = 0
    private set

fun Slide.runAnimation(
    duration: Long = 500,
    onEnd: (ValueAnimator) -> Unit = {},
    onUpdate: (Float) -> Unit = {}
) {
    view.runAnimation(duration, onEnd, onUpdate)
}

fun View.runAnimation(
    duration: Long = 500,
    onEnd: (ValueAnimator) -> Unit = {},
    onUpdate: (Float) -> Unit = {}
) {
    runningAnimations++
    ValueAnimator.ofFloat(0f, 1f).also {
        val callbacks = object : ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                onEnd(animation as ValueAnimator)
                runningAnimations--
                invalidate()
            }

            override fun onAnimationUpdate(animation: ValueAnimator) {
                onUpdate.invoke(animation.animatedFraction)
                invalidate()
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        }
        it.addUpdateListener(callbacks)
        it.addListener(callbacks)
        it.duration = duration
        it.start()
    }
}

fun logd(any: Any?) {
    Log.d("CanvasTalk", any.toString())
}