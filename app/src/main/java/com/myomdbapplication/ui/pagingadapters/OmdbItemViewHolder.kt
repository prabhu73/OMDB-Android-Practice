package com.myomdbapplication.ui.pagingadapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myomdbapplication.R
import com.myomdbapplication.databinding.LayoutMovieItemBinding
import com.myomdbapplication.models.MovieItem

class OmdbItemViewHolder(private val view: LayoutMovieItemBinding, onClick: (MovieItem) -> Unit):
    RecyclerView.ViewHolder(view.root) {

    private var movie: MovieItem? = null

    init {
        view.movieItemView.setOnClickListener {
            movie?.let { onClick(it) }
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(movie: MovieItem) {
        this.movie = movie
        Glide.with(view.posterImageView).load(movie.poster).centerCrop().placeholder(R.drawable.ic_default_movie_icon).into(view.posterImageView)
        view.showTitle.text = movie.title
        view.showType.text = "${view.root.context.resources.getString(R.string.type)} ${movie.type}"
        view.showYear.text = "${view.root.context.resources.getString(R.string.released)} ${movie.year}"
    }

    companion object {
        fun createView(parent: ViewGroup, onClick: (MovieItem) -> Unit): OmdbItemViewHolder {
            return OmdbItemViewHolder(
                LayoutMovieItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onClick
            )
        }
    }
}

