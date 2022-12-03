package com.example.semestralmobv.ui.viewmodels

import android.location.Location
import com.example.semestralmobv.data.api.models.Tag
import com.example.semestralmobv.utils.LatLongLocation

open class NearbyPub(
    val id: String,
    val name: String? = "Missing name",
    val type: String? = "",
    val lat: Double,
    val long: Double,
    val tags: Tag?,
    var distance: Double = 0.0,
    var isPinned: Boolean = false
) {
    fun distanceTo(toLocation: LatLongLocation) {
        this.distance =  Location("").apply {
            latitude = lat
            longitude = long
        }.distanceTo(Location("").apply {
            latitude = toLocation.lat
            longitude = toLocation.long
        }).toDouble()
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + long.hashCode()
//        result = 31 * result + distance.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NearbyPub) return false

        if (type != other.type) return false
        if (id != other.id) return false
        if (lat != other.lat) return false
        if (long != other.long) return false
        if (tags != other.tags) return false
//        if (distance != other.distance) return false

        return true
    }

    override fun toString(): String {
        return "NearbyBar(type='$type', id=$id, lat=$lat, lon=$long, tags=$tags, distance=$distance)"
    }
}