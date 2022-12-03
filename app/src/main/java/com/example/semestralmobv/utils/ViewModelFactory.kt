package com.example.semestralmobv.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semestralmobv.data.DataRepository
import com.example.semestralmobv.ui.viewmodels.AuthorizationViewModel
import com.example.semestralmobv.ui.viewmodels.PubsViewModel
import com.example.semestralmobv.ui.viewmodels.NearbyPubsViewModel
import com.example.semestralmobv.ui.viewmodels.PubDetailViewModel

class ViewModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthorizationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthorizationViewModel(repository) as T
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

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}