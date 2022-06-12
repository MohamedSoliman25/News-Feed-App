package com.example.newsfeedapp.repository

import com.example.newsfeedapp.model.Article
import com.example.newsfeedapp.db.ArticleDatabase
import com.example.newsfeedapp.api.RetrofitClient

class NewsRepository(
    val db:ArticleDatabase) {

    suspend fun getBreakingNews(countryCode:String,pageNumber:Int) =
        RetrofitClient.api.getBreakingNews(countryCode,pageNumber)

    suspend fun insert(article: Article) = db.getArticleDoa().insert(article)
    suspend fun delete(article: Article) = db.getArticleDoa().deleteArticles(article)

    fun getAllArticles()= db.getArticleDoa().getArticles()
}