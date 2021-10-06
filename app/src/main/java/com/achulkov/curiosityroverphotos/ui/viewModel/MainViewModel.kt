package com.achulkov.curiosityroverphotos.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.achulkov.curiosityroverphotos.data.RPhotosDataRepo
import com.achulkov.curiosityroverphotos.data.models.RoomRoverInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
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

    init {
        getRoverManifest()
    }

    private fun getRoverManifest(){
        dataRepo.getRoverManifest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ roverInfo ->
                roverManifestInfo.postValue(roverInfo)
            })
            { throwable ->
                Timber.e(throwable.localizedMessage)
            }

    }


    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}