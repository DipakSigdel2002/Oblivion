package com.oblivion.data

import android.content.Context
import androidx.room.*
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

@Entity(tableName = "messages")
data class MessageEntity(@PrimaryKey(autoGenerate = true) val id: Int = 0, val content: String, val sender: String, val timestamp: Long)

// --- NEW ENTITY ---
@Entity(tableName = "contacts")
data class ContactEntity(@PrimaryKey val onionAddress: String, val name: String)

@Dao
interface MessageDao {
    @Insert fun insert(message: MessageEntity)
    @Query("SELECT * FROM messages") fun getAllMessages(): List<MessageEntity>
}

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun saveContact(contact: ContactEntity)
    @Query("SELECT * FROM contacts") fun getAllContacts(): List<ContactEntity>
}

@Database(entities = [MessageEntity::class, ContactEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun contactDao(): ContactDao

    companion object {
        fun getDatabase(context: Context, pin: String): AppDatabase {
            val factory = SupportOpenHelperFactory(pin.toByteArray())
            return Room.databaseBuilder(context, AppDatabase::class.java, "oblivion.db")
                .openHelperFactory(factory)
                .build()
        }
    }
}