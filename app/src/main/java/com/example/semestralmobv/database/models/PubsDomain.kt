package com.example.semestralmobv.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pub")
data class PubsDomain(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "localId")
    val localId: Int = 0,
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "lat")
    val lat: Double,
    @ColumnInfo(name = "lon")
    val lon: Double,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "amenity")
    val amenity: String?,
    @ColumnInfo(name = "website")
    val website: String?,
    @ColumnInfo(name = "opening_hours")
    val openingHours: String?,
    @ColumnInfo(name = "phone")
    val phone: String?,
)