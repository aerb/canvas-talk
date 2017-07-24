package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class Bullets(
    private val paint: Paint =
        Paint().apply {
            textSize = dp(18)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        },
    bullets: List<String>
) {

    private val texts = bullets.map { MultiLineText(it, paint) }

    val position = PointF()

    var width: Int = 0
        private set
    var height: Int = 0
        private set

    private val lineSpace = dp(7)
    private val bulletInset = dp(20).toInt()
    private val bulletRad = dp(3f)

    fun layout(width: Int) {
        var y = 0f
        for(i in 0..texts.size - 1) {
            val it = texts[i]
            it.position.set(bulletInset.toFloat(), y)
            it.layoutText(width - bulletInset)
            y += it.height
            if(i != texts.size - 1) {
                y += lineSpace
            }
        }
        this.height = y.toInt()
        this.width = width
    }

    fun onDraw(canvas: Canvas) {
        paint.getFontMetrics(FontMetricsBuffer)
        val bY = FontMetricsBuffer.let { it.bottom - it.top } / 2
        val bX = bulletInset / 2

        canvas.save()
        canvas.translate(position.x, position.y)

//        paint.color = Shade0
//        RectBuffer.set(0, 0, width, height)
//        canvas.drawRect(RectBuffer, paint)
//        paint.color = White

        for(i in 0..texts.size - 1) {
            val it = texts[i]
            canvas.drawCircle(bX.toFloat(), it.position.y + bY, bulletRad, paint)
            it.onDraw(canvas)
        }
        canvas.restore()
    }
}