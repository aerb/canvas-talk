package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class Slide2(private val view: SlideHolderView): Slide {

    private val header = Header(view, "What? The Android Graphics Api")

    private val rects = List(4) { RectF() }

    private val paint =
        Paint().apply {
            color = White
            isAntiAlias = true
            strokeWidth = StrokeWidth
            style = Paint.Style.STROKE
        }

    private val arrowPaint =
        Paint().apply {
            color = White
            isAntiAlias = true
            alpha = 255 * 3 / 4
        }
    private val arrows = List(3) { Arrow(arrowPaint) }

    var alpha: Int = 255
    init {
        runAnimation(duration = 1000) { t ->
            header.lineAnimation = t
            alpha = (t * 255).toInt()
            view.invalidate()
        }
    }

    override fun onLayout(width: Int, height: Int) {
        header.layout(width)

        var y = header.lineY + dp(10)
        val boxHeight = (height - PaddingF - y - Spacing * 3) / 4
        items.forEachIndexed { i, text ->
            text.layoutText(width - Padding * 2)
            val rectWidth = text.width + PaddingF * 2
            val x = (width / 2 - rectWidth / 2)
            rects[i].set(x, y, x + rectWidth, y + boxHeight)

            text.position.set(width / 2f - text.width / 2f, rects[i].centerY() - text.height / 2)

            if(i > 0) {
                val top = items[i - 1].bottom + dp(5)
                val arrowHeight = text.position.y - dp(5) - top
                val arrow = arrows[i - 1]
                arrow.layout(arrowHeight * 0.75f, arrowHeight)
                arrow.position.set(width / 2 - arrow.width / 2, top)
            }

            y += boxHeight + Spacing
        }
    }

    override fun onDraw(canvas: Canvas) {
        RectBuffer.set(0, 0, view.width, view.height)

        paint.style = Paint.Style.FILL
        paint.color = Shade3
        canvas.drawRect(RectBuffer, paint)

        canvas.saveLayerAlpha(0f, 0f, view.width.toFloat(), view.height.toFloat(), alpha, Canvas.ALL_SAVE_FLAG)

        header.draw(canvas)

        paint.style = Paint.Style.STROKE
        paint.color = White

        items.forEachIndexed { i, value ->
            canvas.drawRoundRect(rects[i], CornerF, CornerF, paint)
            value.draw(canvas)
        }

        arrows.forEach { it.draw(canvas) }

        canvas.restore()
    }

    private val items = ArrayList<MultiLineText>()

    fun animateItem() {
        onLayout(view.width, view.height)
        runAnimation { t ->
            items.last().paint.alpha = (t * 255).toInt()
            view.invalidate()
        }
    }

    override fun nextPressed() {
        if(items.size > 3) {
            view.slideIn(view.slideIndex + 1, SlideFrom.Left)
            return
        }

        items += MultiLineText(
            text = when (items.size) {
                0 -> "Android Layouts & Resource XML"
                1 -> "Views (LinearLayout / ImageView / TextView)"
                2 -> "Canvas API"
                3 -> "OpenGL Display List, Software Drawing Commands"
                else -> throw IllegalStateException()
            },
            align = Align.Center
        )
        animateItem()
    }
}