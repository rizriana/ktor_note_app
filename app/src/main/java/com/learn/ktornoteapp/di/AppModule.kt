package com.learn.ktornoteapp.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.learn.ktornoteapp.data.local.NoteDatabase
import com.learn.ktornoteapp.data.local.dao.NoteDao
import com.learn.ktornoteapp.data.remote.NoteApiService
import com.learn.ktornoteapp.repository.NoteRepo
import com.learn.ktornoteapp.repository.NoteRepoImpl
import com.learn.ktornoteapp.utils.Constant.BASE_URL
import com.learn.ktornoteapp.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext context: Context,
    ) = SessionManager(context)

    @Singleton
    @Provides
    fun provideNoteDatabase(
        @ApplicationContext context: Context,
    ): NoteDatabase {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("note_app".toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note_db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @Singleton
    @Provides
    fun provideNoteDao(
        noteDb: NoteDatabase,
    ) = noteDb.getNoteDao()

    @Singleton
    @Provides
    fun provideNoteApi(): NoteApiService {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoteApiService::class.java)

    }

    @Singleton
    @Provides
    fun provideNoteRepo(
        noteApi: NoteApiService,
        noteDao: NoteDao,
        sessionManager: SessionManager,
    ): NoteRepo {
        return NoteRepoImpl(
            noteApi,
            noteDao,
            sessionManager
        )
    }
}