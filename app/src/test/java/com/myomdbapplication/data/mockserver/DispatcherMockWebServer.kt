package com.myomdbapplication.data.mockserver

import com.myomdbapplication.data.getJson
import com.myomdbapplication.repository.api.OMDBRemoteServices
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class DispatcherMockWebServer : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        val requestUrl = request.requestUrl
        return when (requestUrl?.encodedPath) {
            "/" -> {
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        getJson(
                            "data/omdbmovieslist.json",
                            OMDBRemoteServices::class
                        )
                    )
            }
            else -> MockResponse().setResponseCode(404)
        }
    }
}