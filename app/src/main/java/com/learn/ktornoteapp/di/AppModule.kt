package com.learn.ktornoteapp.di

import android.content.Context
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.google.gson.Gson
import com.learn.ktornoteapp.data.local.NoteDatabase
import com.learn.ktornoteapp.data.remote.NoteApiService
import com.learn.ktornoteapp.utils.Constant.BASE_URL
import com.learn.ktornoteapp.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGson() = Gson()

    fun provideSessionManager(
        @ApplicationContext context: Context,
    ) = SessionManager(context)

    @Singleton
    @Provides
    fun provideNoteDatabase(
        @ApplicationContext context: Context,
    ): NoteDatabase = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        "note_db"
    ).build()

    @Singleton
    @Provides
    fun provideNoteDao(
        noteDb: NoteDatabase,
    ) = noteDb.getNoteDao()

    @Singleton
    @Provides
    fun provideNoteApi(): NoteApiService {
        val client = if (BuildConfig.DEBUG) {
            OkHttpClient
                .Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build()
        } else {
            OkHttpClient
                .Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build()
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoteApiService::class.java)
    }
}