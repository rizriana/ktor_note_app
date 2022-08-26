package com.learn.ktornoteapp.data.repository.notes

import com.learn.ktornoteapp.data.local.dao.NoteDao
import com.learn.ktornoteapp.data.local.model.LocalNote
import com.learn.ktornoteapp.data.remote.ApiService
import com.learn.ktornoteapp.data.remote.model.RemoteNote
import com.learn.ktornoteapp.utils.Result
import com.learn.ktornoteapp.utils.SessionManager
import com.learn.ktornoteapp.utils.isNetworkManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepoImpl @Inject constructor(
    private val noteApi: ApiService,
    private val noteDao: NoteDao,
    private val sessionManager: SessionManager,
) : NoteRepo {
    override suspend fun createNote(note: LocalNote): Result<String> {
        try {
            noteDao.insertNote(note)
            val token = sessionManager.getJwtToken()
                ?: return Result.Success("Note is Saved in Local Database!")
            if (!isNetworkManager(sessionManager.context)) {
                return Result.Error("No Internet Connection!")
            }

            val result = noteApi.createNote(
                "Bearer $token",
                RemoteNote(
                    id = note.noteId,
                    noteTitle = note.noteTitle,
                    description = note.description,
                    date = note.date
                )
            )

            return if (result.success) {
                noteDao.insertNote(note.also { it.connected = true })
                Result.Success("Note Saved Successfully!")
            } else {
                Result.Error(result.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e.message ?: "Some Problem Occurred!")
        }
    }

    override suspend fun updateNote(note: LocalNote): Result<String> {
        try {
            noteDao.insertNote(note)
            val token = sessionManager.getJwtToken()
                ?: return Result.Success("Note is Updated in Local Database!")
            if (!isNetworkManager(sessionManager.context)) {
                return Result.Error("No Internet Connection!")
            }

            val result = noteApi.updateNote(
                "Bearer $token",
                RemoteNote(
                    id = note.noteId,
                    noteTitle = note.noteTitle,
                    description = note.description,
                    date = note.date
                )
            )

            return if (result.success) {
                noteDao.insertNote(note.also { it.connected = true })
                Result.Success("Note Updated Successfully!")
            } else {
                Result.Error(result.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e.message ?: "Some Problem Occurred!")
        }
    }

    override fun getAllNote(): Flow<List<LocalNote>> = noteDao.getAllNotesOrderedByDate()

    override suspend fun getAllNotesFromServer() {
        try {
            val token = sessionManager.getJwtToken() ?: return
            if (!isNetworkManager(sessionManager.context)) {
                return
            }
            val result = noteApi.getAllNote("Bearer $token")
            result.forEach { remoteNote ->
                noteDao.insertNote(
                    LocalNote(
                        noteTitle = remoteNote.noteTitle,
                        description = remoteNote.description,
                        date = remoteNote.date,
                        connected = true,
                        noteId = remoteNote.id
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteNote(noteId: String) {
        try {
            noteDao.deleteNoteLocally(noteId)
            val token = sessionManager.getJwtToken() ?: kotlin.run {
                noteDao.deleteNote(noteId)
                return
            }
            if (!isNetworkManager(sessionManager.context)) {
                return
            }

            val response = noteApi.deleteNote(
                "Bearer $token",
                noteId
            )

            if (response.success) {
                noteDao.deleteNote(noteId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun syncNotes() {
        try {
            sessionManager.getJwtToken() ?: return
            if (!isNetworkManager(sessionManager.context)) {
                return
            }

            val locallyDeletedNotes = noteDao.getAllLocallyDeletedNotes()
            locallyDeletedNotes.forEach {
                deleteNote(it.noteId)
            }

            val notConnectedNotes = noteDao.getAllLocalNote()
            notConnectedNotes.forEach {
                createNote(it)
            }

            val notUpdateNotes = noteDao.getAllLocalNote()
            notUpdateNotes.forEach {
                updateNote(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}