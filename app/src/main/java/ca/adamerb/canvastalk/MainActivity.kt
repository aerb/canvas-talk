package ca.adamerb.canvastalk

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class SlideHolderView(c: Context): View(c) {

    var slide: Slide = Slide0(this)
        get() = field
        set(slide) {
            field = slide
            field.onLayout(width, height)
            invalidate()
        }

    init {
        setOnClickListener { slide.onClick() }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        logd("Layouttttt!!!")
        slide.onLayout(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        slide.onDraw(canvas)
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



