package com.myomdbapplication.repository

import com.myomdbapplication.models.MovieDetailsResponse
import com.myomdbapplication.models.MoviesResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class OmdbRemoteRepository(private val apiService: OMDBRemoteServices) {

    fun getMoviesBySearch(key: String, searchTerm: String, pageCount: Int = 1): Flow<ResponseState<MoviesResultResponse>> =
        object : NetworkFlowHandler<MoviesResultResponse>() {
            override suspend fun fetchRemoteData(): Response<MoviesResultResponse> =
                apiService.getOmdbSearchData(key, searchTerm, pageCount)
        }.asFlows().flowOn(Dispatchers.IO)

    fun getMovieDetailsById(key: String, id: String): Flow<ResponseState<MovieDetailsResponse>> =
        object : NetworkFlowHandler<MovieDetailsResponse>() {
            override suspend fun fetchRemoteData(): Response<MovieDetailsResponse> =
                apiService.getShowDetails(key, id)
        }.asFlows().flowOn(Dispatchers.IO)
}