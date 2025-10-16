package upc.edu.pe.eduspace.features.shared_spaces.data.remote.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import upc.edu.pe.eduspace.features.shared_spaces.data.repositories.SharedAreasRepositoryImpl
import upc.edu.pe.eduspace.features.shared_spaces.domain.repositories.SharedAreasRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositorySharedAreas {

    @Binds
    @Singleton
    abstract fun bindSharedAreasRepository(
        sharedAreasRepositoryImpl: SharedAreasRepositoryImpl
    ): SharedAreasRepository
}
