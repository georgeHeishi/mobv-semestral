package com.example.semestralmobv.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.semestralmobv.data.db.models.FriendItem

@Dao
interface FriendItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(friends: List<FriendItem>)

    @Query("DELETE FROM friends")
    suspend fun deleteAll()

    @Query(
        "SELECT * FROM friends ORDER BY user_name ASC"
    )
    suspend fun getAll(): List<FriendItem>?
}