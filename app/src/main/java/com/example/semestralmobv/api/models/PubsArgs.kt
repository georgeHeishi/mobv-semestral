package com.example.semestralmobv.api.models

import com.google.gson.annotations.SerializedName


data class PubsArgs(
    @SerializedName("collection") val collection: String,
    @SerializedName("database") val database: String,
    @SerializedName("dataSource") val dataSource: String,
    @SerializedName("filter") val filter: PubsTagsFilter?
)

data class PubsTagsFilter(
    @SerializedName("tags.amenity") val amenity: String,
    @SerializedName("tags.name") val name: String
)