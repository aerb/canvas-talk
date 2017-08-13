package ca.adamerb.canvastalk

import android.graphics.*

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


---

Working with Paths

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
    val view: SlideHolderView

    fun layoutAndInvalidate() {
        onLayout(view.width, view.height)
        view.invalidate()
    }
}


