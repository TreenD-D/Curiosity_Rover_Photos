package com.achulkov.curiosityroverphotos.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.achulkov.curiosityroverphotos.data.RPhotosDataRepo
import com.achulkov.curiosityroverphotos.data.models.RoomRoverInfo
import com.achulkov.curiosityroverphotos.data.models.RoomSolInfo
import com.achulkov.curiosityroverphotos.data.models.RoverPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
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
    val currentSolIndex : MutableLiveData<Int> = MutableLiveData(0)
    val solsWithImagesList : MutableLiveData<List<RoomSolInfo>> = MutableLiveData()
    val isLoading : MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        getInitPhotosList()
        getRoverManifest()
    }

    /**
     * gets rover manifest ang triggers initial photos list query and sols info
     */
    private fun getRoverManifest(){
        disposables.add(
            Observable.combineLatest(dataRepo.getRoverManifest(),
                dataRepo.getDeletedPhotos(),
                {roverInfo, deletedPhotos ->
                    roverInfo.max_sol?.let { getLastSolPhotos(it, deletedPhotos) }
                    getSolsInfo()
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

    private fun getInitPhotosList(){
        disposables.add(dataRepo.getDbPhotos().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ photoList ->
                photos.postValue(photoList)
            })
            { throwable ->
                Timber.e(throwable.localizedMessage)
            })
    }

    private fun getLastSolPhotos(max_sol : Int, deletedPhotos : List<Int>){
        disposables.add(dataRepo.getPhotos(max_sol, deletedPhotos)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ photoList ->
                photos.postValue(photoList)
            })
            { throwable ->
                Timber.e(throwable.localizedMessage)
            })
    }



    fun getSolPhotos(sol : Int){
        disposables.add(dataRepo.getDeletedPhotos()
            .map { deletedPhotos ->
                dataRepo.getPhotos(sol, deletedPhotos)
                    .subscribe({
                        photos.postValue(it)
                        isLoading.postValue(false)
                    })
                    { throwable ->
                        Timber.e(throwable.localizedMessage)
                        isLoading.postValue(false)
                    }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        )
    }

    private fun getSolsInfo() {
        disposables.add(dataRepo.getDBSolsInfoList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                solsWithImagesList.postValue(it)
                solsWithImagesList.value?.get(0)?.sol?.let { it1 -> getSolPhotos(it1) }
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