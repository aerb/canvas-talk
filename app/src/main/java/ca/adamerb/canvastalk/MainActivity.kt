package ca.adamerb.canvastalk

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(JustaView(this))
    }
}


private const val Shade0 = 0xff2F4172.toInt()
private const val Shade1 = 0xff7986AC.toInt()
private const val Shade2 = 0xff4F608F.toInt()
private const val White = 0xffffffff.toInt()


//shade 0 = #2F4172 = rgb( 47, 65,114) = rgba( 47, 65,114,1) = rgb0(0.184,0.255,0.447)
//shade 1 = #7986AC = rgb(121,134,172) = rgba(121,134,172,1) = rgb0(0.475,0.525,0.675)
//shade 2 = #4F608F = rgb( 79, 96,143) = rgba( 79, 96,143,1) = rgb0(0.31,0.376,0.561)
//shade 3 = #162756 = rgb( 22, 39, 86) = rgba( 22, 39, 86,1) = rgb0(0.086,0.153,0.337)
//shade 4 = #061439 = rgb(  6, 20, 57) = rgba(  6, 20, 57,1) = rgb0(0.024,0.078,0.224)


fun runAnimation(
    onUpdate: (Float) -> Unit,
    onEnd: (ValueAnimator) -> Unit
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
        duration = 500
    }.start()
}


class JustaView(c: Context): View(c) {

    private val pixelScale = c.resources.displayMetrics.xdpi / 144f
    private fun dp(pixels: Int): Float = pixels * pixelScale

    private val paint =
        Paint().apply {
            textSize = dp(40)
            color = White
            isAntiAlias = true
        }

    inner class Slide() {

        var t: Float = 0f
        fun onDraw(canvas: Canvas) {
            canvas.drawColor(Shade0)

            paint.color = White
            val text = "The Android Canvas"
            paint.getTextBounds(text, 0, text.length, rectBuffer)
            canvas.drawText(text, width / 2f - rectBuffer.width() / 2, height / 2f - rectBuffer.height() / 2, paint)

            paint.color = Shade2
            canvas.drawCircle(width.toFloat(), height.toFloat(), width * t * 1.5f, paint)
        }

    }

    private val rectBuffer = Rect()

    private var slide = Slide()

    init {
        setOnClickListener {
            runAnimation(
                onUpdate = { t ->
                    slide.t = t
                    invalidate()
                },
                onEnd = {}
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        slide.onDraw(canvas)
    }
}
