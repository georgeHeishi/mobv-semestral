package com.example.semestralmobv.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semestralmobv.data.DataRepository
import kotlinx.coroutines.launch

class AddFriendViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private val _isSuccessful = MutableLiveData(false)

    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    val loading = MutableLiveData(false)

    private val resolvePubs = { _: String ->
        _isSuccessful.postValue(true)
        loading.postValue(false)
    }

    private val onError = { errorMessage: String ->
        _isSuccessful.postValue(false)
        loading.postValue(false)
        _message.postValue(errorMessage)
    }

    fun addFriend(name: String) {
        viewModelScope.launch {
            loading.postValue(true)
            dataRepository.addFriend(name, resolvePubs, onError)
        }
    }

    fun setMessage(message: String) {
        _message.postValue(message)
    }
}