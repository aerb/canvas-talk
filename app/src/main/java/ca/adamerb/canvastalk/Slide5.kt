package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class Slide5(override val view: SlideHolderView): Slide {

    val header = Header(view, "Drawing Text").also { it.lineAnimation = 1f }

    val bullets = Bullets(
        view = view,
        bullets = listOf(
            "Draw text with drawText()",
            "Use getTextBounds() to determine layout.",
            "Rect top and bottom fields are relative to baseline.",
            "Use getFontMetrics() to determine max and min bounds."
        ),
        paint = Paint().apply {
            textSize = dp(13)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        }
    )

    private var exampleDrawOperation: ((Canvas) -> Unit)? = null
        set(value) {
            if(value != null) {
                view.invalidate()
            }
            field = value
        }

    private var code: CodeSnippet? = null


    val actions = ActionSequence(
        { bullets.showNext() },
        {
            codeBackground.animateShow()
            code = CodeSnippet("""
            String text = "Lorem Ipsum";
            canvas.drawText(text,
                0, text.length,
                x, y,
                paint
            );""")
            layoutAndInvalidate()
        },
        {
            code = null
            val text = ExampleText("Lorem Ipsum")
            text.position.x = contentCenter.x - text.width / 2
            text.position.y = contentCenter.y - text.height / 2
            exampleDrawOperation = {
                text.draw(it)
            }
        },
        { bullets.showNext() },
        {
            exampleDrawOperation = null
            code = CodeSnippet("""
                String text = "Lorem Ipsum";
                Rect bounds = new Rect();
                paint.getTextBounds(text,
                    0, text.length, bounds);
                """)
            layoutAndInvalidate()
        },
        {
            code = null
            val text = ExampleText("Lorem Ipsum")
            text.position.x = contentCenter.x - text.width / 2
            text.position.y = contentCenter.y - text.height / 2
            text.showBounds = true
            exampleDrawOperation = {
                text.draw(it)
            }
        },
        { bullets.showNext() },
        {
            exampleDrawOperation = null
            code = CodeSnippet("""
                String text = "Lorem Ipsum";
                Rect bounds = new Rect();
                paint.getTextBounds(text,
                    0, text.length, bounds);
                int baselineToTop = bounds.top * -1;
                int baselineToBottom = bounds.bottom;
                """)
            layoutAndInvalidate()
        },
        {
            code = null
            val text = ExampleText("Lorem Ipsum")
            text.position.x = contentCenter.x - text.width / 2
            text.position.y = contentCenter.y - text.height / 2
            text.showBounds = true
            text.showBaseline = true
            exampleDrawOperation = {
                text.draw(it)
            }
        },
        { bullets.showNext() },
        {
            exampleDrawOperation = null
            code = CodeSnippet("""
                String text = "Lorem Ipsum";
                Rect bounds = new Rect();
                FontMetrics m = paint.getFontMetrics();
                int max = m.top * -1;
                int min = bounds.bottom;
                """)
            layoutAndInvalidate()
        },
        {
            code = null
            val text = ExampleText("Lorem Ipsum")
            text.position.x = contentCenter.x - text.width / 2
            text.position.y = contentCenter.y - text.height / 2
            text.showBounds = true
            text.showBaseline = true
            text.showMetrics = true
            exampleDrawOperation = {
                text.draw(it)
            }
        },
        {
            view.slideIn(view.slideIndex + 1, SlideFrom.Bottom)
        }
    )

    val codeBackground = CodeBackground(view, header)

    var alpha: Int = 255

    val contentCenter = PointF()
    override fun onLayout(width: Int, height: Int) {
        header.layout(width)

        bullets.position.set(PaddingF, header.lineY + PaddingF)
        bullets.layout(width / 2 - Padding * 2)

        contentCenter.set(width * 3f / 4f, header.lineY + (view.height - header.lineY) / 2)

        codeBackground.layout(width, height)

        code?.let {
            val bounds = codeBackground.contentBounds
            it.layout(bounds.width().toInt(), bounds.height().toInt())
            it.position.x = bounds.left
            it.position.y = bounds.top
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBackground(view.width, view.height, Shade5)

        canvas.saveLayerAlpha(0f, 0f, view.width.toFloat(), view.height.toFloat(), alpha, Canvas.ALL_SAVE_FLAG)

        codeBackground.draw(canvas)
        header.draw(canvas)
        bullets.draw(canvas)

        exampleDrawOperation?.invoke(canvas)

        code?.draw(canvas)

        canvas.restore()
    }

    override fun nextPressed() {
        actions.executeNext()
    }
}

