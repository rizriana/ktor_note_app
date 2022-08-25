package com.learn.ktornoteapp.data.remote

import com.learn.ktornoteapp.data.remote.remote_model.RemoteNote
import com.learn.ktornoteapp.data.remote.remote_model.SimpleResponse
import com.learn.ktornoteapp.data.remote.remote_model.User
import com.learn.ktornoteapp.utils.Constant.API_VERSION
import com.learn.ktornoteapp.utils.Constant.NOTES_ENDPOINT
import com.learn.ktornoteapp.utils.Constant.USERS_ENDPOINT
import retrofit2.http.*

interface NoteApiService {
    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/$USERS_ENDPOINT/register")
    suspend fun registerAccount(
        @Body user: User,
    ): SimpleResponse

    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/$USERS_ENDPOINT/login")
    suspend fun loginAccount(
        @Body user: User,
    ): SimpleResponse

    @Headers("Content-Type: application/json")
    @GET("$API_VERSION/$NOTES_ENDPOINT")
    suspend fun getAllNote(
        @Header("Authorization") token: String,
    ): List<RemoteNote>

    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/$NOTES_ENDPOINT/create")
    suspend fun createNote(
        @Header("Authorization") token: String,
        @Body note: RemoteNote,
    ): SimpleResponse

    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/$NOTES_ENDPOINT/update")
    suspend fun updateNote(
        @Header("Authorization") token: String,
        @Body note: RemoteNote,
    ): SimpleResponse

    @Headers("Content-Type: application/json")
    @DELETE("$API_VERSION/$NOTES_ENDPOINT/delete")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Query("id") noteId: String,
    ): SimpleResponse
}