package com.learn.ktornoteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learn.ktornoteapp.data.local.dao.NoteDao
import com.learn.ktornoteapp.data.local.model.LocalNote

@Database(
    entities = [LocalNote::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
}