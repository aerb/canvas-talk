package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View

class CodeBackground(private val view: View, private val header: Header) {
    private val paint = Paint().apply {
        color = CodeBackgroundColor
    }

    val bounds = RectF()
    val contentBounds = RectF()
    private var show = false
    private var animation = 0f


    fun layout(width: Int, height: Int) {
        bounds.set(width / 2f, 0f, width.toFloat(), height.toFloat())

        contentBounds.set(0f, 0f, bounds.width() - PaddingF * 2, height - header.lineY - PaddingF * 2)
        contentBounds.offsetTo(bounds.left + PaddingF, header.lineY + PaddingF)
    }

    fun animateShow() {
        show = true
        view.runAnimation { animation = it }
    }

    fun draw(canvas: Canvas) {
        if(show) {
            canvas.drawRect(bounds.left, bounds.top, bounds.right, bounds.bottom * animation, paint)
        }
    }
}