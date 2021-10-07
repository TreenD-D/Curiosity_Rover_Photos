package com.achulkov.curiosityroverphotos.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.achulkov.curiosityroverphotos.data.models.RoomDeletedPhoto
import io.reactivex.rxjava3.core.Observable

@Dao
abstract class DeletedPhotosDAO {
    @Query("SELECT id FROM RoomDeletedPhoto")
    abstract fun fetchAllIds() : Observable<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(photos: RoomDeletedPhoto)

}