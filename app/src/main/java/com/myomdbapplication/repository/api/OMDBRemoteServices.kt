package com.myomdbapplication.repository.api

import com.myomdbapplication.BuildConfig
import com.myomdbapplication.models.MovieDetailsResponse
import com.myomdbapplication.models.MovieItem
import com.myomdbapplication.models.OmdbListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Call search omdb data and handle the response
 */
fun searchOmdbByQuery(
    service: OMDBRemoteServices,
    query: String, pageNum: Int,
    onSuccess: (List<MovieItem>) -> Unit,
    onError: (String) -> Unit
) {
    service.getOmdbSearchData(BuildConfig.API_KEY, query, pageNum).enqueue(
        object : Callback<OmdbListResponse> {
            override fun onFailure(call: Call<OmdbListResponse>, t: Throwable) {
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<OmdbListResponse>,
                response: Response<OmdbListResponse>
            ) {
                if (response.isSuccessful) {
                    val repos = response.body()?.movies ?: emptyList()
                    onSuccess(repos)
                } else onError("${response.code()}")
            }
        }
    )
}

interface OMDBRemoteServices {

    @GET("/")
    fun getOmdbSearchData(
        @Query("apiKey") apiKey: String,
        @Query("s") term: String,
        @Query("page") count: Int
    ): Call<OmdbListResponse>

    @GET("/")
    suspend fun getShowDetails(
        @Query("apiKey") apiKey: String,
        @Query("i") id: String
    ): Response<MovieDetailsResponse>

}