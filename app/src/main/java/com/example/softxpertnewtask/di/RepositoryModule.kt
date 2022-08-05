package com.example.softxpertnewtask.di

import com.example.softxpertnewtask.home.HomeRepository
import com.example.softxpertnewtask.shared.ApiClient
import com.example.softxpertnewtask.shared.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {



    @Provides
    fun providesProductService(): RetrofitService = ApiClient.getRetrofitService()

    @Provides
    fun providedsHomeRepository(
        service: RetrofitService
    ): HomeRepository = HomeRepository(service)


    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO


}