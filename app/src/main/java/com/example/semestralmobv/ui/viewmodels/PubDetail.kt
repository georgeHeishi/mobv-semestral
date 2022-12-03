package com.example.semestralmobv.ui.viewmodels

import com.example.semestralmobv.data.api.models.Tag

class PubDetail(
    id: String,
    name: String? = "Missing name",
    type: String? = "",
    lat: Double,
    long: Double,
    tags: Tag?,
    distance: Double = 0.0
) : NearbyPub(id, name, type, lat, long, tags, distance) {
    var users: Int? = 0;
}