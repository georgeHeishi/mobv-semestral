package com.example.semestralmobv.data.api.models

import com.google.gson.annotations.SerializedName

data class UserArgs(
    val name: String,
    val password: String,
)

data class UserRefreshArgs(
    val refresh: String
)

data class CheckInPubArgs(
    val id: String, val name: String, val type: String, val lat: Double, val lon: Double
)

data class PubsTagsFilter(
    @SerializedName("tags.amenity") val amenity: String,
    @SerializedName("tags.name") val name: String
)

data class FriendArgs(val contact: String)