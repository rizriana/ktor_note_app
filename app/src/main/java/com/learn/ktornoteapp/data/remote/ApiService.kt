package com.learn.ktornoteapp.data.remote

import com.learn.ktornoteapp.data.remote.model.RemoteNote
import com.learn.ktornoteapp.data.remote.model.SimpleResponse
import com.learn.ktornoteapp.data.remote.model.User
import com.learn.ktornoteapp.utils.Constant.NOTES_ENDPOINT
import com.learn.ktornoteapp.utils.Constant.USERS_ENDPOINT
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("$USERS_ENDPOINT/register")
    suspend fun registerAccount(
        @Body user: User,
    ): SimpleResponse

    @Headers("Content-Type: application/json")
    @POST("$USERS_ENDPOINT/login")
    suspend fun loginAccount(
        @Body user: User,
    ): SimpleResponse

    @Headers("Content-Type: application/json")
    @GET(NOTES_ENDPOINT)
    suspend fun getAllNote(
        @Header("Authorization") token: String,
    ): List<RemoteNote>

    @Headers("Content-Type: application/json")
    @POST("$NOTES_ENDPOINT/create")
    suspend fun createNote(
        @Header("Authorization") token: String,
        @Body note: RemoteNote,
    ): SimpleResponse

    @Headers("Content-Type: application/json")
    @POST("$NOTES_ENDPOINT/update")
    suspend fun updateNote(
        @Header("Authorization") token: String,
        @Body note: RemoteNote,
    ): SimpleResponse

    @Headers("Content-Type: application/json")
    @DELETE("$NOTES_ENDPOINT/delete")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Query("id") noteId: String,
    ): SimpleResponse
}