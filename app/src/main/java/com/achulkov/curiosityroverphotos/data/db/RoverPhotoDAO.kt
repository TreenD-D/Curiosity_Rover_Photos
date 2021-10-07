package com.achulkov.curiosityroverphotos.data.db

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.achulkov.curiosityroverphotos.data.models.RoverPhoto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
abstract class RoverPhotoDAO {
    @Query("SELECT * FROM RoverPhoto ORDER BY id DESC")
    abstract fun fetchAll() : Observable<List<RoverPhoto>>

    @Query("SELECT * FROM RoverPhoto WHERE id=:id")
    abstract fun fetchOne(id: Int) : Single<RoverPhoto>

    @WorkerThread
    @Query("SELECT COUNT(id) FROM RoverPhoto")
    abstract fun photosCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(photos: List<RoverPhoto>)

    @Query("DELETE FROM RoverPhoto")
    abstract fun clearTable()

    @Query("DELETE FROM RoverPhoto WHERE id=:id")
    abstract fun deleteSingleEntry(id:Int)
}