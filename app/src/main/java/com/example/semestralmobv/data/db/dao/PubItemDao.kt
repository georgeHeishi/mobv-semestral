package com.example.semestralmobv.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.semestralmobv.data.db.models.PubItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PubItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pub: PubItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(pubs: List<PubItem>)

    @Update
    suspend fun update(pub: PubItem)

    @Delete
    suspend fun delete(pub: PubItem)

    @Query("DELETE FROM pubs")
    suspend fun deleteAll()

    @Query("SELECT * FROM pubs WHERE localId = :localId")
    fun getByLocalId(localId: Int): Flow<List<PubItem>>

    @Query("SELECT * FROM pubs WHERE id = :id")
    fun getById(id: String): Flow<List<PubItem>?>

    @Query("SELECT * FROM pubs ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN users END ASC," +
            "CASE WHEN :isAsc = 0 THEN users END DESC" )
    suspend fun getAll(isAsc: Boolean): List<PubItem>?

    @Query("SELECT * FROM pubs ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN  name END ASC," +
            "CASE WHEN :isAsc = 0 THEN name END DESC")
    suspend fun getAllOrderName(isAsc: Boolean): List<PubItem>?
}