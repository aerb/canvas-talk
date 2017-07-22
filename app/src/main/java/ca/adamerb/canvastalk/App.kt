package ca.adamerb.canvastalk

import android.app.Application
import android.content.Context

private var _appContext: Context? = null
class App: Application() {
    init { _appContext = this }
}
val AppContext: Context get() = _appContext ?:
    throw IllegalStateException("ApplicationContext has not been initialized.")
