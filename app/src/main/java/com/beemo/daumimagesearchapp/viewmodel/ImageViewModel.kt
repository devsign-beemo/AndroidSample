package com.beemo.daumimagesearchapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemo.daumimagesearchapp.repository.ImageRepository
import com.beemo.daumimagesearchapp.response.ImageSearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(private val repository : ImageRepository) : ViewModel() {

    val imageResult : MutableLiveData<Response<ImageSearchResponse>> = MutableLiveData()

    fun searchImage(query : String, sort : String, page: Int){
        viewModelScope.launch {
            val response = repository.searchImage(query, sort, page)
            imageResult.value = response

        }
    }
}