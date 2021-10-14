package com.achulkov.curiosityroverphotos.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.achulkov.curiosityroverphotos.data.models.RoomDeletedPhoto
import com.achulkov.curiosityroverphotos.data.models.RoomRoverInfo
import com.achulkov.curiosityroverphotos.data.models.RoomSolInfo
import com.achulkov.curiosityroverphotos.data.models.RoverPhoto

@Database(entities =
[RoverPhoto::class, RoomRoverInfo::class, RoomDeletedPhoto::class, RoomSolInfo::class], version = 1)
abstract class RoverDatabase : RoomDatabase() {

    abstract fun photos() : RoverPhotoDAO

    abstract fun roverInfo() : RoverInfoDAO

    abstract fun deletedPhotos() : DeletedPhotosDAO

    abstract fun solInfo() : SolInfoDAO
}