package com.achulkov.curiosityroverphotos.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.achulkov.curiosityroverphotos.data.RPhotosDataRepo
import com.achulkov.curiosityroverphotos.data.models.RoomDeletedPhoto
import com.achulkov.curiosityroverphotos.data.models.RoomRoverInfo
import com.achulkov.curiosityroverphotos.data.models.RoverManifest
import com.achulkov.curiosityroverphotos.data.models.RoverPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataRepo : RPhotosDataRepo
): ViewModel() {

    private val disposables : CompositeDisposable = CompositeDisposable()

    val roverManifestInfo : MutableLiveData<RoomRoverInfo> = MutableLiveData()
    val photos : MutableLiveData<List<RoverPhoto>> = MutableLiveData()
    val selectedPhoto : MutableLiveData<RoverPhoto> = MutableLiveData()

    init {
        getInitPhotosList()
        getRoverManifest()
    }

    private fun getRoverManifest(){
        disposables.add(
            Observable.combineLatest(dataRepo.getRoverManifest(),
                dataRepo.getDeletedPhotos(),
                {roverInfo, deletedPhotos ->
                    roverInfo.max_sol?.let { getLastPhotos(it, deletedPhotos) }
                    roverInfo
                })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ roverInfo ->
                roverManifestInfo.postValue(roverInfo)
            })
            { throwable ->
                Timber.e(throwable.localizedMessage)
            })

    }

    fun getInitPhotosList(){
        disposables.add(dataRepo.getDbPhotos().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ photoList ->
                photos.postValue(photoList.take(20))
            })
            { throwable ->
                Timber.e(throwable.localizedMessage)
            })
    }

    fun getLastPhotos(max_sol : Int, deletedPhotos : List<Int>){
        disposables.add(dataRepo.getPhotos(max_sol, deletedPhotos)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ photoList ->
                photos.postValue(photoList.take(20))
            })
            { throwable ->
                Timber.e(throwable.localizedMessage)
            })
    }

    fun deletePhoto(photos: List<RoverPhoto>){
        disposables.add(dataRepo.removePhotos(photos)
            .subscribeOn(Schedulers.io())
            .subscribe())
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}