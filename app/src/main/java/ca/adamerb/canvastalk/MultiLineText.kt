package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

enum class Align { Left, Right }

class MultiLineText(
    var text: String = "",
    val paint: Paint =
        Paint().apply {
            textSize = dp(15)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        },
    val align: Align = Align.Left
) {
    private val lines = ArrayList<String>()
    val position = PointF()

    var height: Int = 0
        private set

    var width: Int = 0
        private set

    fun layoutText(width: Int) {
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
                    if(text[rangeEnd] == '\n') {
                        lines += text.substring(rangeStart, rangeEnd)
                        rangeStart = rangeEnd
                    } else if (paint.measureText(text, rangeStart, rangeEnd) > width) {
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

        if(rangeStart != text.length) {
            if (paint.measureText(text, rangeStart, text.length) > width) {
                lines += text.substring(rangeStart, lastBreak)
                lines += text.substring(lastBreak + 1, text.length)
            } else {
                lines += text.substring(rangeStart, text.length)
            }
        }

        paint.getFontMetrics(FontMetricsBuffer)
        this.height = FontMetricsBuffer.let { (it.bottom - it.top) * lines.size}.toInt()
        this.width = width
    }

    fun onDraw(canvas: Canvas) {
        paint.getFontMetrics(FontMetricsBuffer)
        val top = FontMetricsBuffer.top * -1
        val ascent = FontMetricsBuffer.ascent * -1
        val textHeight = top + FontMetricsBuffer.bottom

        var y = position.y + ascent
        for (i in 0..lines.size - 1) {
            val line = lines[i]
            if(align == Align.Left) {
                canvas.drawText(line, position.x, y, paint)
            } else {
                val lineWidth = paint.measureText(line)
                canvas.drawText(line, position.x + width - lineWidth, y, paint)
            }
            y += textHeight
        }
    }
}