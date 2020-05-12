package com.myomdbapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Search result response
 */

data class OmdbListResponse(
    @SerializedName("Response") val response: String,
    @SerializedName("Search") val movies: List<MovieItem>,
    @SerializedName("totalResults") val totalResults: String
)

@Entity(tableName = "omdbdata")
data class MovieItem(
    @PrimaryKey @field:SerializedName("Title") val title: String,
    @field:SerializedName("Year") val year: String,
    @field:SerializedName("imdbID") val imdbID: String,
    @field:SerializedName("Type") val type: String,
    @field:SerializedName("Poster") val poster: String
)

/**
 * Single show response details
 */
data class MovieDetailsResponse(
    @SerializedName("Response") val response: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Rated") val rated: String,
    @SerializedName("Released") val released: String,
    @SerializedName("Runtime") val runtime: String,
    @SerializedName("Genre") val genre: String,
    @SerializedName("Director") val director: String,
    @SerializedName("Writer") val writer: String,
    @SerializedName("Actors") val actors: String,
    @SerializedName("Plot") val plot: String,
    @SerializedName("Country") val country: String,
    @SerializedName("Awards") val awards: String,
    @SerializedName("Poster") val poster: String,
    @SerializedName("Type") val type: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Language") val language: String,
    @SerializedName("imdbRating") val imdbRating: String,
    @SerializedName("imdbVotes") val imdbVotes: String
)