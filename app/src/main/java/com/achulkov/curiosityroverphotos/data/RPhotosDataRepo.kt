package com.achulkov.curiosityroverphotos.data

import com.achulkov.curiosityroverphotos.BuildConfig
import com.achulkov.curiosityroverphotos.data.db.RoverDatabase
import com.achulkov.curiosityroverphotos.data.models.RoverCameraType
import com.achulkov.curiosityroverphotos.data.models.RoverManifest
import com.achulkov.curiosityroverphotos.data.models.RoverPhoto
import com.achulkov.curiosityroverphotos.data.remote.NasaApi
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RPhotosDataRepo @Inject constructor(
    private val api: NasaApi,
    private val db: RoverDatabase
) : DataRepo {

    override fun getRoverManifestRemote(): Observable<Response<RoverManifest>> {
        return api.getCuriosityManifest()
    }

    override fun getPhotos(sol: Int, camtype: RoverCameraType, page: Int): Observable<Response<List<RoverPhoto>>>{
        return api.getPhotos(sol, camtype, page)
    }

}