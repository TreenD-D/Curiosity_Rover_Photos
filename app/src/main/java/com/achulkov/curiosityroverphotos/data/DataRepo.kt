package com.achulkov.curiosityroverphotos.data

import com.achulkov.curiosityroverphotos.data.models.RoomRoverInfo
import com.achulkov.curiosityroverphotos.data.models.RoverCameraType
import com.achulkov.curiosityroverphotos.data.models.RoverManifest
import com.achulkov.curiosityroverphotos.data.models.RoverPhoto
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response

interface DataRepo {
    fun getRoverManifest(): Observable<RoomRoverInfo>
    fun getPhotos(
        sol: Int,
        camtype: RoverCameraType,
        page: Int
    ): Observable<List<RoverPhoto>>
}