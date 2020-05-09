package com.myomdbapplication.repository

import com.myomdbapplication.models.MovieDetailsResponse
import com.myomdbapplication.models.MoviesResultResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBRemoteServices {

    @GET("/")
    suspend fun getOmdbSearchData(@Query("apiKey")apiKey: String,
                                  @Query("s")term: String,
                                  @Query("page")count: Int): Response<MoviesResultResponse>

    @GET("/")
    suspend fun getShowDetails(@Query("apiKey")apiKey: String,
                               @Query("i")id: String): Response<MovieDetailsResponse>

}