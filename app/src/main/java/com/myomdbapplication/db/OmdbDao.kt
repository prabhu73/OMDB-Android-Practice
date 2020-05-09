package com.myomdbapplication.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myomdbapplication.models.MovieItem

@Dao
interface OmdbDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<MovieItem>)

    /**
     * Look for repos that contain the query string in the name
     * and order those results descending, by released year
     */
    @Query("SELECT * FROM omdbdata WHERE (Title LIKE :queryString)" +
            " ORDER BY Year DESC")
    fun moviesByQuery(queryString: String): DataSource.Factory<Int, MovieItem>
}