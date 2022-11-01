package com.example.semestralmobv.data.pubs.model

import com.google.gson.annotations.SerializedName

data class Pub(
    @SerializedName("id") val id: String,
    @SerializedName("lat") val lat: Float,
    @SerializedName("lon") val lon: Float,
    @SerializedName("tags") val tags: Tag?
) {

}