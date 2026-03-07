package com.oblivion

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import net.zetetic.database.sqlcipher.SQLiteDatabase
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

class OblivionApp : MultiDexApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // FIX 1: Install MultiDex before ANY classes are loaded
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        // FIX 2: Load SQLCipher's native C/C++ libraries
        SQLiteDatabase.loadLibs(this)

        // Crash Reporter: Saves to public Android/data/com.oblivion/files/
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