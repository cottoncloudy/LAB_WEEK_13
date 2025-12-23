package com.example.test_lab_week_13

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MovieWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Ambil repository dari Application class
            val movieRepository = (context as MovieApplication).movieRepository

            // Jalankan fungsi refresh data
            movieRepository.fetchMoviesFromNetwork()

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}