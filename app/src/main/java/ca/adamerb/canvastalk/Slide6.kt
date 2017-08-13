package ca.adamerb.canvastalk

import android.graphics.*

class Slide6(override val view: SlideHolderView): Slide {

    val header = Header(view, "Shaders & Color Filters")

    val bullets = Bullets(
        view = view,
        bullets = listOf(
            "Paint can be assigned a shader, for specialized fill effects.",
            "You can use BitmapShader to crop bitmaps to shapes.",
            "Gradient effect can also be achieved with Linear, Radial, Sweet Gradient shaders.",
            "Paint can be assigned color filter. Useful for icon re-use."
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
            if(value != null) {
                view.invalidate()
            }
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
        { bullets.showNext() },
        {
            codeBackground.animateShow()
            code = CodeSnippet("""
Bitmap bitmap = //
BitmapShader shader =
    new BitmapShader(
        bitmap,
        Shader.TileMode.REPEAT,
        Shader.TileMode.REPEAT
    );

Paint paint = new Paint();
paint.setShader(shader);

canvas.drawCircle(0f, 0f,
    radius, paint);
            """)
        },
        {
            code = null

            val bmp = BitmapFactory.decodeResource(view.context.resources, R.drawable.android)
            val w = dp(250)
            val scale = w / bmp.width
            val matrix = Matrix()
            matrix.preScale(scale, scale)
            matrix.preTranslate(dp(45), dp(50))
            val paint = Paint().apply {
                color = White
                shader = BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
                    .also { it.setLocalMatrix(matrix) }
                isAntiAlias = true
            }

            var rad: Float = 0f
            val position = codeBackground.contentBounds.let {
                PointF(it.centerX(), it.centerY())
            }
            exampleDrawOperation = { canvas ->
                canvas.drawCircle(position.x, position.y, rad, paint)
            }
            runAnimation { t ->
                rad = t * codeBackground.contentBounds.width() / 2
            }
        },
        {
            val bmp = BitmapFactory.decodeResource(view.context.resources, R.drawable.android)
            val w = dp(150)
            val scale = w / bmp.width
            val paint = Paint().apply {
                color = White
                shader = ComposeShader(
                    LinearGradient(0f, 0f, 0f, 0f, intArrayOf(Color.BLACK, Color.BLACK), floatArrayOf(0f, 1f), Shader.TileMode.CLAMP),
                    BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
                        .also {
                            it.setLocalMatrix(Matrix().apply {
                                preScale(scale, scale)
                                preTranslate(dp(75), dp(250))
                            })
                        },
                    PorterDuff.Mode.SRC_ATOP
                )
                isAntiAlias = true
            }

            val rect = codeBackground.contentBounds.let {
                val r = RectF(0f, 0f, dp(150), dp(150))
                r.offsetTo(it.centerX() - r.width() / 2, it.centerY() - r.height() / 2)
                r
            }
            exampleDrawOperation = { canvas ->
                canvas.drawRoundRect(rect, dp(20), dp(20), paint)
            }
            runAnimation { t ->
                paint.alpha = (t * 255).toInt()
            }
        },
        {
            val bmp = BitmapFactory.decodeResource(view.context.resources, R.drawable.android)
            val w = dp(20)
            val scale = w / bmp.width

            val example = MultiLineText("And\nroid",
                Paint().apply {
                    typeface = UbuntuBold
                    textSize = dp(80)

                    shader = ComposeShader(
                        LinearGradient(0f, 0f, 0f, 0f, intArrayOf(Color.BLACK, Color.BLACK), floatArrayOf(0f, 1f), Shader.TileMode.CLAMP),
                        BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
                            .also {
                                it.setLocalMatrix(Matrix().apply {
                                    preScale(scale, scale)
                                })
                            },
                        PorterDuff.Mode.SRC_ATOP
                    )

                    isAntiAlias = true
                }
            )
            example.layoutText(Int.MAX_VALUE)
            example.position.x = codeBackground.contentBounds.centerX() - example.width / 2
            example.position.y = codeBackground.contentBounds.centerY() - example.height / 2
            exampleDrawOperation = { canvas ->
                example.draw(canvas)
            }
        },
        {
            val bmp = BitmapFactory.decodeResource(view.context.resources, R.drawable.android)
            val w = dp(50)
            val scale = w / bmp.width

            val arrow = Arrow(
                Paint().apply {
                    typeface = UbuntuBold
                    shader = ComposeShader(
                        LinearGradient(0f, 0f, 0f, 0f, intArrayOf(Color.BLACK, Color.BLACK), floatArrayOf(0f, 1f), Shader.TileMode.CLAMP),
                        BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
                            .also {
                                it.setLocalMatrix(
                                    Matrix().apply {
                                        preScale(scale, scale)
                                    }
                                )
                            },
                        PorterDuff.Mode.SRC_ATOP
                    )
                    isAntiAlias = true
                }
            )
            arrow.layout(dp(150), dp(150))
            arrow.position.x = codeBackground.contentBounds.centerX() - arrow.width / 2
            arrow.position.y = codeBackground.contentBounds.centerY() - arrow.height / 2
            exampleDrawOperation = { canvas ->
                arrow.draw(canvas)
            }
        },
        {
            exampleDrawOperation = null
            bullets.showNext()
        },
        {
            code = CodeSnippet("""

SweepGradient shader =
    new SweepGradient(
        centerX, centerY,
        new Int[] { 0, Color.RED },
        new Float[] { 0f, 1f }
    );

Paint paint = new Paint();
paint.setShader(shader);

canvas.drawCircle(0f, 0f,
    radius, paint);
            """)
        },
        {
            code = null

            val center = codeBackground.contentBounds.let {
                PointF(it.centerX(), it.centerY())
            }

            val paint = Paint().apply {
                color = White
                shader = SweepGradient(
                    center.x, center.y,
                    intArrayOf(Color.TRANSPARENT, Color.RED),
                    floatArrayOf(0f, 0f)
                )
                isAntiAlias = true
            }

            var rad: Float = 0f
            exampleDrawOperation = { canvas ->
                canvas.drawCircle(center.x, center.y, rad, paint)
            }
            runAnimation(duration = 1000) { t ->
                rad = t * codeBackground.contentBounds.width() / 2
                paint.shader = SweepGradient(
                    center.x, center.y,
                    intArrayOf(Color.TRANSPARENT, Color.RED),
                    floatArrayOf(0f, t * 0.99f)
                )
            }
        },
        {
            bullets.showNext()
        },
        {

            val paint = Paint().apply {
                colorFilter = PorterDuffColorFilter(
                    AndroidGreen, PorterDuff.Mode.SRC_IN
                )
                isAntiAlias = true
            }
            val bitmap = BitmapFactory.decodeResource(view.context.resources, R.drawable.android)
            val w = dp(150)
            val h = w * bitmap.height / bitmap.width
            val scale = w / bitmap.width

            val matrix = codeBackground.contentBounds.let {
                Matrix().apply {
                    preTranslate(it.centerX() - w / 2, it.centerY() - h / 2)
                    preScale(scale, scale)
                }
            }
            exampleDrawOperation = { canvas ->
                canvas.drawBitmap(bitmap, matrix, paint)
            }
            runAnimation(duration = 1000) { t ->
                paint.colorFilter = PorterDuffColorFilter(
                    mixColor(AndroidGreen, Color.RED, t), PorterDuff.Mode.SRC_IN
                )
            }
        },
        {
            runAnimation(duration = 1000) { t ->
                matrix.setSaturation(1f - t)
                paint.colorFilter =
                    ColorMatrixColorFilter(
                        matrix
                    )
            }
        },
        {
            view.slideIn(view.slideIndex + 1, SlideFrom.Left)
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

    val matrix = ColorMatrix()
    private val paint =
        Paint().apply {
            colorFilter =
                ColorMatrixColorFilter(
                    matrix
                )
        }
    override fun onDraw(canvas: Canvas) {
        canvas.saveLayer(0f, 0f, view.width.toFloat(), view.height.toFloat(), paint, Canvas.ALL_SAVE_FLAG)

        canvas.drawBackground(view.width, view.height, Shade2)

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

