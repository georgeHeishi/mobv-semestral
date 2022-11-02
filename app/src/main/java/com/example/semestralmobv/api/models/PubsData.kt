package com.example.semestralmobv.api.models

import com.google.gson.annotations.SerializedName

data class PubsData(val documents: MutableList<Pub>)

data class Pub(
    @SerializedName("id") val id: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("tags") val tags: Tag?
)

data class Tag(
    @SerializedName("name") val name: String,
    @SerializedName("amenity") val amenity: String,
    @SerializedName("website") val website: String,
    @SerializedName("opening_hours") val openingHours: String,
    @SerializedName("phone") val phone: String,
)