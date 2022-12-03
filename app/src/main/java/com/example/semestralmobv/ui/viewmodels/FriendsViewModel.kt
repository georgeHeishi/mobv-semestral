package com.example.semestralmobv.ui.viewmodels

import androidx.lifecycle.*
import com.example.semestralmobv.data.DataRepository
import com.example.semestralmobv.data.db.models.FriendItem
import kotlinx.coroutines.launch

class FriendsViewModel(private val dataRepository: DataRepository) : ViewModel() {
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

    val friends: LiveData<List<FriendItem>?> = liveData {
        loading.postValue(true)
        dataRepository.refreshFriends(resolvePubs, onError)
        emit(dataRepository.getAllFriendsFromDb())
    }

    fun refreshData() {
        viewModelScope.launch {
            loading.postValue(true)
            dataRepository.refreshFriends(resolvePubs, onError)
            loading.postValue(false)
        }
    }
}