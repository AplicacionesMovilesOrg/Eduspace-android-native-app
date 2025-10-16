package upc.edu.pe.eduspace.features.shared_spaces.data.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import upc.edu.pe.eduspace.features.shared_spaces.data.remote.services.SharedAreasService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleSharedAreas {

    @Provides
    @Singleton
    fun provideSharedAreasService(retrofit: Retrofit): SharedAreasService {
        return retrofit.create(SharedAreasService::class.java)
    }
}
