package com.example.semestralmobv.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semestralmobv.data.DataRepository
import com.example.semestralmobv.ui.viewmodels.*

class ViewModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthorizationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return AuthorizationViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(PubsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return PubsViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(NearbyPubsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return NearbyPubsViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(PubDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return PubDetailViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return FriendsViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(AddFriendViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return AddFriendViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}