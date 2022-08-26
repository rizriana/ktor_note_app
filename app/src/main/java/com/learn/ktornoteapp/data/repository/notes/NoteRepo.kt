package com.learn.ktornoteapp.data.repository.notes

import com.learn.ktornoteapp.data.local.model.LocalNote
import com.learn.ktornoteapp.utils.Result
import kotlinx.coroutines.flow.Flow

interface NoteRepo {
    suspend fun createNote(note: LocalNote): Result<String>
    suspend fun updateNote(note: LocalNote): Result<String>
    fun getAllNote(): Flow<List<LocalNote>>
    suspend fun getAllNotesFromServer()
    suspend fun deleteNote(noteId: String)
    suspend fun syncNotes()
}