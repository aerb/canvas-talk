package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class Bullets(
    val view: SlideHolderView,
    val paint: Paint =
        Paint().apply {
            textSize = dp(18)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        },
    bullets: List<String>,
    var showNextCallback: (index: Int) -> Unit = {}
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

    fun draw(canvas: Canvas) {
        paint.getFontMetrics(FontMetricsBuffer)
        val bY = FontMetricsBuffer.let { it.bottom - it.top } / 2
        val bX = bulletInset / 2

        canvas.save()
        canvas.translate(position.x, position.y)

        for(i in 0..showUpToIndex) {
            val it = texts[i]
            if(i < showUpToIndex) {
                canvas.drawCircle(bX.toFloat(), it.position.y + bY, bulletRad, paint)
                it.draw(canvas)
            } else {
                val oldAlpha = paint.alpha
                paint.alpha = alpha
                canvas.drawCircle(bX.toFloat(), it.position.y + bY, bulletRad, paint)
                it.draw(canvas)
                paint.alpha = oldAlpha
            }
        }
        canvas.restore()
    }

    var showUpToIndex: Int = -1
    var alpha: Int = 255
    fun showNext(): Boolean {
        return if(showUpToIndex < texts.size - 1) {
            showUpToIndex ++
            showNextCallback(showUpToIndex)
            runAnimation { t ->
                alpha = (t * 255).toInt()
                view.invalidate()
            }
            true
        } else false
    }
}