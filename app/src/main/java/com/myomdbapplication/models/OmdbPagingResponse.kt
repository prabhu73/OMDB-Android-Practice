package com.myomdbapplication.models

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.myomdbapplication.repository.api.NetworkState

/**
 * Result for paged list data (Used by pagging library)
 */
data class OmdbPagingResponse(
    val data: LiveData<PagedList<MovieItem>>,
    val networkErrors: LiveData<NetworkState>
)