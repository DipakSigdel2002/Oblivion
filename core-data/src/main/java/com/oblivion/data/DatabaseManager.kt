package com.oblivion.data

import android.content.Context
import androidx.room.*
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

// Room Database setup
@Database(entities = [], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        fun getDatabase(context: Context, pin: String): AppDatabase {
            val factory = SupportOpenHelperFactory(pin.toByteArray())
            return Room.databaseBuilder(context, AppDatabase::class.java, "oblivion_secure.db")
                .openHelperFactory(factory)
                .build()
        }
    }
}