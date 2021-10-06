package com.achulkov.curiosityroverphotos.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomRoverInfo(
    @PrimaryKey val name: String,
    val landing_date: String?,
    val launch_date: String?,
    val status: String?,
    val max_sol: Int?,
    val max_date: String?,
    val total_photos: Int?
)
