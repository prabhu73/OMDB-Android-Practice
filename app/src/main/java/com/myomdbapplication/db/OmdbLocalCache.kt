package com.myomdbapplication.db

import androidx.paging.DataSource
import com.myomdbapplication.models.MovieItem
import java.util.concurrent.Executor


class OmdbLocalCache(
    private val omdbDao: OmdbDao,
    private val ioExecutor: Executor
) {

    // Insert movies list from remote to local cache
    fun insert(movies: List<MovieItem>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            omdbDao.insert(movies)
            insertFinished()
        }
    }

    fun moviesByNameQuery(name: String): DataSource.Factory<Int, MovieItem> {
        // appending '%' so we can allow other characters to be before and after the query string
        val query = "%${name.replace(' ', '%')}%"
        return omdbDao.moviesByQuery(query)
    }
}