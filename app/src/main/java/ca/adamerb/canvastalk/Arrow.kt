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
        var x: Float = width / 2 - bodyWidth / 2
        var y: Float = 0f
        path.reset()
        path.moveTo(x, y)
        y = height - headHeight
        path.lineTo(x, y)
        x = 0f
        path.lineTo(x, y)
        x = width / 2f
        y = height
        path.lineTo(x, y)
        x = width
        y = height - headHeight
        path.lineTo(x, y)
        x = width / 2 + bodyWidth / 2
        path.lineTo(x, y)
        y = 0f
        path.lineTo(x, y)
        path.close()

        this.width = width
        this.height = height
    }

    fun draw(canvas: Canvas) {
        canvas.save()
        canvas.translate(position.x, position.y)
        canvas.drawPath(path, paint)
        canvas.restore()
    }
}