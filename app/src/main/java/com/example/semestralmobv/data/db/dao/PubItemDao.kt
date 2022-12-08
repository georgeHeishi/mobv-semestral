package com.example.semestralmobv.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.semestralmobv.data.db.models.PubItem

@Dao
interface PubItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(pubs: List<PubItem>)

    @Query("DELETE FROM pubs")
    suspend fun deleteAll()

    @Query("SELECT * FROM pubs WHERE id=:id")
    suspend fun getById(id: String): PubItem?

    @Query(
        "SELECT * FROM pubs ORDER BY " + "CASE WHEN :isAsc = 1 THEN users END ASC," + "CASE WHEN :isAsc = 0 THEN users END DESC"
    )
    suspend fun getAll(isAsc: Boolean): List<PubItem>?

    @Query(
        "SELECT * FROM pubs ORDER BY " + "CASE WHEN :isAsc = 1 THEN  name END ASC," + "CASE WHEN :isAsc = 0 THEN name END DESC"
    )
    suspend fun getAllOrderName(isAsc: Boolean): List<PubItem>?
}