package com.achulkov.curiosityroverphotos.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.achulkov.curiosityroverphotos.data.models.RoomRoverInfo
import io.reactivex.rxjava3.core.Observable

@Dao
abstract class RoverInfoDAO {
    @Query("SELECT * FROM RoomRoverInfo")
    abstract fun fetchAll() : Observable<List<RoomRoverInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(roverInfo: RoomRoverInfo)
}