package com.learn.ktornoteapp.ui.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.ktornoteapp.data.local.local_model.LocalNote
import com.learn.ktornoteapp.repository.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
) : ViewModel() {
    var oldNote: LocalNote? = null
    val notes = noteRepo.getAllNote()

    fun createNote(
        noteTitle: String?,
        description: String?,
    ) = viewModelScope.launch(Dispatchers.IO) {
        val localNote = LocalNote(
            noteTitle = noteTitle,
            description = description
        )
        noteRepo.createNote(localNote)
    }

    fun updateNote(
        noteTitle: String?,
        description: String?,
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (noteTitle == oldNote?.noteTitle && description == oldNote?.description && oldNote?.connected == true) {
            return@launch
        }

        val note = LocalNote(
            noteTitle = noteTitle,
            description = description,
            noteId = oldNote!!.noteId
        )
        noteRepo.updateNote(note)
    }

    fun deleteNote(
        noteId: String,
    ) = viewModelScope.launch {
        noteRepo.deleteNote(noteId)
    }

    fun undoDelete(
        note: LocalNote,
    ) = viewModelScope.launch {
        noteRepo.createNote(note)
    }
}