package com.achulkov.curiosityroverphotos.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RoverManifestInfo(
    val photo_manifest: RoverManifest
)
