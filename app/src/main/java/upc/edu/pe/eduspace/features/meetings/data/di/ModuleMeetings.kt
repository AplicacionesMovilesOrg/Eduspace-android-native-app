package upc.edu.pe.eduspace.features.meetings.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import upc.edu.pe.eduspace.features.meetings.data.remote.services.MeetingsService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleMeetings {

    @Provides
    @Singleton
    fun provideMeetingsService(retrofit: Retrofit): MeetingsService {
        return retrofit.create(MeetingsService::class.java)
    }
}
