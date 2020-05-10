package com.myomdbapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.myomdbapplication.db.OmdbLocalCache
import com.myomdbapplication.models.MovieItem
import com.myomdbapplication.repository.api.OMDBRemoteServices
import com.myomdbapplication.repository.api.searchOmdbByQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MoviesBoundaryCallback(
    private val query: String,
    private val remoteService: OMDBRemoteServices,
    private val localCache: OmdbLocalCache
) : PagedList.BoundaryCallback<MovieItem>() {

    private var pageCount = 1

    // LiveData of network errors.
    private val _networkErrors = MutableLiveData<String>()
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    private fun requestAndSaveDataLocally(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchOmdbByQuery(remoteService, query, pageCount, { res ->
            localCache.insert(res) {
                pageCount++
                isRequestInProgress = false
            }
        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }

    override fun onZeroItemsLoaded() {
        requestAndSaveDataLocally(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: MovieItem) {
        requestAndSaveDataLocally(query)
    }
}