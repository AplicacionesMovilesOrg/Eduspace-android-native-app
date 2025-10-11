package upc.edu.pe.eduspace.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    @Named("url")
    fun provideBaseUrl(): String {
        return "https://eduspace-platform-production-e783.up.railway.app/api/v1/"
    }

    @Provides
    @Singleton
    fun provideRetrofit(@Named("url") apiBaseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}