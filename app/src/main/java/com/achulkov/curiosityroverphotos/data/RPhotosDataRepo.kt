package com.achulkov.curiosityroverphotos.data

import com.achulkov.curiosityroverphotos.BuildConfig
import com.achulkov.curiosityroverphotos.data.db.RoverDatabase
import com.achulkov.curiosityroverphotos.data.models.*
import com.achulkov.curiosityroverphotos.data.remote.NasaApi
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RPhotosDataRepo @Inject constructor(
    private val api: NasaApi,
    private val db: RoverDatabase
) : DataRepo {

    override fun getRoverManifest(): Observable<RoomRoverInfo> {
        return api.getCuriosityManifest()
            .retry(3L)
            .doOnNext { manifest ->
                val solsWithPhotosList = mutableListOf<RoomSolInfo>()
                for(solPhotosInfo in manifest.photo_manifest.photos){
                    solsWithPhotosList.add(RoomSolInfo(solPhotosInfo.sol, solPhotosInfo.total_photos))
                }
                db.solInfo().insert(solsWithPhotosList)
            }
            .map { manifest ->
                RoomRoverInfo(manifest.photo_manifest.name,
                    manifest.photo_manifest.landing_date, manifest.photo_manifest.launch_date,
                    manifest.photo_manifest.status, manifest.photo_manifest.max_sol,
                    manifest.photo_manifest.max_date, manifest.photo_manifest.total_photos) }
            .doOnNext{roverInfo -> db.roverInfo().insert(roverInfo) }
            .switchMap { db.roverInfo().fetchSingleNamed("Curiosity") }
            .switchIfEmpty{ db.roverInfo().fetchSingleNamed("Curiosity") }
            .subscribeOn(Schedulers.io())
    }

    override fun getPhotos(
        sol: Int,
        camtype: RoverCameraType,
        page: Int
    ): Observable<RoverPhotosList> {
        return api.getPhotosWithCamera(sol, camtype, page)
    }

    override fun getDeletedPhotos(): Observable<List<Int>> {
        return db.deletedPhotos().fetchAllIds()
            .subscribeOn(Schedulers.io())
    }

    override fun getPhotos(sol: Int, deletedPhotos: List<Int>): Observable<List<RoverPhoto>>{
        return api.getPhotos(sol)
            .retry(3L)
            .map{
                val photosList = mutableListOf<RoverPhoto>()
                for(photo in it.photos){
                    if(!deletedPhotos.contains(photo.id)) photosList.add(photo)
                }
                photosList
            }
            .doOnNext { photos -> db.photos().insert(photos) }
            .switchMap { db.photos().fetchAll() }
            .switchIfEmpty{ db.photos().fetchAll() }
            .subscribeOn(Schedulers.io())
    }

    override fun getDbPhotos(): Observable<List<RoverPhoto>>{
        return db.photos().fetchAll()
            .switchIfEmpty{ emptyList<RoverPhoto>() }
            .subscribeOn(Schedulers.io())
    }

    override fun removePhotos(photos: List<RoverPhoto>): Observable<Unit> {
        return Observable.fromIterable(photos)
            .map { photo ->
                db.deletedPhotos().insert(RoomDeletedPhoto(photo.id, photo.sol, photo.img_src))
                db.photos().deleteSingleEntry(photo.id)}
            .subscribeOn(Schedulers.io())

    }

    override fun getDBSolsInfoList() : Observable<List<RoomSolInfo>> {
        return db.solInfo().fetchAll()
            .switchIfEmpty{ emptyList<RoomSolInfo>() }
            .subscribeOn(Schedulers.io())
    }


}