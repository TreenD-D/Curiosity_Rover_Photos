package com.achulkov.curiosityroverphotos.data

import com.achulkov.curiosityroverphotos.data.models.*
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response

interface DataRepo {
    fun getRoverManifest(): Observable<RoomRoverInfo>
    fun getPhotos(
        sol: Int,
        camtype: RoverCameraType,
        page: Int
    ): Observable<RoverPhotosList>

    fun removePhotos(photos: List<RoverPhoto>): Observable<Unit>
    fun getDeletedPhotos(): Observable<List<Int>>
    fun getPhotos(sol: Int, deletedPhotos: List<Int>): Observable<List<RoverPhoto>>
    fun getDbPhotos(): Observable<List<RoverPhoto>>
    fun getDBSolsInfoList(): Observable<List<RoomSolInfo>>
}