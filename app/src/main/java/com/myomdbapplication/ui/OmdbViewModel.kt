package com.myomdbapplication.ui

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.myomdbapplication.BuildConfig
import com.myomdbapplication.models.MovieDetailsResponse
import com.myomdbapplication.models.MovieItem
import com.myomdbapplication.models.MoviesResponseResult
import com.myomdbapplication.repository.OmdbRemoteRepository
import com.myomdbapplication.repository.ResponseState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
val omdbViewModel = module {
    viewModel {
        OmdbViewModel(get())
    }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class OmdbViewModel constructor(private val repository: OmdbRemoteRepository) : ViewModel() {

    private val _showDetailsRes = MutableLiveData<ResponseState<MovieDetailsResponse>>()
    val showDetails: LiveData<ResponseState<MovieDetailsResponse>> get() = _showDetailsRes

    private val queryLiveData = MutableLiveData<String>()
    private val moviesResult: LiveData<MoviesResponseResult> = Transformations.map(queryLiveData) {
        repository.getMoviesBySearch(it)
    }

    val movies: LiveData<PagedList<MovieItem>> = Transformations.switchMap(moviesResult) { it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(moviesResult) {
        it.networkErrors
    }

    fun searchMovies(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun lastQueryValue(): String? = queryLiveData.value

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