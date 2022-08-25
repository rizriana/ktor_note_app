package com.learn.ktornoteapp.repository

import com.learn.ktornoteapp.data.local.local_model.LocalNote
import com.learn.ktornoteapp.data.remote.remote_model.User
import com.learn.ktornoteapp.utils.Result
import kotlinx.coroutines.flow.Flow

interface NoteRepo {
    suspend fun createUser(user: User): Result<String>
    suspend fun loginUser(user: User): Result<String>
    suspend fun getUser(): Result<User>
    suspend fun logoutUser(): Result<String>

    suspend fun createNote(note: LocalNote): Result<String>
    suspend fun updateNote(note: LocalNote): Result<String>
    fun getAllNote(): Flow<List<LocalNote>>
    suspend fun getAllNotesFromServer()

    suspend fun deleteNote(noteId: String)
    suspend fun syncNotes()
}