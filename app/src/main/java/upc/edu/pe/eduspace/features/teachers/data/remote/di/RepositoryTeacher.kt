package upc.edu.pe.eduspace.features.teachers.data.remote.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import upc.edu.pe.eduspace.features.teachers.data.repositories.TeachersRepositoryImpl
import upc.edu.pe.eduspace.features.teachers.domain.repositories.TeachersRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryTeacher {

    @Binds
    fun provideTeachersRepository(impl: TeachersRepositoryImpl): TeachersRepository
}