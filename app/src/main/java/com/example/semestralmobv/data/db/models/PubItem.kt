package com.example.semestralmobv.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pubs")
data class PubItem(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "localId") val localId: Int = 0,
    val id: String,
    val lat: Double,
    val lon: Double,
    @ColumnInfo(name = "name") val name: String?,
    val amenity: String?,
    val website: String?,
    @ColumnInfo(name = "opening_hours") val openingHours: String?,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "users") val users: Int?
) {

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + amenity.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + lon.hashCode()
        result = 31 * result + website.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PubItem) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (amenity != other.amenity) return false
        if (lat != other.lat) return false
        if (lon != other.lon) return false
        if (website != other.website) return false

        return true
    }

    override fun toString(): String {
        return "PubItem(localId=$localId ,id='$id', name='$name', amenity='$amenity', lat=$lat, lon=$lon, website=$website)"
    }
}
