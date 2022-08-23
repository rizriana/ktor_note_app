package com.learn.ktornoteapp.repository

import android.content.res.Resources
import com.learn.ktornoteapp.R
import com.learn.ktornoteapp.data.local.dao.NoteDao
import com.learn.ktornoteapp.data.remote.NoteApiService
import com.learn.ktornoteapp.data.remote.remote_model.User
import com.learn.ktornoteapp.utils.Result
import com.learn.ktornoteapp.utils.SessionManager
import com.learn.ktornoteapp.utils.isNetworkManager
import javax.inject.Inject

class NoteRepoImpl @Inject constructor(
    val noteApi: NoteApiService,
    val noteDao: NoteDao,
    val sessionManager: SessionManager,
) : NoteRepo {
    override suspend fun createUser(user: User): Result<String> {
        return try {
            if (!isNetworkManager(sessionManager.context)) {
                Result.Error<String>(Resources.getSystem()
                    .getString(R.string.no_internet_connection))
            }
            val result = noteApi.registerAccount(user)
            if (result.success) {
                Result.Success("User Create Successfully!")
            } else {
                Result.Error<String>(result.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error<String>(e.message ?: Resources.getSystem()
                .getString(R.string.some_problem_occurred))
        }
    }

    override suspend fun loginUser(user: User): Result<String> {
        return try {
            if (!isNetworkManager(sessionManager.context)) {
                Result.Error<String>("No Internet Connection!")
            }
            val result = noteApi.loginAccount(user)
            if (result.success) {
                sessionManager.updateSession(result.message, user.name ?: "", user.email)
                Result.Success("Logged In Successfully!")
            } else {
                Result.Error<String>(result.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error<String>(e.message ?: Resources.getSystem()
                .getString(R.string.some_problem_occurred))
        }
    }

    override suspend fun getUser(): Result<User> {
        return try {
            val name = sessionManager.getCurrentUserName()
            val email = sessionManager.getCurrentUserEmail()
            if (name == null || email == null) {
                Result.Error<User>("User Not Logged In!")
            } else {
                Result.Success(User(name, email, ""))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: Resources.getSystem()
                .getString(R.string.some_problem_occurred))
        }
    }

    override suspend fun logoutUser(): Result<String> {
        return try {
            sessionManager.logoutUser()
            Result.Success<String>("Logged Out Successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: Resources.getSystem()
                .getString(R.string.some_problem_occurred))
        }
    }
}