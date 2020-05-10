package com.myomdbapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.myomdbapplication.R
import com.myomdbapplication.databinding.FragmentOmdbHomeBinding
import com.myomdbapplication.ui.pagingadapters.OmdbMoviesAdapter
import kotlinx.android.synthetic.main.fragment_omdb_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
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
            findNavController().navigate(OmdbHomeFragmentDirections.actionMoviesHomeFragmentToShowDetailsFragment())
        }
        initAdapter()
        val query = savedInstanceState?.getString(QUERY) ?: DEFAULT_QUERY
        viewModel.searchMovies(query)
    }

    private fun initAdapter() {
        moviesList.adapter = adapter
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.networkErrors.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(QUERY, viewModel.lastQueryValue())
    }

}