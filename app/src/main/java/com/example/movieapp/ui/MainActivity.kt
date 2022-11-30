package com.example.movieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var toolbarFavButton: ImageView
    private val mainViewModel: MainActivityViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        toolbarFavButton = binding.toolbarFavButton
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        binding.toolbarBackButton.setOnClickListener {
            onBackPressed()
        }

        //val navHostFragment =
        //supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        //val navController = navHostFragment.navController
        //binding.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)

        //val appBarConfiguration = AppBarConfiguration(navController.graph)
        //binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        binding.toolbar.setTitle(R.string.list_fragment_label)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.toolbarTitle.setText(R.string.list_fragment_label)
        binding.toolbarBackButton.visibility = View.GONE
        binding.toolbarFavButton.visibility = View.GONE
        binding.toolbarTitle.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    }

    fun deleteFavMovie(title: String, fav: Boolean, imdbID: String) {
        mainViewModel.delete(imdbID)
        setToolbar(title, fav)
    }

    fun addFavMovie(title: String, fav: Boolean, imdbID: String) {
        mainViewModel.addMovie(imdbID)
        setToolbar(title, fav)
    }


    fun setToolbar(title: String, fav: Boolean) {
        var removeRange: String
        if (title.length > 19) {
            removeRange = title.removeRange(20, title.length)
            removeRange += "..."
            binding.toolbarTitle.text = removeRange
        } else {
            binding.toolbarTitle.text = title
        }

        if (fav) {
            binding.toolbarFavButton.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.toolbarFavButton.setImageResource(R.drawable.ic_not_favorite)
        }

        binding.toolbarBackButton.visibility = View.VISIBLE
        binding.toolbarFavButton.visibility = View.VISIBLE
        binding.toolbarTitle.textAlignment = View.TEXT_ALIGNMENT_CENTER

    }
}