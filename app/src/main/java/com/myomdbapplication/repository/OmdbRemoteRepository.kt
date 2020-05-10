package com.myomdbapplication.repository

import androidx.paging.LivePagedListBuilder
import com.myomdbapplication.db.OmdbLocalCache
import com.myomdbapplication.models.MovieDetailsResponse
import com.myomdbapplication.models.MoviesResponseResult
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

    fun getMoviesBySearch(searchTerm: String): MoviesResponseResult {
        // Single truth data (Locally stored data will be passed to pagging library)
        val dataSourceFactory = cache.moviesByNameQuery(searchTerm)

        val boundaryCallback = MoviesBoundaryCallback(searchTerm, apiService, cache)
        val networkError = boundaryCallback.networkErrors

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()
        return MoviesResponseResult(data, networkError)
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