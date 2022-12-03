package com.example.semestralmobv.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends")
data class FriendItem(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "pub_id") val pubId: String?,
    @ColumnInfo(name = "pub_name") val pubName: String?,
) {
    override fun toString(): String {
        return "FriendItem(localId=$localId ,userId='$userId', userName='$userName', pubId='$pubId', pubName=$pubName)"
    }
}