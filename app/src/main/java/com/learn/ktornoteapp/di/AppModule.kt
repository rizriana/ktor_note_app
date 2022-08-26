package com.learn.ktornoteapp.di

import android.content.Context
import com.learn.ktornoteapp.data.local.dao.NoteDao
import com.learn.ktornoteapp.data.remote.ApiService
import com.learn.ktornoteapp.data.repository.notes.NoteRepo
import com.learn.ktornoteapp.data.repository.notes.NoteRepoImpl
import com.learn.ktornoteapp.data.repository.users.UserRepo
import com.learn.ktornoteapp.data.repository.users.UserRepoImpl
import com.learn.ktornoteapp.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext context: Context,
    ) = SessionManager(context)

    @Singleton
    @Provides
    fun provideNoteRepo(
        noteApi: ApiService,
        noteDao: NoteDao,
        sessionManager: SessionManager,
    ): NoteRepo {
        return NoteRepoImpl(
            noteApi,
            noteDao,
            sessionManager
        )
    }

    @Singleton
    @Provides
    fun provideUserRepo(
        noteApi: ApiService,
        noteDao: NoteDao,
        sessionManager: SessionManager,
    ): UserRepo {
        return UserRepoImpl(
            noteApi,
            noteDao,
            sessionManager
        )
    }
}