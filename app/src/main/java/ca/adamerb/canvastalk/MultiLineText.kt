package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class MultiLineText(
    var text: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut a feugiat felis. Integer ac aliquet elit. Vestibulum quis erat fringilla, varius ex eu, cursus erat. Morbi lobortis luctus justo nec euismod. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Cras ullamcorper sapien consectetur libero aliquam, finibus viverra velit finibus. Curabitur venenatis rhoncus massa, quis lobortis nisl placerat vitae. Duis id lacus lacus. In ut sapien et felis auctor condimentum nec at orci. Sed turpis enim, fermentum id lacus ac, commodo blandit eros. Pellentesque et tellus felis. Nam convallis, magna in convallis fringilla, lorem est porta neque, aliquam pharetra sapien ipsum a sem.",
    val paint: Paint =
        Paint().apply {
            textSize = dp(15)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        }
) {
    private val lines = ArrayList<String>()
    val position = PointF()

    fun setMaxWidth(maxWidth: Int) {
        lines.clear()
        var lastBreak = 0
        var rangeStart = 0
        var rangeEnd = 0
        while (rangeEnd < text.length) {
            if(text[rangeEnd].isWhitespace()) {
                if(rangeStart == rangeEnd) {
                    rangeStart++
                    rangeEnd++
                } else {
                    if (paint.measureText(text, rangeStart, rangeEnd) > maxWidth) {
                        lines += text.substring(rangeStart, lastBreak)
                        rangeStart = lastBreak
                        rangeEnd = lastBreak
                    } else {
                        lastBreak = rangeEnd
                        rangeEnd++
                    }
                }
            } else rangeEnd++
        }
    }

    fun onDraw(canvas: Canvas) {
        paint.getFontMetrics(FontMetricsBuffer)
        val top = FontMetricsBuffer.top * -1
        val textHeight = top + FontMetricsBuffer.bottom

        var y = position.y + top
        for (i in 0..lines.size - 1) {
            val line = lines[i]
            canvas.drawText(line, position.x, y, paint)
            y += textHeight
        }
    }
}