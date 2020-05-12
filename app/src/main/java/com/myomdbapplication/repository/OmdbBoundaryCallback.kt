package com.myomdbapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.myomdbapplication.db.OmdbLocalCache
import com.myomdbapplication.ext.PagingRequestHelper
import com.myomdbapplication.ext.createStatusLiveData
import com.myomdbapplication.models.MovieItem
import com.myomdbapplication.repository.api.NetworkState
import com.myomdbapplication.repository.api.OMDBRemoteServices
import com.myomdbapplication.repository.api.searchOmdbByQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class OmdbBoundaryCallback(
    private val query: String,
    private val remoteService: OMDBRemoteServices,
    private val localCache: OmdbLocalCache
) : PagedList.BoundaryCallback<MovieItem>() {

    private var pageCount = 1

    // LiveData of network errors.
    private val helper = PagingRequestHelper(Executors.newSingleThreadExecutor())
    private val _networkState: MutableLiveData<NetworkState> = helper.createStatusLiveData()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    private fun requestAndSaveDataLocally(query: String, pagingRequest: PagingRequestHelper.Request.Callback) {
        if (isRequestInProgress) return
        isRequestInProgress = true
        searchOmdbByQuery(remoteService, query, pageCount, { res ->
            localCache.insert(res) {
                pagingRequest.recordSuccess()
                pageCount++
                isRequestInProgress = false
            }
        }, { error ->
            pagingRequest.recordFailure(Throwable(error))
            _networkState.postValue(NetworkState.error(error))
            isRequestInProgress = false
        })
    }

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            requestAndSaveDataLocally(query, it)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: MovieItem) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            requestAndSaveDataLocally(query, it)
        }
    }

    fun retryPetitions() = helper.retryAllFailed()
}