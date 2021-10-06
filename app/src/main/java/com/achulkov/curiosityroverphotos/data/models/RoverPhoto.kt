package com.achulkov.curiosityroverphotos.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoverPhoto(
    @PrimaryKey val id: Int?,
    val sol: Int?,
    val img_src: String?,
    val earth_date: String?,
    val isDeleted : Boolean?
)
