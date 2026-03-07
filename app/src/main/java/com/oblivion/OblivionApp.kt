package com.oblivion

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

class OblivionApp : MultiDexApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        // THE FIX: Modern SQLCipher uses standard Android native library loading.
        // No imports required!
        System.loadLibrary("sqlcipher")

        // Crash Reporter
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            val sw = StringWriter()
            throwable.printStackTrace(PrintWriter(sw))
            try {
                val file = File(getExternalFilesDir(null), "crash_report.txt")
                file.writeText(sw.toString())
            } catch (_: Exception) {}
            
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }
}