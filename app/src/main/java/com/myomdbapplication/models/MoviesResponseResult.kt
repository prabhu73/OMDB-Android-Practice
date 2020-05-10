package com.myomdbapplication.models

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * Result for paged list data (Used by pagging library)
 */
data class MoviesResponseResult(
    val data: LiveData<PagedList<MovieItem>>,
    val networkErrors: LiveData<String>
)