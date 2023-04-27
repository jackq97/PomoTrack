package com.example.pomodoro.di

import android.content.Context
import androidx.room.Room
import com.example.pomodoro.data.DurationDao
import com.example.pomodoro.data.DurationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesDurationDao(durationDatabase: DurationDatabase): DurationDao
            = durationDatabase.durationDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): DurationDatabase
            = Room.databaseBuilder(
        context,
        DurationDatabase::class.java,
        "duration_db")
        .fallbackToDestructiveMigration()
        .build()
}