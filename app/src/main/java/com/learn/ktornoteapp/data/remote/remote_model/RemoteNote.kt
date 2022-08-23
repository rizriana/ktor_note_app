package com.learn.ktornoteapp.data.remote.remote_model

data class RemoteNote(
    val id: String,
    val noteTitle: String?,
    val description: String?,
    val date: Long,
)