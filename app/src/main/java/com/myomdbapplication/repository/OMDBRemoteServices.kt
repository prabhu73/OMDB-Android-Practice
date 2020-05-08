package com.myomdbapplication.repository

import retrofit2.Response
import retrofit2.http.GET

interface OMDBRemoteServices {

    //http://www.omdbapi.com/?apikey=c577c726&s=friends&page=1 // To search for movies name friends
    //http://www.omdbapi.com/?&apikey=c577c726&i=tt3896198 // To get particular show details

    @GET("/data.json")
    suspend fun getStateWiseData(): Response<Any>

    @GET("/v2/state_district_wise.json")
    suspend fun getStateDistrictWiseData(): Response<List<Any>>

}