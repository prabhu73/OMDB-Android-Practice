package com.myomdbapplication.ui

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.myomdbapplication.databinding.FragmentOmdbHomeBinding
import com.myomdbapplication.ui.pagingadapters.OmdbMoviesAdapter
import kotlinx.android.synthetic.main.fragment_omdb_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class OmdbHomeFragment : Fragment() {

    private lateinit var binding: FragmentOmdbHomeBinding
    private val viewModel: OmdbViewModel by sharedViewModel()
    private lateinit var adapter : OmdbMoviesAdapter

    companion object {
        private const val QUERY = "QUERY"
        private const val DEFAULT_QUERY = "Friend"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOmdbHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OmdbMoviesAdapter {
            viewModel.omdbId = it.imdbID
            findNavController().navigate(OmdbHomeFragmentDirections.actionMoviesHomeFragmentToShowDetailsFragment())
        }
        initAdapter()
        val query = savedInstanceState?.getString(QUERY) ?: viewModel.lastQueryValue() ?: DEFAULT_QUERY
        shimmerVisibility(true)
        viewModel.searchMovies(query)
        initSearchField(query)
    }

    private fun initAdapter() {
        moviesList.adapter = adapter
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            shimmerVisibility(false)
            adapter.submitList(it)
        })
        viewModel.networkErrors.observe(viewLifecycleOwner, Observer {
            shimmerVisibility(false)
            Toast.makeText(context, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })
    }

    private fun initSearchField(query: String) {
        searchOmdb.setText(query)
        searchOmdb.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateMoviesFromInput()
                true
            } else {
                false
            }
        }
        searchOmdb.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateMoviesFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateMoviesFromInput() {
        searchOmdb.text.trim().let {
            if (it.isNotEmpty()) {
                moviesList.scrollToPosition(0)
                shimmerVisibility(true)
                viewModel.searchMovies(it.toString())
                adapter.submitList(null)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(QUERY, viewModel.lastQueryValue())
    }

    private fun shimmerVisibility(isVisible: Boolean) {
        homeShimmerAnimation.visibility = if (isVisible) View.VISIBLE else View.GONE
        if (isVisible) homeShimmerAnimation.startShimmer()
        else homeShimmerAnimation.stopShimmer()
    }
}