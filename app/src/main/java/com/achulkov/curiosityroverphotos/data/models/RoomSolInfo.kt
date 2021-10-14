package com.achulkov.curiosityroverphotos.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomSolInfo(
    @PrimaryKey val sol: Int,
    val photosCount: Int
)

