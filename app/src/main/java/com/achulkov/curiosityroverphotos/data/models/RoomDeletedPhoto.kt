package com.achulkov.curiosityroverphotos.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomDeletedPhoto(
    @PrimaryKey val id: Int,
    val sol: Int?,
    val img_src: String?
)
