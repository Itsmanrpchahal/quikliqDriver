package com.quikliq.quikliqdriver.app

import android.app.Application
import android.content.ContextWrapper
import android.os.StrictMode
import com.quikliq.quikliqdriver.utilities.Prefs


class quikliqDriver : Application() {

    override fun onCreate() {
        super.onCreate()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        Prefs.Builder()
            .setContext(this@quikliqDriver)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}
