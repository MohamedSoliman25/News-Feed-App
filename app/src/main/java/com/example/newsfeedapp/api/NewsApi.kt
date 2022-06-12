package com.example.newsfeedapp.api

import com.example.newsfeedapp.model.NewsResponse
import com.example.newsfeedapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi{

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") country:String = "eg",
        @Query("page") pageNumber:Int,
        @Query("apiKey") apiKey:String = Constants.API_KEY
    ):Response<NewsResponse>
}