package com.achulkov.curiosityroverphotos.data.remote



import com.achulkov.curiosityroverphotos.BuildConfig
import com.achulkov.curiosityroverphotos.data.models.RoverCameraType
import com.achulkov.curiosityroverphotos.data.models.RoverManifest
import com.achulkov.curiosityroverphotos.data.models.RoverPhoto
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {

    /**
     * gets rover photos, made on
     * @param martianSol martian sol data
     * with
     * @param cameraType rover cam type
     * @param page page of photo list(25-per-page)
     */
    @GET("rovers/curiosity/photos")
    fun getPhotos(@Query("sol") martianSol : Int,
                  @Query("cam") cameraType: RoverCameraType,
                  @Query("page") page: Int,
                  @Query("api_key") key : String = BuildConfig.API_KEY
    ) : Observable<List<RoverPhoto>>

    /**
     * gets nasa manifst for rover with data about mission
     */
    @GET("manifests/curiosity")
    fun getCuriosityManifest(@Query("api_key") key : String = BuildConfig.API_KEY
        ) : Observable<RoverManifest>
}