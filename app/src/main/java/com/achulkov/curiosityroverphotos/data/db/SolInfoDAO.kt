package com.achulkov.curiosityroverphotos.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.achulkov.curiosityroverphotos.data.models.RoomSolInfo
import io.reactivex.rxjava3.core.Observable

@Dao
abstract class SolInfoDAO {
    @Query("SELECT * FROM RoomSolInfo ORDER BY sol DESC")
    abstract fun fetchAll() : Observable<List<RoomSolInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(solsInfo: List<RoomSolInfo>)

    @Query("SELECT * FROM RoomSolInfo WHERE sol=:sol")
    abstract fun fetchSingleSol(sol: Int) : Observable<RoomSolInfo>
}