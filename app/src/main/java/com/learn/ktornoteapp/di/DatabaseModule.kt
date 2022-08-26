package com.learn.ktornoteapp.di

import android.content.Context
import androidx.room.Room
import com.learn.ktornoteapp.data.local.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
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
}