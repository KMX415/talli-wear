package com.talliwear.mobile.di

import android.content.Context
import com.talliwear.shared.database.ActivityDao
import com.talliwear.shared.database.TalliWearDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TalliWearDatabase {
        return TalliWearDatabase.getDatabase(context)
    }

    @Provides
    fun provideActivityDao(database: TalliWearDatabase): ActivityDao {
        return database.activityDao()
    }
} 