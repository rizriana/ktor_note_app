package com.learn.ktornoteapp.data.remote.remote_model

data class RemoteNote(
    val noteTitle: String?,
    val description: String,
    val date: Long,
    val noteId: String,
)