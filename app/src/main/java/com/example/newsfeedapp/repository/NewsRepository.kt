package com.example.newsfeedapp.repository

import com.example.newsfeedapp.api.NewsApi
import com.example.newsfeedapp.model.Article
import com.example.newsfeedapp.db.ArticleDatabase
import com.example.newsfeedapp.api.RetrofitClient
import com.example.newsfeedapp.db.ArticleDAO
import javax.inject.Inject

class NewsRepository @Inject constructor(
   private val newsApi: NewsApi,
   private  val articleDao:ArticleDAO) {

    suspend fun getBreakingNews(countryCode:String,pageNumber:Int) =
        newsApi.getBreakingNews(countryCode,pageNumber)
//        RetrofitClient.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery:String,pageNumber: Int) =
        newsApi.searchNews(searchQuery,pageNumber)
//            RetrofitClient.api.searchNews(searchQuery,pageNumber)


//    suspend fun insert(article: Article) = db.getArticleDoa().insert(article)
    suspend fun insert(article: Article) = articleDao.insert(article)
    suspend fun delete(article: Article) = articleDao.deleteArticles(article)
    fun getSavedNews()= articleDao.getAllArticles()

}