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
    fun onClick()
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

        credsText.onDraw(canvas)

        paint.color = Shade2
        canvas.drawCircle(view.width.toFloat(), view.height.toFloat(), circleRad * progress, paint)
    }

    override fun onClick() {
        runAnimation(
            onUpdate = { t ->
                progress = t
                view.invalidate()
            },
            onEnd = {
                view.slide = Slide1(view)
            }
        )
    }
}

class Slide1(private val view: SlideHolderView): Slide {

    private val paint =
        Paint().apply {
            isAntiAlias = true
            strokeWidth = StrokeWidth
            style = Paint.Style.STROKE
            color = White
        }

    val title = MultiLineText(
        text = "Header",
        paint = Paint().apply {
            textSize = dp(25)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        }
    )

    val bullets = Bullets(
        bullets = listOf(
            "These slides are created using only the Android Canvas Api.",
            "One Activity, One Overriden view, Zero Fragments.",
            "No libraries with the exception of the Kotlin StdLib.",
            "Download the app @ _________"
        )
    )

    private var lineStart: Float = 0f
    private var lineWidth: Float = 0f
    private var lineY: Float = 0f
    private var lineAnimation: Float = 0f

    init {
        runAnimation(duration = 1000) { t ->
            lineAnimation = t
            paint.alpha = (t * 255).toInt()
            view.invalidate()
        }
    }

    override fun onLayout(width: Int, height: Int) {
        lineStart = PaddingF
        lineWidth = width - PaddingF * 2

        val layoutWidth = width - Padding * 2
        title.position.set(PaddingF, PaddingF)
        title.layoutText(layoutWidth)

        lineY = title.position.y + title.height

        bullets.position.set(PaddingF, title.position.y + title.height + dp(15))
        bullets.layout(layoutWidth)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Shade2)

        val count = canvas.saveLayer(0f, 0f, view.width.toFloat(), view.height.toFloat(), paint, Canvas.ALL_SAVE_FLAG)

        canvas.drawLine(lineStart, lineY, lineStart + (lineWidth) * lineAnimation, lineY, paint)

        title.onDraw(canvas)
        bullets.onDraw(canvas)

        canvas.restoreToCount(count)
    }

    override fun onClick() {
        view.slideIn(Slide2(view))
    }
}

class Slide2(private val view: SlideHolderView): Slide {

    private val paint =
        Paint().apply {
            color = White
            isAntiAlias = true
            strokeWidth = StrokeWidth
            style = Paint.Style.STROKE
        }

    val title = MultiLineText(
        text = "What? The Android Graphics Api",
        paint = Paint().apply {
            textSize = dp(25)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        }
    )

    private var lineStart: Float = 0f
    private var lineWidth: Float = 0f
    private var lineY: Float = 0f
    private var lineAnimation: Float = 0f
    private val rects = List(4) { RectF() }

    init {
        runAnimation(duration = 1000) { t ->
            lineAnimation = t
            paint.alpha = (t * 255).toInt()
            view.invalidate()
        }
    }

    override fun onLayout(width: Int, height: Int) {
        lineStart = PaddingF
        lineWidth = width - PaddingF * 2

        title.position.set(PaddingF, PaddingF)
        title.layoutText(lineWidth.toInt())

        lineY = title.position.y + title.height

        var y = lineY + Padding
        val boxHeight = (height - PaddingF - y - Spacing * 3) / 4
        items.forEachIndexed { i, value ->
            value.layoutText(width - Padding * 2)
            val rectWidth = value.width + PaddingF * 2
            val x = (width / 2 - rectWidth / 2)
            rects[i].set(x, y, x + rectWidth, y + boxHeight)

            value.position.set(width / 2f - value.width / 2f, rects[i].centerY() - value.height / 2)

            y += boxHeight + Spacing
        }
    }

    override fun onDraw(canvas: Canvas) {
        RectBuffer.set(0, 0, view.width, view.height)

        paint.style = Paint.Style.FILL
        paint.color = Shade3
        canvas.drawRect(RectBuffer, paint)

        canvas.saveLayer(0f, 0f, view.width.toFloat(), view.height.toFloat(), paint, Canvas.ALL_SAVE_FLAG)

        paint.style = Paint.Style.STROKE
        paint.color = White
        canvas.drawLine(lineStart, lineY, lineStart + (lineWidth) * lineAnimation, lineY, paint)

        title.onDraw(canvas)

        items.forEachIndexed { i, value ->
            canvas.drawRoundRect(rects[i], CornerF, CornerF, paint)
            value.onDraw(canvas)
        }

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

    override fun onClick() {
        if(items.size > 3) return

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


    private val multiLineText = MultiLineText(text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut a feugiat felis. Integer ac aliquet elit. Vestibulum quis erat fringilla, varius ex eu, cursus erat. Morbi lobortis luctus justo nec euismod. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Cras ullamcorper sapien consectetur libero aliquam, finibus viverra velit finibus. Curabitur venenatis rhoncus massa, quis lobortis nisl placerat vitae. Duis id lacus lacus. In ut sapien et felis auctor condimentum nec at orci. Sed turpis enim, fermentum id lacus ac, commodo blandit eros. Pellentesque et tellus felis. Nam convallis, magna in convallis fringilla, lorem est porta neque, aliquam pharetra sapien ipsum a sem.")

    override fun onLayout(width: Int, height: Int) {
        multiLineText.position.set(dp(10), dp(20))
        multiLineText.layoutText(width / 2)
    }

    override fun onDraw(canvas: Canvas) {
        RectBuffer.set(0, 0, view.width, view.height)
        paint.color = Shade3
        canvas.drawRect(RectBuffer, paint)

        paint.color = White
        val text = "Adam Erb"
        paint.getTextBounds(text, 0, text.length, RectBuffer)
        canvas.drawText(text, 0f, RectBuffer.height().toFloat(), paint)

        multiLineText.onDraw(canvas)
    }

    override fun onClick() {
        view.slide = Slide0(view)
    }
}

