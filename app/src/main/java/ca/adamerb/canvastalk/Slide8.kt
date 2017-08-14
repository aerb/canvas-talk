package ca.adamerb.canvastalk

import android.graphics.Canvas
import android.graphics.Paint

class Slide8(override val view: SlideHolderView): Slide {

    val header = Header(view, "Thanks")

    val bullets = Bullets(
        view = view,
        bullets = listOf(
            "Adam Erb",
            "@erbal",
            "adam.l.erb@gmail.com"
        ),
        paint = Paint().apply {
            textSize = dp(13)
            color = White
            typeface = UbuntuBold
            isAntiAlias = true
        }
    ).also { it.showUpToIndex = 2 }

    init {
        runAnimation { t -> header.lineAnimation = t }
    }

    override fun onLayout(width: Int, height: Int) {
        header.layout(width)

        bullets.position.set(PaddingF, header.lineY + PaddingF)
        bullets.layout(width / 2 - Padding * 2)
    }

    private val alpha: Int = 255
    override fun onDraw(canvas: Canvas) {
        canvas.drawBackground(view.width, view.height, Shade2)

        canvas.saveLayerAlpha(0f, 0f, view.width.toFloat(), view.height.toFloat(), alpha, Canvas.ALL_SAVE_FLAG)

        header.draw(canvas)
        bullets.draw(canvas)


        canvas.restore()
    }

    override fun nextPressed() {

    }
}

