package com.achulkov.curiosityroverphotos.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RoverPhotosList(
    val photos : List<RoverPhoto>
)
