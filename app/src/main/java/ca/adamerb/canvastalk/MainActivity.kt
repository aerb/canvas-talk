package ca.adamerb.canvastalk

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

enum class SlideFrom {
    Left, Top, Right, Bottom
}

class SlideHolderView(c: Context): View(c) {

    val slides =
        listOf(
//            { Slide0(this) },
//            { Slide1(this) },
//            { Slide2(this) },
//            { Slide3(this) },
//            { Slide4(this) },
//            { Slide5(this) },
            { Slide6(this) },
            { Slide7(this) },
            { Slide8(this) }
        )

    var slideIndex = 0
        set(value) {
            field = value.coerceIn(0, slides.size - 1)
        }

    var slide: Slide = slides[slideIndex]()
        get() = field
        set(slide) {
            field = slide
            field.onLayout(width, height)
            invalidate()
        }

    var incomingSlide: Slide? = null

    init {
        setOnClickListener {
            if(runningAnimations == 0) {
                slide.nextPressed()
            }
        }
    }

    private var offset = PointF()
    private var incomingOffset = PointF()
    fun slideIn(index: Int, direction: SlideFrom) {
        val incomingSlide = slides[index.coerceIn(0, slides.size - 1)]()
            .also { it.onLayout(width, height) }
        this.incomingSlide = incomingSlide
        val w = width
        val h = height
        runAnimation(
            onUpdate = { t ->
                when(direction) {
                    SlideFrom.Right -> {
                        offset.x = t * -w
                        incomingOffset.x = offset.x + w
                    }
                    SlideFrom.Left -> {
                        offset.x = t * w
                        incomingOffset.x = offset.x - w
                    }
                    SlideFrom.Top -> {
                        offset.y = t * h
                        incomingOffset.y = offset.y - h
                    }
                    SlideFrom.Bottom -> {
                        offset.y = t * -h
                        incomingOffset.y = offset.y + h
                    }
                }
                invalidate()
            },
            onEnd = {
                this.slide = incomingSlide
                this.slideIndex = index
                this.incomingSlide = null
                this.offset.set(0f, 0f)
                this.incomingOffset.set(0f, 0f)
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
        canvas.translate(offset.x, offset.y)
        slide.onDraw(canvas)
        canvas.restore()

        incomingSlide?.let {
            canvas.save()
            canvas.translate(incomingOffset.x, incomingOffset.y)
            it.onDraw(canvas)
            canvas.restore()
        }

    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SlideHolderView(this))
    }
}



