package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF

class Arrow(private val paint: Paint) {
    val position = PointF()
    val path = Path()
    var bodyWidthRatio = 0.5f
    var headHeightRatio = 0.5f
    var width = 0f
    var height = 0f
    fun layout(width: Float, height: Float) {
        val bodyWidth = width * bodyWidthRatio
        val headHeight = height * headHeightRatio
        var x: Float
        var y: Float
        path.apply {
            reset()
            x = width / 2 - bodyWidth / 2
            y = 0f
            moveTo(x, y)
            y = height - headHeight
            lineTo(x, y)
            x = 0f
            lineTo(x, y)
            x = width / 2f
            y = height
            lineTo(x, y)
            x = width
            y = height - headHeight
            lineTo(x, y)
            x = width / 2 + bodyWidth / 2
            lineTo(x, y)
            y = 0f
            lineTo(x, y)
            close()
        }
        this.width = width
        this.height = height
    }

    fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(position.x, position.y)
        canvas.drawPath(path, paint)
        canvas.restore()
    }
}