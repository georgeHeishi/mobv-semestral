package com.example.semestralmobv.ui.viewmodels

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

    private val _isCheckedIn = MutableLiveData(false)
    val isCheckedIn: LiveData<Boolean>
        get() = _isCheckedIn

    val deviceLocation = MutableLiveData<LatLongLocation>(null)

    private val _checkedInPub = MutableLiveData<NearbyPub?>()

    val checkedInPub: LiveData<NearbyPub?>
        get() = _checkedInPub

    private val _selectedId = MutableLiveData<String?>()

    val selectedId: LiveData<String?>
        get() = _selectedId

    val nearbyPubs: LiveData<List<NearbyPub>> = deviceLocation.switchMap { location ->
        liveData {
            loading.postValue(true)
            location?.let {
                val nearPubs = dataRepository.nearPubs(location, onError)
                _selectedId.postValue(nearPubs[0].id)
                emit(nearPubs)
            } ?: emit(listOf())
            loading.postValue(false)
        }
    }


    private val onCheckInResolve = { checkedIntoPub: NearbyPub ->
        _checkedInPub.postValue(checkedIntoPub)
        _isCheckedIn.postValue(true)
    }


    fun checkIntoPub(pub: NearbyPub) {

        viewModelScope.launch {
            delay(500)
            dataRepository.checkInPub(pub, onCheckInResolve, onError)
        }
    }

    fun setMessage(message: String) {
        _message.postValue(message)
    }

}