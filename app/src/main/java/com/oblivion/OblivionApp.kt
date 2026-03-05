package com.oblivion

import android.app.Application
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

class OblivionApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Install the Crash Catcher
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val sw = StringWriter()
            throwable.printStackTrace(PrintWriter(sw))
            val crashReport = sw.toString()
            
            // Save to a file in internal storage
            val file = File(filesDir, "crash_report.txt")
            file.writeText(crashReport)
            
            // Exit the app
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }
}