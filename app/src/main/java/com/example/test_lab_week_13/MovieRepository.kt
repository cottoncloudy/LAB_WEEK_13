package com.example.test_lab_week_13

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(private val movieService: MovieService) {
    // GANTI DENGAN API KEY ASLI KAMU
    private val apiKey = "02e464701823dfa0e710aba4ae1ad5ef"

    private val movieLiveData = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieLiveData

    private val errorLiveData = MutableLiveData<String>()
    val error: LiveData<String>
        get() = errorLiveData

    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            // emit data dari API
            val popularMovies = movieService.getPopularMovies(apiKey)
            emit(popularMovies.results)
        }.flowOn(Dispatchers.IO) // Jalankan di background thread (IO)
    }
}