package com.example.semestralmobv.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.semestralmobv.data.db.dao.FriendItemDao
import com.example.semestralmobv.data.db.dao.PubItemDao
import com.example.semestralmobv.data.db.models.FriendItem
import com.example.semestralmobv.data.db.models.PubItem

@Database(entities = [PubItem::class, FriendItem::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun pubItemDao(): PubItemDao
    abstract fun friendItemDao(): FriendItemDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, LocalDatabase::class.java, "mobv_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}