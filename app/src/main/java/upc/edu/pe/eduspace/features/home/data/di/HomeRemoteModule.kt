package upc.edu.pe.eduspace.features.home.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import upc.edu.pe.eduspace.features.home.data.remote.services.HomeService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeRemoteModule {

    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)
}
