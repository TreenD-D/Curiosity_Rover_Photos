package com.achulkov.curiosityroverphotos.data

import com.achulkov.curiosityroverphotos.BuildConfig
import com.achulkov.curiosityroverphotos.data.db.RoverDatabase
import com.achulkov.curiosityroverphotos.data.models.RoomRoverInfo
import com.achulkov.curiosityroverphotos.data.models.RoverCameraType
import com.achulkov.curiosityroverphotos.data.models.RoverManifest
import com.achulkov.curiosityroverphotos.data.models.RoverPhoto
import com.achulkov.curiosityroverphotos.data.remote.NasaApi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
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
            .map { manifest ->
                RoomRoverInfo(manifest.name, manifest.landing_date, manifest.launch_date, manifest.status, manifest.max_sol, manifest.max_date, manifest.total_photos) }
            .doOnNext{roverInfo -> db.roverInfo().insert(roverInfo) }
            .switchMap { db.roverInfo().fetchSingleNamed("Curiosity") }
            .switchIfEmpty{ db.roverInfo().fetchSingleNamed("Curiosity") }
            .startWith(db.roverInfo().fetchSingleNamed("Curiosity"))
            .subscribeOn(Schedulers.io())
    }

    override fun getPhotos(sol: Int, camtype: RoverCameraType, page: Int): Observable<List<RoverPhoto>>{
        return api.getPhotos(sol, camtype, page)
    }

}