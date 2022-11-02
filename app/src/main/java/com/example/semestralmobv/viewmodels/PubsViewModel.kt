package com.example.semestralmobv.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semestralmobv.api.PubsApi
import com.example.semestralmobv.api.models.Pub
import com.example.semestralmobv.api.models.PubsArgs
import com.example.semestralmobv.api.models.PubsData
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PubsViewModel : ViewModel() {
    private val _pubs = MutableLiveData<MutableList<Pub>>()
    val pubs: LiveData<MutableList<Pub>> get() = _pubs

    private var _status: String = "Init"
    val status: String get() = _status

    init {
        _pubs.value = mutableListOf()
        getPubs()
    }

    fun getPubs() {
        viewModelScope.launch {
            try {
                PubsApi.retrofitService.getPubs(PubsArgs("bars", "mobvapp", "Cluster0", null))
                    .enqueue(object : Callback<PubsData> {
                        override fun onResponse(
                            call: Call<PubsData>, response: Response<PubsData>
                        ) {
                            Log.i("resultCode", response.code().toString())
                            Log.i("resultMessage", response.message().toString())
                            Log.i("resultRaw", response.raw().toString())
                            _pubs.value = response.body()?.documents
                            _status = "Success: Items ${response.body()?.documents?.size}"
                        }

                        override fun onFailure(call: Call<PubsData>, t: Throwable) {
                            Log.i("response", "onFailure")
                            _pubs.value = mutableListOf()
                            _status = "Failure"
                        }
                    })
            } catch (e: java.lang.Exception) {
                _status = "Failure: ${e.message}"
                _pubs.value = mutableListOf()
            }
        }
    }
}