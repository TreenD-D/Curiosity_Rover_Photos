package com.achulkov.curiosityroverphotos.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RoverManifest(
    val name: String,
    val landing_date: String?,
    val launch_date: String?,
    val status: String?,
    val max_sol: Int?,
    val max_date: String?,
    val total_photos: Int?,
    val photos : List<RoverSolPhotosInfo>

)
