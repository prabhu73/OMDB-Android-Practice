package com.myomdbapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class OmdbMainActivity : AppCompatActivity() {

    private val viewModel: OmdbViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_omdb_main)

        findNavController(R.id.nav_host_fragment).setGraph(R.navigation.app_nav)

        viewModel.getMoviesBySearchTerm()
        viewModel.getMovieDetailsById()
    }
}
