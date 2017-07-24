package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint
import java.lang.Math.*

/*
Talk overview:

Canvas API

---

Talk format.
- These slides are created using only the Android Canvas Api.
- One Activity, One Overriden view, Zero Fragments.
- No libraries with the exception of the Kotlin StdLib.
- Download the app @ _________

---

What? The Android Graphics Pipeline
-


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
        credsText.position.set(PaddingF, height - PaddingF - credsText.height)
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
            style = Paint.Style.STROKE
            strokeWidth = dp(3)
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

        lineY = title.position.y + title.height + dp(1)

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

