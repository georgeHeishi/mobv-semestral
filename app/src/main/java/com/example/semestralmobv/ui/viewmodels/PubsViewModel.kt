package com.example.semestralmobv.ui.viewmodels

import androidx.lifecycle.*
import com.example.semestralmobv.data.DataRepository
import com.example.semestralmobv.data.db.models.PubItem
import kotlinx.coroutines.launch

enum class SortBy {
    DEFAULT, NAME
}

class PubsViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _sortBy = MutableLiveData(SortBy.DEFAULT)
    val sortBy: LiveData<SortBy> get() = _sortBy

    var isAsc = false

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    val loading = MutableLiveData(false)

    private val resolvePubs = { _: String ->
        loading.postValue(false)
    }

    private val onError = { errorMessage: String ->
        loading.postValue(false)
        _message.postValue(errorMessage)
    }

    private val _pubs = MutableLiveData<List<PubItem>?>()
    val pubs: LiveData<List<PubItem>?> get() = _pubs

    init {
        viewModelScope.launch {
            val pubsFromDb = dataRepository.getAllPubsFromDb(isAsc, SortBy.DEFAULT)
            if (pubsFromDb == null || pubsFromDb.isEmpty()) {
                refreshPubsFromRepository()
            } else {
                _pubs.postValue(pubsFromDb)
            }
        }
    }

    fun refreshPubsFromRepository() {
        viewModelScope.launch {
            loading.postValue(true)
            dataRepository.refreshPubs(resolvePubs, onError)
            val pubsFromDb = dataRepository.getAllPubsFromDb(isAsc, sortBy.value)
            _pubs.postValue(pubsFromDb)
        }
    }


    fun setSortBy(newSortBy: SortBy) {
        isAsc = if (_sortBy.value == newSortBy) {
            !isAsc
        } else {
            false
        }
        _sortBy.postValue(newSortBy)
        viewModelScope.launch {
            val pubsFromDb = dataRepository.getAllPubsFromDb(isAsc, newSortBy)
            _pubs.postValue(pubsFromDb)
        }
    }

}