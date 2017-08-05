package ca.adamerb.canvastalk

import android.graphics.*

class CodeSnippet(snippet: String) {

    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null

    val text = MultiLineText(
        text = snippet.trimIndent(),
        paint = Paint().apply {
            textSize = dp(15)
            color = White
            typeface = SourceCodePro
            isAntiAlias = true
        }
    ).also {
        it.trimLeading = false
    }

    var scale: Float = 1f
    var width: Int = 0
    var height: Int = 0
    val position = PointF()

    val scaledWidth: Int get() = (scale * width).toInt()
    val scaledHeight: Int get() = (scale * height).toInt()

    fun layout(width: Int) {
        text.layoutText(Int.MAX_VALUE)
        val textPadding = 0f
        text.position.set(textPadding, textPadding)
        val targetWidth = (text.width + textPadding * 2).toInt()
        val targetHeight = (text.height + textPadding * 2).toInt()

        val w = bitmap?.width ?: 0
        val h = bitmap?.height ?: 0
        if(targetWidth != w || targetHeight != h) {
            bitmap?.recycle()

            logd("Building new bitmap of size $targetWidth x $targetHeight")
            val bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
//            canvas.drawColor(CodeBackground)
            text.draw(canvas)
            this.canvas = canvas
            this.bitmap = bitmap

            this.width = targetWidth
            this.height = targetHeight
        }
    }

    fun draw(canvas: Canvas) {
        val bitmap = bitmap
        if(bitmap != null) {
            canvas.save()
            canvas.translate(position.x, position.y)
            canvas.scale(scale, scale)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            canvas.restore()
        }
    }

    fun recycle() {
        bitmap?.recycle()
        bitmap = null
    }
}