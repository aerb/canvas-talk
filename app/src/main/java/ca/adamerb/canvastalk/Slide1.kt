package ca.adamerb.canvastalk

import android.graphics.Canvas

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