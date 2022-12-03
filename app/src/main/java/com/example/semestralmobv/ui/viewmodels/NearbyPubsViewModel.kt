package com.example.semestralmobv.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.semestralmobv.data.DataRepository
import com.example.semestralmobv.utils.LatLongLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NearbyPubsViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    val loading = MutableLiveData(false)

    private val onError = { errorMessage: String ->
        loading.postValue(false)
        _message.postValue(errorMessage)
    }


    private val _isCheckedIn = MutableLiveData<Boolean>()
    val isCheckedIn: LiveData<Boolean>
        get() = _isCheckedIn


    val deviceLocation = MutableLiveData<LatLongLocation>(null)

    private val _checkedInPub = MutableLiveData<NearbyPub?>()

    val selectedPub = MutableLiveData<NearbyPub>()

    val nearbyPubs: LiveData<List<NearbyPub>> = deviceLocation.switchMap { location ->
        liveData {
            loading.postValue(true)
            location?.let {
                val nearPubs = dataRepository.nearPubs(location, onError)
                selectedPub.postValue(nearPubs[0])
                emit(nearPubs)
            } ?: emit(listOf())
            loading.postValue(false)
        }
    }


    private val onCheckInResolve = { checkInStatus: Boolean ->
        Log.i("checkInStatus", checkInStatus.toString())
        _checkedInPub.postValue(selectedPub.value)
        _isCheckedIn.postValue(checkInStatus)

    }


    fun checkIntoSelected() {
        _checkedInPub.value.run {
            if (selectedPub.value?.id === _checkedInPub.value?.id) {
                return
            }
        }

        viewModelScope.launch {
            delay(500)
            selectedPub.value?.let {
                dataRepository.checkInPub(it, onCheckInResolve, onError)
            }
        }
    }

    fun setSelectedPub(pub: NearbyPub) {
        selectedPub.postValue(pub)
        if (pub.id == _checkedInPub.value?.id) {
            _isCheckedIn.postValue(true)
        } else {
            _isCheckedIn.postValue(false)

        }
    }

    fun setMessage(message: String) {
        _message.postValue(message)
    }

}