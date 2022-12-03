package com.example.semestralmobv.utils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.semestralmobv.data.DataRepository
import com.example.semestralmobv.data.api.ApiService
import com.example.semestralmobv.data.db.PubsLocalDatabase
import com.example.semestralmobv.data.db.dao.PubItemDao
import com.example.semestralmobv.data.db.models.PubItem

object ViewModelFactoryProvider {
    private fun provideLocalDatabase(context: Context): PubItemDao {
        val db = PubsLocalDatabase.getInstance(context);
        return db.pubItemDao()
    }

    private fun provideDataRepository(context: Context): DataRepository {
        return DataRepository.getInstance(ApiService.create(context), provideLocalDatabase(context));
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(
            provideDataRepository(context)
        )
    }
}