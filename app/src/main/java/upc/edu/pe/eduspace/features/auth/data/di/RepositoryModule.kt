package upc.edu.pe.eduspace.features.auth.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import upc.edu.pe.eduspace.features.auth.data.repositories.AuthRepositoryImpl
import upc.edu.pe.eduspace.features.auth.domain.repositories.AuthRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}