package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

enum class Align { Left, Right, Center }

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
    private val lineWidths = ArrayList<Int>()

    val position = PointF()

    var height: Int = 0
        private set

    var width: Int = 0
        private set

    var trimLeading = true

    fun layoutText(width: Int) {
        lines.clear()
        lineWidths.clear()
        var lastBreak = 0
        var rangeStart = 0
        var rangeEnd = 0
        var maxWidth = 0
        while (rangeEnd < text.length) {
            if(text[rangeEnd].isWhitespace()) {
                if(rangeStart == rangeEnd) {
                    if(trimLeading) rangeStart++
                    rangeEnd++
                } else {
                    if(text[rangeEnd] == '\n') {
                        lines += text.substring(rangeStart, rangeEnd)
                        lineWidths += paint.measureText(lines.last()).toInt()
                        maxWidth = maxOf(maxWidth, lineWidths.last())
                        rangeStart = rangeEnd
                    } else {
                        if (paint.measureText(text, rangeStart, rangeEnd) > width) {
                            lines += text.substring(rangeStart, lastBreak)
                            lineWidths += paint.measureText(lines.last()).toInt()
                            maxWidth = maxOf(maxWidth, lineWidths.last())
                            rangeStart = lastBreak
                            rangeEnd = lastBreak
                        } else {
                            lastBreak = rangeEnd
                            rangeEnd++
                        }
                    }
                }
            } else rangeEnd++
        }

        if(rangeStart != text.length) {
            if (paint.measureText(text, rangeStart, text.length) > width) {
                lines += text.substring(rangeStart, lastBreak)
                lineWidths += paint.measureText(lines.last()).toInt()
                maxWidth = maxOf(maxWidth, lineWidths.last())
                lines += text.substring(lastBreak + 1, text.length)
                lineWidths += paint.measureText(lines.last()).toInt()
                maxWidth = maxOf(maxWidth, lineWidths.last())
            } else {
                lines += text.substring(rangeStart, text.length)
                lineWidths += paint.measureText(lines.last()).toInt()
                maxWidth = maxOf(maxWidth, lineWidths.last())
            }
        }

        paint.getFontMetrics(FontMetricsBuffer)
        this.height = FontMetricsBuffer.let { (it.bottom - it.top) * lines.size}.toInt()
        this.width = maxWidth
    }

    fun draw(canvas: Canvas) {
        paint.getFontMetrics(FontMetricsBuffer)
        val top = FontMetricsBuffer.top * -1
        val ascent = FontMetricsBuffer.ascent * -1
        val textHeight = top + FontMetricsBuffer.bottom

        var y = position.y + ascent
        for (i in 0..lines.size - 1) {
            val line = lines[i]
            when (align) {
                Align.Left -> canvas.drawText(line, position.x, y, paint)
                Align.Right -> {
                    canvas.drawText(line, position.x + width - lineWidths[i], y, paint)
                }
                Align.Center -> {
                    canvas.drawText(line, position.x + width / 2 - lineWidths[i] / 2, y, paint)
                }
            }
            y += textHeight
        }
    }

    val bottom: Float get() = position.y + height
}