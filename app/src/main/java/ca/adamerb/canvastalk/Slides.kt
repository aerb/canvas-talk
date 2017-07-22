package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import java.lang.Math.*

interface Slide {
    fun onLayout(width: Int, height: Int)
    fun onDraw(canvas: Canvas)
    fun onClick()
}

class Slide0(private val view: SlideHolderView): Slide {

    private val paint =
        Paint().apply {
            textSize = dp(40)
            typeface = UbuntuBold
            color = White
            isAntiAlias = true
        }

    private val rectBuffer = Rect()
    private var progress: Float = 0f

    private var circleRad = 0
    override fun onLayout(width: Int, height: Int) {
        circleRad = ceil(sqrt((width * width + height * height).toDouble())).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Shade0)

        paint.color = White
        val text = "The Android Canvas"
        paint.getTextBounds(text, 0, text.length, rectBuffer)
        canvas.drawText(text, view.width / 2f - rectBuffer.width() / 2, view.height / 2f, paint)

        paint.color = Shade2
        canvas.drawCircle(view.width.toFloat(), view.height.toFloat(), circleRad * progress, paint)
    }

    override fun onClick() {
        runAnimation(
            onUpdate = { t ->
                progress = t
                view.invalidate()
            },
            onEnd = {
                view.slide = Slide1(view)
            }
        )
    }
}

class Slide1(private val view: SlideHolderView): Slide {

    private val paint =
        Paint().apply {
            textSize = dp(20)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        }

    private val multiLineText = MultiLineText()

    init {
        runAnimation(duration = 1000) { t ->
            val color = mixColor(Shade2, White, t)
            paint.color = color
            multiLineText.paint.color = color
            view.invalidate()
        }
    }

    override fun onLayout(width: Int, height: Int) {
        multiLineText.position.set(dp(10), dp(20))
        multiLineText.setMaxWidth(width / 2)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Shade2)

        val text = "Adam Erb"
        paint.getTextBounds(text, 0, text.length, RectBuffer)
        canvas.drawText(text, 0f, RectBuffer.height().toFloat(), paint)

        multiLineText.onDraw(canvas)

    }

    override fun onClick() {
        view.slide = Slide0(view)
    }

}
