package com.example.test_lab_week_13

import android.util.Log // Import Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.database.MovieDatabase
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase
) {
    private val apiKey = "02e464701823dfa0e710aba4ae1ad5ef"

    fun fetchMovies(): Flow<List<Movie>> {
        // ... (Kode flow yang sudah ada biarkan saja) ...
        return flow {
            val movieDao = movieDatabase.movieDao()
            val savedMovies = movieDao.getMovies()
            if (savedMovies.isEmpty()) {
                try {
                    val popularMovies = movieService.getPopularMovies(apiKey)
                    val movies = popularMovies.results
                    movieDao.addMovies(movies)
                    emit(movies)
                } catch (e: Exception) {
                    emit(emptyList())
                }
            } else {
                emit(savedMovies)
            }
        }.flowOn(Dispatchers.IO)
    }

    // --- TAMBAHKAN FUNGSI BARU INI ---
    suspend fun fetchMoviesFromNetwork() {
        val movieDao = movieDatabase.movieDao()
        try {
            val popularMovies = movieService.getPopularMovies(apiKey)
            val moviesFetched = popularMovies.results

            // Simpan ke database (ini akan menimpa data lama karena ConflictStrategy.REPLACE)
            movieDao.addMovies(moviesFetched)

            Log.d("MovieRepository", "Success fetching data from network")
        } catch (exception: Exception) {
            Log.d("MovieRepository", "An error occurred: ${exception.message}")
        }
    }
}