package com.example.newsfeedapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsfeedapp.databinding.ActivityMainBinding
import com.example.newsfeedapp.repository.NewsRepository
import com.example.newsfeedapp.db.ArticleDatabase
import com.example.newsfeedapp.viewmodel.NewsViewModel
import com.example.newsfeedapp.viewmodel.NewsViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding:ActivityMainBinding
    lateinit var mainViewModel:NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProvider = NewsViewModelFactory(application,newsRepository)
        mainViewModel = ViewModelProvider(this,viewModelProvider).get(NewsViewModel::class.java)

        val navController  = supportFragmentManager.findFragmentById(R.id.newsFragment)?.findNavController()
        activityMainBinding.bottomNavigationView.setupWithNavController(navController!!)



    }
}