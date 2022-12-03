package com.example.semestralmobv.data.api.models

import com.example.semestralmobv.data.db.models.PubItem
import com.google.gson.annotations.SerializedName

data class UserResponse(val uid: String, val access: String, val refresh: String)

data class PubListResponse(
    val bar_id: String,
    val bar_name: String,
    val bar_type: String,
    val lat: Double,
    var lon: Double,
    var users: Int
) {
    fun asDatabaseModel(): PubItem {
        return PubItem(
            0,
            bar_id,
            lat,
            lon,
            bar_name,
            bar_type,
            null,
            null,
            null,
            users
        )
    }
}

data class PubsData(val elements: MutableList<Pub>)

data class Pub(
    val type: String, val id: String, val lat: Double, val lon: Double, val tags: Tag?
)

data class Tag(
    val name: String,
    val amenity: String,
    val website: String,
    @SerializedName("opening_hours") val openingHours: String,
    val phone: String,
)