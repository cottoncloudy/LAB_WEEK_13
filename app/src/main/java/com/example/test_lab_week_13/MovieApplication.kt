package com.example.test_lab_week_13

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.database.MovieDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit // Jangan lupa import ini

class MovieApplication : Application() {
    lateinit var movieRepository: MovieRepository

    override fun onCreate() {
        super.onCreate()

        // 1. Setup Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val movieService = retrofit.create(MovieService::class.java)

        // 2. Setup Database
        val movieDatabase = MovieDatabase.getInstance(applicationContext)

        // 3. Setup Repository
        movieRepository = MovieRepository(movieService, movieDatabase)

        // --- TAMBAHAN BAGIAN WORKMANAGER ---
        setupWorker()
    }

    private fun setupWorker() {
        // Constraints: Hanya jalan jika ada internet
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Request: Jalan setiap 1 jam
        val workRequest = PeriodicWorkRequest.Builder(
            MovieWorker::class.java,
            1, TimeUnit.HOURS // Interval minimal 15 menit
        )
            .setConstraints(constraints)
            .addTag("movie-work")
            .build()

        // Enqueue: Jadwalkan tugas
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}