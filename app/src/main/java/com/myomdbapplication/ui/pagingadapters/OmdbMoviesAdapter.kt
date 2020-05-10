package com.myomdbapplication.ui.pagingadapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.myomdbapplication.models.MovieItem

class OmdbMoviesAdapter(private val onClick: (MovieItem) -> Unit) :
    PagedListAdapter<MovieItem, OmdbItemViewHolder>(MOVIE_DIFF_UTIL) {

    companion object {
        private val MOVIE_DIFF_UTIL = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem.imdbID == newItem.imdbID
            }

            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OmdbItemViewHolder {
        return OmdbItemViewHolder.createView(parent, onClick)
    }

    override fun onBindViewHolder(holder: OmdbItemViewHolder, position: Int) {
        val movieItem = getItem(position)
        movieItem?.let {
            holder.bind(movieItem)
        }
    }
}