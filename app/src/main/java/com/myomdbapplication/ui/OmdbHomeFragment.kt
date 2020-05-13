package com.myomdbapplication.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.myomdbapplication.R
import com.myomdbapplication.databinding.FragmentOmdbHomeBinding
import com.myomdbapplication.ext.hideKeyboard
import com.myomdbapplication.ext.isNetworkAvailable
import com.myomdbapplication.repository.api.Status
import com.myomdbapplication.ui.pagingadapters.OmdbMoviesAdapter
import kotlinx.android.synthetic.main.fragment_omdb_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.NumberFormatException

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class OmdbHomeFragment : Fragment() {

    private lateinit var binding: FragmentOmdbHomeBinding
    private val viewModel: OmdbViewModel by sharedViewModel()
    private lateinit var adapter: OmdbMoviesAdapter

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
            viewModel.isNavigatedToDetailsScreen = true
            findNavController().navigate(OmdbHomeFragmentDirections.actionMoviesHomeFragmentToShowDetailsFragment())
        }
        initAdapter()
        val query = savedInstanceState?.getString(QUERY) ?: viewModel.lastQueryValue() ?: DEFAULT_QUERY
        if (viewModel.isNavigatedToDetailsScreen) viewModel.isNavigatedToDetailsScreen = false
        else {
            shimmerVisibility(true)
            if (viewModel.isNetworkNotAvailable) viewModel.retryBoundaryCall()
            else viewModel.searchMovies(query)
        }
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
            if (it.status == Status.FAILED) {
                it.msg?.let { msg ->
                    try {
                        when(msg.toInt()) {
                            504 -> (activity as OmdbMainActivity)
                                .displaySnackBar(R.string.enable_internet_text, R.string.enable,
                                    SnackbarAction.NETWORK_NOT_AVAILABLE)
                            in 400..499 -> (activity as OmdbMainActivity)
                                .displaySnackBar(R.string.retry_api_details, R.string.retry,
                                    SnackbarAction.API_RETRY)
                        }
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                }
                Toast.makeText(context, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.activityCommunication.observe(viewLifecycleOwner, Observer {
            if(it != null && it == SnackbarAction.API_RETRY) viewModel.retryBoundaryCall()
        })
    }

    private fun initSearchField(query: String) {
        searchOmdb.setText(query)
        searchOmdb.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                requireContext().hideKeyboard(searchOmdb)
                updateMoviesFromInput()
                true
            } else {
                false
            }
        }
        searchOmdb.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                requireContext().hideKeyboard(searchOmdb)
                updateMoviesFromInput()
                true
            } else {
                false
            }
        }
        searchOmdb.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateMoviesFromInput()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
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