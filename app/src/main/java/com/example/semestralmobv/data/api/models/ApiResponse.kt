package com.example.semestralmobv.data.api.models

import com.example.semestralmobv.data.db.models.FriendItem
import com.example.semestralmobv.data.db.models.PubItem
import com.google.gson.annotations.SerializedName

data class UserResponse(val uid: String, val access: String, val refresh: String)

data class PubListResponse(
    @SerializedName("bar_id") val barId: String,
    @SerializedName("bar_name") val barName: String,
    @SerializedName("bar_type") val barType: String,
    val lat: Double,
    var lon: Double,
    var users: Int
) {
    fun asDatabaseModel(): PubItem {
        return PubItem(
            0, barId, lat, lon, barName, barType, null, null, null, users
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

data class FriendListResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("bar_id") val barId: String?,
    @SerializedName("bar_name") val barName: String?,
    val time: String,
    @SerializedName("bar_lat") val barLat: Double,
    @SerializedName("bar_lon") val barLon: Double,
) {
    fun asDatabaseModel(): FriendItem {
        return FriendItem(
            0, userId, userName, barId, barName
        )
    }
}