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

    val pubs: LiveData<List<PubItem>?> = liveData {
        loading.postValue(true)
        emit(dataRepository.getAllPubsFromDb(false,SortBy.DEFAULT))
        loading.postValue(false)
    }

    private val _pub = MutableLiveData<PubDetail>()
    val pub: LiveData<PubDetail> get() = _pub

    private val resolvePubs = { _: String -> }

    private val onError = { errorMessage: String ->
        _message.postValue(errorMessage)
    }


    fun loadPub(id: String) {
        viewModelScope.launch {
            loading.postValue(true)
            val foundPub = dataRepository.pubDetail(id, resolvePubs, onError)
            val pubFromDb = pubs.value?.find { it.id == id }
            foundPub?.let {
                _pub.postValue(PubDetail(
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

    fun refresh(id: String) {
        viewModelScope.launch {
            loading.postValue(true)
            loadPub(id)
            dataRepository.refreshPubs(resolvePubs, onError)
            loading.postValue(false)
        }
    }
}