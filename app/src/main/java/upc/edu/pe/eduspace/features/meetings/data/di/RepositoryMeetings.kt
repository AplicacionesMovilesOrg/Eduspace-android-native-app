package upc.edu.pe.eduspace.features.meetings.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import upc.edu.pe.eduspace.features.meetings.data.repositories.MeetingsRepositoryImpl
import upc.edu.pe.eduspace.features.meetings.domain.repositories.MeetingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryMeetings {

    @Binds
    @Singleton
    abstract fun bindMeetingsRepository(
        meetingsRepositoryImpl: MeetingsRepositoryImpl
    ): MeetingsRepository
}
