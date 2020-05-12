package com.myomdbapplication.repository

import androidx.paging.LivePagedListBuilder
import com.myomdbapplication.db.OmdbLocalCache
import com.myomdbapplication.models.MovieDetailsResponse
import com.myomdbapplication.models.OmdbPagingResponse
import com.myomdbapplication.repository.api.NetworkFlowHandler
import com.myomdbapplication.repository.api.OMDBRemoteServices
import com.myomdbapplication.repository.api.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class OmdbRemoteRepository(
    private val apiService: OMDBRemoteServices,
    private val cache: OmdbLocalCache
) {

    private lateinit var boundaryCallback : OmdbBoundaryCallback

    fun getMoviesBySearch(searchTerm: String): OmdbPagingResponse {
        // Single truth data (Locally stored data will be passed to pagging library)
        val dataSourceFactory = cache.moviesByNameQuery(searchTerm)

        boundaryCallback = OmdbBoundaryCallback(searchTerm, apiService, cache)
        val networkError = boundaryCallback.networkState

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()
        return OmdbPagingResponse(data, networkError!!)
    }

    fun retryBoundaryCallback() {
        boundaryCallback.retryPetitions()
    }

    fun getMovieDetailsById(key: String, id: String): Flow<ResponseState<MovieDetailsResponse>> =
        object : NetworkFlowHandler<MovieDetailsResponse>() {
            override suspend fun fetchRemoteData(): Response<MovieDetailsResponse> =
                apiService.getShowDetails(key, id)
        }.asFlows().flowOn(Dispatchers.IO)

    companion object {
        private const val DATABASE_PAGE_SIZE = 7
    }
}