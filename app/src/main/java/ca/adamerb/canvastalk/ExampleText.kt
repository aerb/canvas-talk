package ca.adamerb.canvastalk

import android.graphics.*

class ExampleText(
    val text: String,
    val paint: Paint = Paint().apply {
        textSize = dp(30)
        color = White
        typeface = UbuntuBold
        isAntiAlias = true
    }
) {
    val boundsPaint = Paint().apply {
        color = Shade0
        isAntiAlias = true
    }

    val linePaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = dp(1)
        isAntiAlias = true
    }

    val position = PointF()
    val bounds = Rect()
    val offset = PointF()
    init {
        paint.getTextBounds(text, 0, text.length, bounds)
        offset.x = -bounds.left.toFloat()
        offset.y = -bounds.top.toFloat()
        bounds.offsetTo(0, 0)
    }

    val metrics = paint.fontMetrics!!

    val width = bounds.width()
    val height = bounds.height()

    var showBounds = false
    var showBaseline = false
    var showMetrics = false

    fun draw(canvas: Canvas) {
        canvas.save()
        canvas.translate(position.x, position.y)

        if(showBounds) {
            canvas.drawRect(bounds, boundsPaint)
        }

        canvas.drawText(text, 0, text.length, offset.x, offset.y, paint)

        if(showBaseline) {
            linePaint.color = Color.RED
            canvas.drawLine(0f, 0f, width.toFloat(), 0f, linePaint)
            canvas.drawLine(0f, offset.y, width.toFloat(), offset.y, linePaint)
            canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), linePaint)
        }

        if(showMetrics) {
            canvas.save()
            canvas.translate(0f, offset.y)
            linePaint.color = Color.YELLOW
            var y = metrics.top
            canvas.drawLine(0f, y, width.toFloat(), y, linePaint)
            y = metrics.bottom
            canvas.drawLine(0f, y, width.toFloat(), y, linePaint)
            canvas.restore()
        }

        canvas.restore()
    }
}