package upc.edu.pe.eduspace.features.classrooms.data.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import upc.edu.pe.eduspace.features.classrooms.data.remote.services.ClassroomsService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleClassrooms {

    @Provides
    @Singleton
    fun provideClassroomsService(retrofit: Retrofit): ClassroomsService {
        return retrofit.create(ClassroomsService::class.java)
    }
}
