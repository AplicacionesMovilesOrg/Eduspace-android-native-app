package upc.edu.pe.eduspace.features.home.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import upc.edu.pe.eduspace.features.home.data.repositories.HomeRepositoryImpl
import upc.edu.pe.eduspace.features.home.domain.repositories.HomeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository
}
