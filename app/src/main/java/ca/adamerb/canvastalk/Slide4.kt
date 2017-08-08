package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF

class Slide4(private val view: SlideHolderView): Slide {

    private val paint =
        Paint().apply {
            textSize = dp(20)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        }

    var header: Header? = Header(view, "More Useful Methods")
    var bullets: Bullets? = Bullets(
        view = view,
        paint = Paint().apply {
            textSize = dp(13)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        },
        bullets = listOf(
            "More complex geometry can be drawn using Path, and drawPath()",
            "The drawArc() method is great for pie charts.",
            "The drawRoundedRect() method is useful for buttons.",
            "Draw text with drawText()."
        )
    )


    var codeSnippet: CodeSnippet? = null
    var codeBackground = CodeBackground(view, header!!)
    val contentCenter = PointF()

    var alpha: Int = 255
    init {
        runAnimation(duration = 1000) { t ->
            header?.lineAnimation = t
            alpha = (t * 255).toInt()
            view.invalidate()
        }
    }

    override fun onLayout(width: Int, height: Int) {
        header?.let { header ->
            header.layout(width)
            contentCenter.set(width * 3f / 4f, header.lineY + (view.height - header.lineY) / 2)

            bullets?.let { bullets ->
                bullets.position.set(PaddingF, header.lineY + PaddingF)
                bullets.layout(width / 2 - Padding * 2)
            }

            codeBackground.layout(width, height)

            codeSnippet?.let {
                val bounds = codeBackground.contentBounds
                it.layout(bounds.width().toInt(), bounds.height().toInt())
                it.position.x = bounds.left
                it.position.y = bounds.top
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        RectBuffer.set(0, 0, view.width, view.height)
        paint.color = Shade4
        canvas.drawRect(RectBuffer, paint)

        canvas.saveLayerAlpha(0f, 0f, view.width.toFloat(), view.height.toFloat(), alpha, Canvas.ALL_SAVE_FLAG)
        codeBackground.draw(canvas)

        header?.draw(canvas)
        bullets?.draw(canvas)

        codeSnippet?.draw(canvas)
        exampleDrawOperation?.invoke(canvas)

        canvas.restore()
    }

    private var exampleDrawOperation: ((Canvas) -> Unit)? = null

    private val examplePaint =
        Paint().apply {
            color = White
            strokeWidth = StrokeWidth
            style = Paint.Style.STROKE
            isAntiAlias = true
        }


    val actionSequence = ActionSequence(
        { bullets?.showNext() },
        {
            codeBackground.animateShow()
            codeSnippet =
                CodeSnippet("""
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
                    }"""
                )
            onLayout(view.width, view.height)
        },
        {
            codeSnippet = null
            val arrow = Arrow(examplePaint)
            arrow.layout(dp(50), dp(50))
            arrow.position.set(contentCenter.x - arrow.width / 2, contentCenter.y - arrow.height / 2)
            exampleDrawOperation = { arrow.draw(it) }
            view.invalidate()
        },
        {
            exampleDrawOperation = null
            bullets?.showNext()
        },
        {
            codeSnippet =
                CodeSnippet("""
                    var angle = 0f
                    val rad = dp(30)
                    val bounds = RectF(
                        contentCenter.x - rad, contentCenter.y - rad,
                        contentCenter.x + rad, contentCenter.y + rad
                    )
                    canvas.drawArc(bounds, 0f, angle, true, examplePaint)"""
                )
            onLayout(view.width, view.height)
            view.invalidate()
        },
        {
            codeSnippet = null
            var angle = 0f
            val rad = dp(30)
            val bounds = RectF(
                contentCenter.x - rad, contentCenter.y - rad,
                contentCenter.x + rad, contentCenter.y + rad
            )

            runAnimation(duration = 1000, invalidate = view) { angle = 360 * it }
            exampleDrawOperation = { canvas ->
                canvas.drawArc(bounds, 0f, angle, true, examplePaint)
            }
        },
        {
            exampleDrawOperation = null
            bullets?.showNext()
        },
        {
            codeSnippet = CodeSnippet("canvas.drawRoundRect(bounds, dp(5), dp(5), paint)")
            onLayout(view.width, view.height)
            view.invalidate()
        },
        {
            codeSnippet = null

            val textPaint =
                Paint().apply {
                    textSize = dp(12)
                    color = White
                    typeface = UbuntuBold
                    isAntiAlias = true
                }

            val bounds = RectF()
            val textPosition = PointF()
            val text = "Click Me"
            textPaint.getTextBounds(text, 0, text.length, RectBuffer)
            textPosition.x = contentCenter.x - RectBuffer.width() / 2
            textPosition.y = contentCenter.y + RectBuffer.height() / 2

            bounds.set(0f, 0f, RectBuffer.width() + PaddingF * 2, RectBuffer.height() + PaddingF)
            bounds.offsetTo(
                contentCenter.x - bounds.width() / 2,
                contentCenter.y - bounds.height() / 2
            )

            exampleDrawOperation = { canvas ->
                canvas.drawRoundRect(bounds, dp(5), dp(5), examplePaint)
                canvas.drawText(text, textPosition.x, textPosition.y, textPaint)
            }
            view.invalidate()
        },
        {
            exampleDrawOperation = null
            bullets?.showNext()
        },
        {
            val header = Header(view, "Draw Text!")
            header.layout(view.width)
            header.title.let {
                it.position.x = contentCenter.x - it.width / 2
                it.position.y = contentCenter.y - it.height / 2
            }
            exampleDrawOperation = {
                header.draw(it)
            }
            view.invalidate()
        },
        {
            val header = Header(view, "Draw Text!")
            header.layout(view.width)

            val position = Interpolation(
                PointF(
                    contentCenter.x - header.title.width / 2,
                    contentCenter.y - header.title.height / 2
                ),
                PointF(PaddingF, PaddingF)
            )

            this.header = null
            this.bullets = null

            exampleDrawOperation = {
                header.draw(it)
            }
            runAnimation(duration = 1000, invalidate = view) { time ->
                header.title.position.set(position[time])
                header.lineAnimation = time
                codeBackground.bounds.left = (view.width / 2) * (1f - time)
            }
        },
        {
            view.slide = view.slides[++view.slideIndex]()
        }
    )

    override fun nextPressed() {
        actionSequence.executeNext()
    }
}