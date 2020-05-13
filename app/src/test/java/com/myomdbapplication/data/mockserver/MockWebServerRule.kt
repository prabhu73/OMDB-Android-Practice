package com.myomdbapplication.data.mockserver

import android.util.Log
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import okhttp3.mockwebserver.MockWebServer
import java.io.IOException
import java.lang.Exception


class MockWebServerRule : TestRule {

    companion object {
        const val MOCK_WEBSERVER_PORT = 8000
        const val MOCK_WEB_SERVER_URL = "http://localhost:$MOCK_WEBSERVER_PORT"
    }

    private lateinit var mServer: MockWebServer

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                startServer()
                try {
                    base?.evaluate()
                } finally {
                    stopServer()
                }
            }
        }
    }

    fun server(): MockWebServer = mServer

    fun startServer() {
        mServer = MockWebServer()
        try {
            mServer.close()
            mServer.start(MOCK_WEBSERVER_PORT)
            mServer.dispatcher = DispatcherMockWebServer()
        } catch (e: Exception) {
            Log.e("##StartServerError", "mock server start issue", e.cause)
        }

    }

    fun stopServer() {
        try {
            mServer.shutdown()
        } catch (e: IOException) {
            Log.e("####ServerStopIssue", "mock server shutdown error", e.cause)
        }
    }
}