package com.example.newsfeedapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsfeedapp.model.Article
@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article):Long

    @Query("Select * FROM articles")
    fun getAllArticles():LiveData<List<Article>>

    @Delete
    suspend fun deleteArticles(article: Article)
}