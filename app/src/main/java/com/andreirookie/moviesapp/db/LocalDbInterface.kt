package com.andreirookie.moviesapp.db

import androidx.lifecycle.LiveData
import com.andreirookie.moviesapp.data.Movie

interface LocalDbInterface {
    fun removeById(movieId: Long)
    fun add(movie: Movie)
    fun getSaved(): LiveData<List<Movie>>

}