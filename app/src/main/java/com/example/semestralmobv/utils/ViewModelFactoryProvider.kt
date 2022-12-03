package com.example.semestralmobv.utils

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.semestralmobv.data.DataRepository
import com.example.semestralmobv.data.api.ApiService
import com.example.semestralmobv.data.db.LocalDatabase
import com.example.semestralmobv.data.db.dao.FriendItemDao
import com.example.semestralmobv.data.db.dao.PubItemDao

object ViewModelFactoryProvider {
    private fun providePubCache(context: Context): PubItemDao {
        val db = LocalDatabase.getInstance(context);
        return db.pubItemDao()
    }

    private fun provideFriendCache(context: Context): FriendItemDao {
        val db = LocalDatabase.getInstance(context);
        return db.friendItemDao()
    }


    fun provideDataRepository(context: Context): DataRepository {
        return DataRepository.getInstance(
            ApiService.create(context), providePubCache(context), provideFriendCache(context)
        )
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(
            provideDataRepository(context)
        )
    }
}