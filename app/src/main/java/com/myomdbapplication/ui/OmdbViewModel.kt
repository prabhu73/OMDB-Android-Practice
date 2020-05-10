package com.myomdbapplication.ui

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.google.gson.GsonBuilder
import com.myomdbapplication.BuildConfig
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
class OmdbViewModel constructor(private val repository: OmdbRemoteRepository): ViewModel() {

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

    fun getMovieDetailsById() {
        viewModelScope.launch {
            repository.getMovieDetailsById(BuildConfig.API_KEY, "tt0108778").collect {
                when(it) {
                    is ResponseState.Failed -> {}
                    is ResponseState.Loading -> {}
                    is ResponseState.Success -> {
                        Log.d("####MoviesDetails", GsonBuilder().create().toJson(it.data))
                    }
                }
            }
        }
    }
}