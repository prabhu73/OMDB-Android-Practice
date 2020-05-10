package com.myomdbapplication.ui.pagingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myomdbapplication.databinding.LayoutMovieItemBinding
import com.myomdbapplication.models.MovieItem

class OmdbItemViewHolder(private val view: LayoutMovieItemBinding, onClick: (MovieItem) -> Unit): RecyclerView.ViewHolder(view.root) {

    private var movie: MovieItem? = null

    init {
        view.movieItemView.setOnClickListener {
            movie?.let { onClick(it) }
        }
    }

    fun bind(movie: MovieItem) {
        this.movie = movie
        Glide.with(view.posterImageView).load(movie.poster).into(view.posterImageView)
        view.showTitle.text = movie.title
        view.showType.text = movie.type
        view.showYear.text = movie.year
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

