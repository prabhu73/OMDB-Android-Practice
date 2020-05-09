package com.myomdbapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
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

    fun getMoviesBySearchTerm() {
        viewModelScope.launch {
            repository.getMoviesBySearch(BuildConfig.API_KEY, "friends", 1).collect {
                when(it) {
                    is ResponseState.Failed -> {}
                    is ResponseState.Loading -> {}
                    is ResponseState.Success -> {
                        Log.d("####MoviesList", GsonBuilder().create().toJson(it.data))
                    }
                }
            }
        }
    }

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