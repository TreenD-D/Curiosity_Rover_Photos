package com.achulkov.curiosityroverphotos.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.achulkov.curiosityroverphotos.data.models.RoomRoverInfo
import com.achulkov.curiosityroverphotos.data.models.RoverPhoto

@Database(entities =
[RoverPhoto::class, RoomRoverInfo::class], version = 1)
abstract class RoverDatabase : RoomDatabase() {

    abstract fun photos() : RoverPhotoDAO

    abstract fun roverInfo() : RoverInfoDAO
}