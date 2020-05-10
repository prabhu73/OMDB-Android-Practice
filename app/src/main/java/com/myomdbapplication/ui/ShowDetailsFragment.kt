package com.myomdbapplication.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.myomdbapplication.R
import com.myomdbapplication.databinding.FragmentShowDetailsBinding
import com.myomdbapplication.models.MovieDetailsResponse
import com.myomdbapplication.repository.api.ResponseState
import kotlinx.android.synthetic.main.fragment_show_details.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class ShowDetailsFragment : Fragment() {

    private val viewModel: OmdbViewModel by sharedViewModel()
    private lateinit var binding: FragmentShowDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowDetailsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMovieDetailsById()
        initObservables()
    }

    private fun initObservables() {
        viewModel.showDetails.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResponseState.Success -> displayUiData(it.data)
                is ResponseState.Loading -> handleShimmerVisibility(true)
                is ResponseState.Failed -> {
                    handleShimmerVisibility(false)
                    Toast.makeText(context, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun handleShimmerVisibility(isVisible: Boolean) {
        shimmerLoadingView.visibility = if (isVisible) View.VISIBLE else View.GONE
        if (isVisible) shimmerLoadingView.startShimmer()
        else shimmerLoadingView.stopShimmer()
    }

    @SuppressLint("SetTextI18n")
    private fun displayUiData(movieDetails: MovieDetailsResponse) {
        handleShimmerVisibility(false)
        toolbarTitle.text = movieDetails.title
        backNavIcon.setOnClickListener { findNavController().popBackStack() }
        Glide.with(showPosterImage).load(movieDetails.poster).into(showPosterImage)
        Glide.with(showSmallPoster).load(movieDetails.poster).into(showSmallPoster)
        releasedDate.text = movieDetails.year
        showTime.text = movieDetails.runtime
        userRating.text = movieDetails.imdbRating
        showLanguage.text = movieDetails.language
        directorDetails.text = "${resources.getString(R.string.director_details)} ${movieDetails.director}"
        genreDetails.text = "${resources.getString(R.string.type)} ${movieDetails.genre}"
        showDescription.text = movieDetails.plot
        showActors.text = movieDetails.actors
        writerDetails.text = movieDetails.writer
    }
}
