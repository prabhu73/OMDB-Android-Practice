package com.myomdbapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.myomdbapplication.R
import com.myomdbapplication.databinding.ActivityOmdbMainBinding
import com.myomdbapplication.ui.pagingadapters.OmdbMoviesAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class OmdbMainActivity : AppCompatActivity() {

    private val viewModel: OmdbViewModel by viewModel()
    private lateinit var adapter: OmdbMoviesAdapter
    private lateinit var binding: ActivityOmdbMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOmdbMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = OmdbMoviesAdapter {
            // Handle Item click functionality
        }

        findNavController(R.id.nav_host_fragment).setGraph(R.navigation.app_nav)

        viewModel.searchMovies("friends")
        viewModel.getMovieDetailsById()
    }
}
