package com.beemo.daumimagesearchapp.repository

import com.beemo.daumimagesearchapp.api.ApiService
import com.beemo.daumimagesearchapp.response.ImageSearchResponse
import retrofit2.Response
import javax.inject.Inject

class ImageRepository @Inject constructor(private val service: ApiService) {

    suspend fun searchImage(query : String, sort : String, page: Int) : Response<ImageSearchResponse> {
        return service.searchImage(query = query, sort = sort, page = page, size = 30)
    }
}