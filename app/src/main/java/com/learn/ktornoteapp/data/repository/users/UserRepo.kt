package com.learn.ktornoteapp.data.repository.users

import com.learn.ktornoteapp.data.remote.model.User
import com.learn.ktornoteapp.utils.Result

interface UserRepo {
    suspend fun createUser(user: User): Result<String>
    suspend fun loginUser(user: User): Result<String>
    suspend fun getUser(): Result<User>
    suspend fun logoutUser(): Result<String>
}