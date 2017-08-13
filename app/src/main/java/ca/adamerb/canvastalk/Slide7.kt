package ca.adamerb.canvastalk

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

class Slide7(override val view: SlideHolderView): Slide {

    val header = Header(view, "Custom Canvas")

    val bullets = Bullets(
        view = view,
        bullets = listOf(
            "You can create a bitmap backed Canvas, and just draw to a bitmap.",
            "Useful for applying blur operation."
        ),
        paint = Paint().apply {
            textSize = dp(13)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        }
    )

    init {
        runAnimation { t -> header.lineAnimation = t }
    }

    private var exampleDrawOperation: ((Canvas) -> Unit)? = null
        set(value) {
            view.invalidate()
            field = value
        }

    private var code: CodeSnippet? = null
        set(value) {
            field = value
            if(value != null) {
                layoutAndInvalidate()
            } else {
                view.invalidate()
            }
        }

    val actions = ActionSequence(
        { bullets.showNext() },
        {
            codeBackground.animateShow()
            code = CodeSnippet("""
Bitmap bitmap = //
Canvas canvas = new Canvas(bitmap);
view.onDraw(canvas);
            """)
        },
        {
            val bitmap =
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                    .also { onDraw(Canvas(it)) }

            val matrix = Matrix()

            exampleDrawOperation = {
                it.drawColor(mixColor(Black, 0, 0.25f))
                it.drawBitmap(bitmap, matrix, null)
            }
            runAnimation { t ->
                val scale = t * -0.25f + 1
                matrix.setTranslate(
                    view.width / 2 - view.width * scale / 2,
                    view.height / 2 - view.height * scale / 2
                )
                matrix.preScale(scale, scale)
            }
        },
        {
            exampleDrawOperation = null
            bullets.showNext()
        },
        {
            val bitmap =
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                    .also { onDraw(Canvas(it)) }

            val blurred = bitmap.blur(25f)

            val paint = Paint()
            val scale = 0.75f
            val matrix =
                Matrix().apply {
                    setTranslate(
                        view.width / 2 - view.width * scale / 2,
                        view.height / 2 - view.height * scale / 2
                    )
                    preScale(scale, scale)
                }

            exampleDrawOperation = {
                it.drawColor(mixColor(Black, 0, 0.25f))
                it.drawBitmap(bitmap, matrix, null)
                it.drawBitmap(blurred, matrix, paint)
            }
            runAnimation { t -> paint.alpha = (t * 255).toInt() }
        },
        {
            exampleDrawOperation = null
        },
        {
            view.slideIn(view.slideIndex + 1, SlideFrom.Right)
        }
    )

    val codeBackground = CodeBackground(view, header)

    override fun onLayout(width: Int, height: Int) {
        header.layout(width)

        bullets.position.set(PaddingF, header.lineY + PaddingF)
        bullets.layout(width / 2 - Padding * 2)

        codeBackground.layout(width, height)

        code?.let {
            val bounds = codeBackground.contentBounds
            it.layout(bounds.width().toInt(), bounds.height().toInt())
            it.position.x = bounds.left
            it.position.y = bounds.top
        }
    }

    private val alpha: Int = 255

    override fun onDraw(canvas: Canvas) {
        canvas.drawBackground(view.width, view.height, Shade3)

        canvas.saveLayerAlpha(0f, 0f, view.width.toFloat(), view.height.toFloat(), alpha, Canvas.ALL_SAVE_FLAG)

        codeBackground.draw(canvas)
        header.draw(canvas)
        bullets.draw(canvas)

        code?.draw(canvas)

        exampleDrawOperation?.invoke(canvas)

        canvas.restore()
    }

    override fun nextPressed() {
        actions.executeNext()
    }
}

