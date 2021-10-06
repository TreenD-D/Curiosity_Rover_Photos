package com.achulkov.curiosityroverphotos.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RoverSolPhotosInfo(
    val sol: Int?,
    val earth_date: String?,
    val total_photos: Int?,
    val cameraTypes: List<RoverCameraType>
)
