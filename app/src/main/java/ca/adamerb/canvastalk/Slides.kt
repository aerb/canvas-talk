package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import java.lang.Math.*

/*
Talk overview:

Canvas API

---

This talk has come out of frustrations I've had with the Android Community
overlooking the canvas Api.

If you go on Stack overflow and lookup drawing a rounded rectangle, or a dividing
line you often get pointed to xml.

This talk is meant to demonstrate the uses of the Canvas API in Android.

---

Talk format.
- These slides are created using only the Android Canvas Api.
- One Activity, One Overriden View, Zero Fragments, Zero Drawables.
- No libraries with the exception of the Kotlin StdLib.
- Download the app @ _________

---

What? The Android Graphics Api

Android Layouts & Resource XML ->
Views (LinearLayout / ImageView / TextView) ->
Canvas API ->
OpenGL Display List, Software Drawing Commands

---

The Basics:

- A canvas object is most often attained by overriding the onDraw function of a view.
- Canvas object has series of draw*() methods for drawing primitive shapes.
    - drawLine, drawCircle, drawRoundedRect
- All draw*() methods take arguments that describe draw coordinates, and an instance of a paint
 object.
- Paint describes color, path effect, color-filters.
    - For more complex shapes you can use drawPath, drawText, drawBitmap







---

- Simple.
- No confusing layout logic.
- Fast.
- Flexible.
- Incredibly Stable.
- Backward compatible.

---


*/

interface Slide {
    fun onLayout(width: Int, height: Int)
    fun onDraw(canvas: Canvas)
    fun nextPressed()
}

class Slide0(private val view: SlideHolderView): Slide {

    private val paint =
        Paint().apply {
            textSize = dp(40)
            typeface = UbuntuBold
            color = White
            isAntiAlias = true
        }

    private var progress: Float = 0f
    private var circleRad = 0
    private val credsText =
        MultiLineText(
            text = "Adam Erb\n@erbal\nadam.l.erb@gmail.com",
            align = Align.Right
        )

    override fun onLayout(width: Int, height: Int) {
        circleRad = ceil(sqrt((width * width + height * height).toDouble())).toInt()

        credsText.layoutText(width - Padding * 2)
        credsText.position.set(width - PaddingF - credsText.width, height - PaddingF - credsText.height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Shade0)

        paint.color = White
        val text = "The Android Canvas"
        paint.getTextBounds(text, 0, text.length, RectBuffer)
        canvas.drawText(text, view.width / 2f - RectBuffer.width() / 2, view.height / 2f, paint)

        credsText.draw(canvas)

        paint.color = Shade2
        canvas.drawCircle(view.width.toFloat(), view.height.toFloat(), circleRad * progress, paint)
    }

    override fun nextPressed() {
        runAnimation(
            onUpdate = { t ->
                progress = t
                view.invalidate()
            },
            onEnd = {
                view.slide = view.slides[++view.slideIndex]()
            }
        )
    }
}

class Slide1(private val view: SlideHolderView): Slide {

    val header = Header(view, "Format")

    val bullets = Bullets(
        view = view,
        bullets = listOf(
            "These slides are created using only the Android Canvas Api.",
            "One Activity, One Overriden view, Zero Fragments.",
            "No libraries with the exception of the Kotlin StdLib.",
            "Download the app @ _________"
        )
    )

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
        bullets.layout(width - Padding * 2)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Shade2)

        canvas.saveLayerAlpha(0f, 0f, view.width.toFloat(), view.height.toFloat(), alpha, Canvas.ALL_SAVE_FLAG)

        header.draw(canvas)
        bullets.draw(canvas)

        canvas.restore()
    }

    override fun nextPressed() {
        if(!bullets.showNext()) {
            view.slideIn(view.slideIndex + 1, SlideFrom.Right)
        }
    }
}

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

        arrows.forEach { it.onDraw(canvas) }

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
            text = when(items.size) {
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


class Slide3(private val view: SlideHolderView): Slide {

    private val paint =
        Paint().apply {
            textSize = dp(20)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        }

    val header = Header(view, "The Basics")
    val bullets = Bullets(
        view = view,
        paint = Paint().apply {
            textSize = dp(14)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        },
        bullets = listOf(
            "A Canvas object is most often attained by overriding the onDraw function of a view.",
            "Canvas object has series of draw*() methods for drawing primitive shapes. drawLine, drawCircle, drawRoundedRect",
            "All draw*() methods take arguments that describe draw coordinates, and an instance of a paint object.",
            "Paint describes color, path effect, color-filters."
        )
    ).also {
        it.showNextCallback = { index ->
            when(index) {
                0 -> {
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
                    runAnimation { t ->
                        codeBackground.bottom = view.height * t
                        view.invalidate()
                    }
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
    val codeBackground = RectF()

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
        bullets.layout(width / 2 - Padding - dp(5).toInt())

        codeSnippet?.let {
            it.layout(width)
            it.scale = (width / 2 - PaddingF- dp(5)) / it.width
            it.position.x = width - PaddingF - it.scaledWidth
            it.position.y = header.lineY + PaddingF
        }
        codeBackground.set(width / 2f, 0f, width.toFloat(), codeBackground.bottom)
    }

    override fun onDraw(canvas: Canvas) {
        RectBuffer.set(0, 0, view.width, view.height)
        paint.color = Shade4
        canvas.drawRect(RectBuffer, paint)

        canvas.saveLayerAlpha(0f, 0f, view.width.toFloat(), view.height.toFloat(), alpha, Canvas.ALL_SAVE_FLAG)
        if(codeBackground.height() > 0) {
            paint.color = CodeBackground
            canvas.drawRect(codeBackground, paint)
        }

        header.draw(canvas)
        bullets.draw(canvas)

        codeSnippet?.draw(canvas)
        canvas.restore()
    }

    override fun nextPressed() {
        if(!bullets.showNext()) {
            view.slideIn(view.slideIndex + 1, SlideFrom.Top)
        }
    }
}

