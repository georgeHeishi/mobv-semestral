package com.example.semestralmobv.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.semestralmobv.data.db.dao.PubItemDao
import com.example.semestralmobv.data.db.models.PubItem

// Whenever you change the schema of the database table, you'll have to increase the version number.
@Database(entities = [PubItem::class], version = 3, exportSchema = false)
abstract class PubsLocalDatabase : RoomDatabase() {
    abstract fun pubItemDao(): PubItemDao

    companion object {
        @Volatile
        private var INSTANCE: PubsLocalDatabase? = null

        fun getInstance(context: Context): PubsLocalDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PubsLocalDatabase::class.java,
                    "pubsDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}