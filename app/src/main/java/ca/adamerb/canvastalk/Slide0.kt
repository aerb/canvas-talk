package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint

class Slide0(private val view: SlideHolderView): Slide {

    private val paint =
        Paint().apply {
            textSize = dp(40)
            typeface = UbuntuBold
            color = White
            isAntiAlias = true
        }

    private var progress: Float = 0f
    private var circleRad = 0
    private val credsText =
        MultiLineText(
            text = "Adam Erb\n@erbal\nadam.l.erb@gmail.com",
            align = Align.Right
        )

    override fun onLayout(width: Int, height: Int) {
        circleRad = Math.ceil(Math.sqrt((width * width + height * height).toDouble())).toInt()

        credsText.layoutText(width - Padding * 2)
        credsText.position.set(width - PaddingF - credsText.width, height - PaddingF - credsText.height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Shade0)

        paint.color = White
        val text = "The Android Canvas"
        paint.getTextBounds(text, 0, text.length, RectBuffer)
        canvas.drawText(text, view.width / 2f - RectBuffer.width() / 2, view.height / 2f, paint)

        credsText.draw(canvas)

        paint.color = Shade2
        canvas.drawCircle(view.width.toFloat(), view.height.toFloat(), circleRad * progress, paint)
    }

    override fun nextPressed() {
        runAnimation(
            onUpdate = { t ->
                progress = t
                view.invalidate()
            },
            onEnd = {
                view.slide = view.slides[++view.slideIndex]()
            }
        )
    }
}