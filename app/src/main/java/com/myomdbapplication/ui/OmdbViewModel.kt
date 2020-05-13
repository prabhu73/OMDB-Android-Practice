package com.myomdbapplication.ui

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.myomdbapplication.BuildConfig
import com.myomdbapplication.models.MovieDetailsResponse
import com.myomdbapplication.models.MovieItem
import com.myomdbapplication.models.OmdbPagingResponse
import com.myomdbapplication.repository.OmdbRemoteRepository
import com.myomdbapplication.repository.api.NetworkState
import com.myomdbapplication.repository.api.ResponseState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
val omdbViewModel by lazy {
    module {
        viewModel {
            OmdbViewModel(get())
        }
    }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class OmdbViewModel constructor(private val repository: OmdbRemoteRepository) : ViewModel() {

    private var _isNetworkNotAvailable = false
    var isNetworkNotAvailable : Boolean get() = _isNetworkNotAvailable
    set(value) {
        _isNetworkNotAvailable = value
    }

    private var _isNavigatedToDetailsScreen = false
    var isNavigatedToDetailsScreen : Boolean get() = _isNavigatedToDetailsScreen
    set(value) {
        _isNavigatedToDetailsScreen = value
    }

    private val _showDetailsRes = MutableLiveData<ResponseState<MovieDetailsResponse>>()
    val showDetails: LiveData<ResponseState<MovieDetailsResponse>> get() = _showDetailsRes

    private val queryLiveData = MutableLiveData<String>()
    private val omdbPaging: LiveData<OmdbPagingResponse> = Transformations.map(queryLiveData) {
        repository.getMoviesBySearch(it)
    }

    val connectivityState = MutableLiveData<Boolean>()
    val activityCommunication = MutableLiveData<SnackbarAction>()

    val movies: LiveData<PagedList<MovieItem>> = Transformations.switchMap(omdbPaging) { it.data }
    val networkErrors: LiveData<NetworkState> = Transformations.switchMap(omdbPaging) {
        it.networkErrors
    }

    fun searchMovies(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun lastQueryValue(): String? = queryLiveData.value

    fun retryBoundaryCall() = repository.retryBoundaryCallback()

    private var _omdbId = String()
    var omdbId: String
        get() = _omdbId
        set(value) {
            _omdbId = value
        }

    fun getMovieDetailsById() {
        viewModelScope.launch {
            repository.getMovieDetailsById(BuildConfig.API_KEY, omdbId).collect {
                _showDetailsRes.postValue(it)
            }
        }
    }
}