package upc.edu.pe.eduspace.features.classrooms.data.remote.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import upc.edu.pe.eduspace.features.classrooms.data.repositories.ClassroomsRepositoryImpl
import upc.edu.pe.eduspace.features.classrooms.data.repositories.ResourcesRepositoryImpl
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.ClassroomsRepository
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.ResourcesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryClassrooms {

    @Binds
    @Singleton
    abstract fun bindClassroomsRepository(
        classroomsRepositoryImpl: ClassroomsRepositoryImpl
    ): ClassroomsRepository

    @Binds
    @Singleton
    abstract fun bindResourcesRepository(
        resourcesRepositoryImpl: ResourcesRepositoryImpl
    ): ResourcesRepository
}
