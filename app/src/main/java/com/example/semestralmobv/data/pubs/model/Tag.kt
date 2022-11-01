package com.example.semestralmobv.data.pubs.model

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("name") val name: String,
    @SerializedName("amenity") val amenity: String,
    @SerializedName("website") val website: String,
    @SerializedName("opening_hours") val opening_hours: String,
    @SerializedName("phone") val phone: String,
) {
}