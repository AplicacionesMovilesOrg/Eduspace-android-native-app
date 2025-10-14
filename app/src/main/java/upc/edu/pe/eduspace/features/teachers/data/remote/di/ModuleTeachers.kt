package upc.edu.pe.eduspace.features.teachers.data.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import upc.edu.pe.eduspace.features.teachers.data.remote.services.TeachersService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleTeachers {

    @Provides
    @Singleton
    fun provideTeachersService(retrofit: Retrofit): TeachersService{
        return retrofit.create(TeachersService::class.java)
    }
}