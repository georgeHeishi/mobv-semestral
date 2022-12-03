package com.example.semestralmobv.ui.viewmodels

import androidx.lifecycle.*
import com.example.semestralmobv.data.DataRepository
import com.example.semestralmobv.data.api.models.UserResponse
import kotlinx.coroutines.launch

class AuthorizationViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean?>
        get() = _loading

    private val _user = MutableLiveData<UserResponse>(null)
    val user: LiveData<UserResponse>
        get() = _user

    private val bindUser = { userResponse: UserResponse? -> _user.postValue(userResponse) }
    private val bindMessage = { errorMessage: String -> _message.postValue(errorMessage) }

    fun login(name: String, password: String) {
        viewModelScope.launch {
            _loading.postValue(true)
            dataRepository.login(name, password, bindUser, bindMessage)
        }
    }

    fun signup(name: String, password: String) {
        viewModelScope.launch {
            _loading.postValue(true)
            dataRepository.signup(name, password, bindUser, bindMessage)
        }
    }

    fun setMessage(message: String) {
        _message.postValue(message)
    }
}