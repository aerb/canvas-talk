package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF

class Slide3(override val view: SlideHolderView): Slide {
    val header = Header(view, "The Basics")
    val bullets = Bullets(
        view = view,
        paint = Paint().apply {
            textSize = dp(13)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        },
        bullets = listOf(
            "A canvas instance can be attained by subclassing View and overriding onDraw()",
            "Canvas has a large amount of draw*() methods. Eg. drawRect, drawCircle, drawLine",
            "All of the draw*() methods take positional arguments, and an instance of paint.",
            "Paint describes color, path effect, color-filters."
        )
    ).also {
        it.showNextCallback = { index ->
            when(index) {
                0 -> {
                    codeBackground.animateShow()
                    codeSnippet = CodeSnippet("""
                        public class CustomView extends View {
                          public CustomView(Context context) {
                            super(context);
                          }

                          @Override
                          protected void onDraw(Canvas canvas) {

                          }
                        }"""
                    )
                }
                1 -> {
                    codeSnippet?.recycle()
                    codeSnippet = CodeSnippet("""
                        public class CustomView extends View {
                          public CustomView(Context context) {
                            super(context);
                          }

                          @Override
                          protected void onDraw(Canvas canvas) {
                            canvas.drawLine(0, 0, w, h, paint);
                            canvas.drawRect(0, 0, w, h, paint);
                            canvas.drawCircle(0, 0, r, paint);
                          }
                        }"""
                    )
                }
                2 -> {
                    codeSnippet?.recycle()
                    codeSnippet = CodeSnippet("""
                        public class CustomView extends View {
                          Paint paint;
                          public CustomView(Context context) {
                            super(context);
                            paint = new Paint();
                            paint.setColor(Color.RED);
                            paint.setStyle(Paint.Style.STROKE);
                          }

                          @Override
                          protected void onDraw(Canvas canvas) {
                            canvas.drawRect(0, 0, w, h, paint);
                            canvas.drawCircle(0, 0, r, paint);
                            canvas.drawLine(0, 0, w, h, paint);
                          }
                        }"""
                    )
                }
            }
            onLayout(view.width, view.height)
            view.invalidate()
        }
    }

    var codeSnippet: CodeSnippet? = null
    val codeBackground = CodeBackground(view, header)

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
        bullets.position.set(PaddingF, header.lineY + PaddingF)
        bullets.layout(width / 2 - Padding * 2)

        codeSnippet?.let {
            it.layout(codeBackground.contentBounds.width().toInt(), codeBackground.contentBounds.height().toInt())
            it.scale = (width / 2 - PaddingF * 2) / it.width
            it.position.x = width - PaddingF - it.scaledWidth
            it.position.y = header.lineY + PaddingF
        }

        codeBackground.layout(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBackground(view.width, view.height, Shade2)

        canvas.saveLayerAlpha(0f, 0f, view.width.toFloat(), view.height.toFloat(), alpha, Canvas.ALL_SAVE_FLAG)

        codeBackground.draw(canvas)

        header.draw(canvas)
        bullets.draw(canvas)

        codeSnippet?.draw(canvas)

        drawOps.forEach { it(canvas) }
        canvas.restore()
    }

    private val examplePaint =
        Paint().apply {
            color = White
            strokeWidth = StrokeWidth
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

    val drawOps = ArrayList<(Canvas) -> Unit>()
    fun showDemonstrations(): Boolean {
        codeSnippet = null

        val w = view.width

        val circleRad = dp(30)
        val squareEdge = circleRad * 2
        val space = dp(10)

        val x = w * 3f / 4f

        val rectY = header.lineY + (view.height - header.lineY) / 2 - (squareEdge * 3 + space * 2) / 2

        val rect = RectF(0f, 0f, squareEdge, squareEdge)
        rect.offsetTo(x - rect.width() / 2, rectY)

        val circle = PointF(x, rect.bottom + space + circleRad)

        val lineStart = circle.y + circleRad + space
        val line = listOf(
            PointF(x - circleRad, lineStart),
            PointF(x + circleRad, lineStart + circleRad * 2)
        )

        when(drawOps.size) {
            0 -> drawOps += { it.drawRect(rect, examplePaint) }
            1 -> drawOps += { it.drawCircle(circle.x, circle.y, circleRad, examplePaint) }
            2 -> drawOps += { it.drawLine(line[0].x, line[0].y, line[1].x, line[1].y, examplePaint) }
            else -> return false
        }

        view.invalidate()
        return true
    }

    override fun nextPressed() {
        if(!bullets.showNext() && !showDemonstrations()) {
            view.slideIn(view.slideIndex + 1, SlideFrom.Top)
        }
    }
}


