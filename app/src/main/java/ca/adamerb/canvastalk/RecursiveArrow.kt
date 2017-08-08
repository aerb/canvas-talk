package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class RecursiveArrow(val depth: Int) {
    val arrow = Arrow(
        Paint().apply {
            color = White
            strokeWidth = StrokeWidth
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    ).also { it.rotation = 180f }

    fun layout() {
        arrow.layout(dp(50), dp(75))
    }

    val position = PointF()

    var rotation: Float = 0f

    private fun drawArrow(canvas: Canvas, i: Int) {
        if(i < depth) {
            canvas.save()

            canvas.rotate(rotation, arrow.width / 2, arrow.height / 2)
            arrow.draw(canvas)
            canvas.scale(0.6f, 0.6f, arrow.width / 2, arrow.height / 2)
            canvas.translate(0f, -dp(125))
            drawArrow(canvas, i + 1)
            canvas.restore()
        }
    }

    fun draw(canvas: Canvas) {
        canvas.save()
        canvas.translate(position.x, position.y)
        drawArrow(canvas, 0)
        canvas.restore()
    }
}