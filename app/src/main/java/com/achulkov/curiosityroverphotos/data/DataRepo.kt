package com.achulkov.curiosityroverphotos.data

import com.achulkov.curiosityroverphotos.data.models.RoverCameraType
import com.achulkov.curiosityroverphotos.data.models.RoverManifest
import com.achulkov.curiosityroverphotos.data.models.RoverPhoto
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response

interface DataRepo {
    fun getRoverManifestRemote(): Observable<Response<RoverManifest>>
    fun getPhotos(
        sol: Int,
        camtype: RoverCameraType,
        page: Int
    ): Observable<Response<List<RoverPhoto>>>
}