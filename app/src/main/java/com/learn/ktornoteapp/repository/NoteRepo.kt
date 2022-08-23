package com.learn.ktornoteapp.repository

import com.learn.ktornoteapp.data.remote.remote_model.User
import com.learn.ktornoteapp.utils.Result

interface NoteRepo {
    suspend fun createUser(user: User): Result<String>
    suspend fun loginUser(user: User): Result<String>
    suspend fun getUser(): Result<User>
    suspend fun logoutUser(): Result<String>
}