package com.example.newsfeedapp.di
import android.content.Context
import com.example.newsfeedapp.api.NewsApi
import com.example.newsfeedapp.db.ArticleDAO
import com.example.newsfeedapp.db.ArticleDatabase
import com.example.newsfeedapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {

        @Provides
        fun provideBaseUrl() = Constants.BASE_URL

        @Provides
        @Singleton
        fun provideRetrofitInstance(BASE_URL: String):NewsApi =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApi::class.java)


        @Provides
        @Singleton
        fun getAppDatabase(@ApplicationContext context:Context):ArticleDatabase{
            return ArticleDatabase(context)
        }

        @Provides
        @Singleton
        fun getAppDao(articleDatabase: ArticleDatabase):ArticleDAO{
            return articleDatabase.getArticleDoa()
        }



    }
