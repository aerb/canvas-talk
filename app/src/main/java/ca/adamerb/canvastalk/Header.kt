package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint

class Header(
    private val view: SlideHolderView,
    text: String
) {
    private var lineStart: Float = 0f
    private var lineWidth: Float = 0f
    var lineY: Float = 0f
    var lineAnimation: Float = 0f

    val title = MultiLineText(
        text = text,
        paint = Paint().apply {
            textSize = dp(25)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        }
    )

    private val paint =
        Paint().apply {
            color = White
            isAntiAlias = true
            strokeWidth = StrokeWidth
            style = Paint.Style.STROKE
        }

    fun layout(width: Int) {
        lineStart = PaddingF
        lineWidth = width - PaddingF * 2

        title.position.set(PaddingF, PaddingF)
        title.layoutText(lineWidth.toInt())

        lineY = title.position.y + title.height
    }

    fun draw(canvas: Canvas) {
        canvas.drawLine(lineStart, lineY, lineStart + (lineWidth) * lineAnimation, lineY, paint)
        title.draw(canvas)
    }
}