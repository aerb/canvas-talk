package ca.adamerb.canvastalk

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

class SlideHolderView(c: Context): View(c) {

    var slide: Slide = Slide0(this)
        get() = field
        set(slide) {
            field = slide
            field.onLayout(width, height)
            invalidate()
        }

    var incomingSlide: Slide? = null

    init {
        setOnClickListener { slide.onClick() }
    }

    private var offsetX: Float = 0f
    fun slideIn(slide: Slide) {
        incomingSlide = slide
        slide.onLayout(width, height)
        val w = -width
        runAnimation(
            onUpdate = { t ->
                offsetX = t * w
                invalidate()
            },
            onEnd = {
                this.slide = slide
                this.incomingSlide = null
                this.offsetX = 0f
                invalidate()
            }
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        slide.onLayout(width, height)
        incomingSlide?.onLayout(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.translate(offsetX, 0f)
        slide.onDraw(canvas)
        canvas.restore()

        incomingSlide?.let {
            canvas.save()
            canvas.translate(offsetX + width, 0f)
            it.onDraw(canvas)
            canvas.restore()
        }

    }
}

class MainActivity : AppCompatActivity() {

    init {
        logd("New instance $this")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SlideHolderView(this))
    }
}



