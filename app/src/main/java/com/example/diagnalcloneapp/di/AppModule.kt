package com.example.diagnalcloneapp.di

import android.app.Application
import android.content.Context
import com.example.diagnalcloneapp.view_model.PosterViewModelFectory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideListingViewModelFactory(
        application: Application,
        context: Context
    ): PosterViewModelFectory {
        return  PosterViewModelFectory(
            application,
            context
        )
    }

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

}