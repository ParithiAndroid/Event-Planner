package com.parithidb.taskiassessment.data.di

import android.content.Context
import com.parithidb.taskiassessment.data.database.AppDatabase
import com.parithidb.taskiassessment.data.repository.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object ActivityModule {
    @Provides
    @ActivityRetainedScoped
    fun provideUserRepository(
        @ApplicationContext context: Context,
        database: AppDatabase
    ): EventRepository {
        return EventRepository(context, database)
    }

}