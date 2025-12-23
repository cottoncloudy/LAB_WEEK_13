package com.example.test_lab_week_13

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.test_lab_week_13.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Setup DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 2. Setup ViewModel (SOLUSI BARU)
        // Kita ambil Repository dari MovieApplication, bukan bikin baru pakai RetrofitInstance
        val app = application as MovieApplication
        val movieRepository = app.movieRepository
        val viewModelFactory = MovieViewModelFactory(movieRepository)

        movieViewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        // 3. Setup Adapter
        // Pastikan langkah nomor 1 (tambah 'fun' di interface) sudah dilakukan
        movieAdapter = MovieAdapter { movie ->
            Toast.makeText(this, "Membuka: ${movie.title}", Toast.LENGTH_SHORT).show()

            // Opsional: Kode untuk pindah ke DetailsActivity
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            intent.putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            intent.putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)
            intent.putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
            startActivity(intent)
        }

        binding.rvMovies.adapter = movieAdapter

        // 4. Bind ViewModel ke Layout
        binding.viewModel = movieViewModel
        binding.lifecycleOwner = this
    }
}