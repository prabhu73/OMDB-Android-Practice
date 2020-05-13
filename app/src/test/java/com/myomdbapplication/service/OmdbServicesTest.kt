package com.myomdbapplication.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.myomdbapplication.BuildConfig
import com.myomdbapplication.OmdbApplication
import com.myomdbapplication.data.mockserver.MockWebServerRule
import com.myomdbapplication.di.TestKoinModule
import com.myomdbapplication.repository.api.OMDBRemoteServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.koin.test.inject
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class OmdbServicesTest : AutoCloseKoinTest() {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var mockWebServer = MockWebServerRule()

    private val mockedApplication = mock(OmdbApplication::class.java)

    private val service: OMDBRemoteServices by inject()

    @Before
    fun before() {
        val serviceModule by lazy {
            module {
                fun provideGson(): Gson =
                    GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()

                fun provideOkHttpClient(): OkHttpClient {
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.level = HttpLoggingInterceptor.Level.BODY
                    return OkHttpClient.Builder().addInterceptor(interceptor)
                        .build()
                }

                fun provideOmdbService(): OMDBRemoteServices =
                    Retrofit.Builder()
                        .baseUrl(MockWebServerRule.MOCK_WEB_SERVER_URL)
                        .addConverterFactory(GsonConverterFactory.create(get()))
                        .client(get())
                        .build()
                        .create(OMDBRemoteServices::class.java)

                single { provideGson() }
                single { provideOkHttpClient() }
                single { provideOmdbService() }
            }
        }

        startKoin {
            androidContext(mockedApplication)
            modules(serviceModule)
        }
    }

    @Test
    fun omdbListTest() {
        val execute = service.getOmdbSearchData(
            BuildConfig.API_KEY,
            "friend",
            1
        ).execute()
        assertTrue(execute.isSuccessful && execute.body() != null)
    }
}