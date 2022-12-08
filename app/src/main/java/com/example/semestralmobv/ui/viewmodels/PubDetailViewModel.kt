package com.example.semestralmobv.ui.viewmodels

import androidx.lifecycle.*
import com.example.semestralmobv.data.DataRepository
import com.example.semestralmobv.data.db.models.PubItem
import kotlinx.coroutines.launch

class PubDetailViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    val loading = MutableLiveData(false)


    var id = MutableLiveData<String?>(null)

    private val resolvePubs = { _: String -> }

    private val onError = { errorMessage: String ->
        _message.postValue(errorMessage)
    }

    val pub: LiveData<PubDetail> = id.switchMap {
        liveData {
            loading.postValue(true)
            val foundPub = id.value?.let { dataRepository.pubDetail(it, resolvePubs, onError) }
            val pubFromDb = id.value?.let { dataRepository.getPubById(it) }
            foundPub?.let {
                emit(PubDetail(
                    foundPub.id,
                    foundPub.name,
                    foundPub.type,
                    foundPub.lat,
                    foundPub.long,
                    foundPub.tags,
                    foundPub.distance
                ).apply {
                    pubFromDb?.let {
                        this.users = it.users
                    }
                })
            }
            loading.postValue(false)
        }
    }

    fun refresh(localId: String) {
        viewModelScope.launch {
            loading.postValue(true)
            id.postValue(localId)
            dataRepository.refreshPubs(resolvePubs, onError)
            loading.postValue(false)
        }
    }
}