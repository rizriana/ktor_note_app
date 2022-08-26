package com.learn.ktornoteapp.utils

object Constant {
    const val JWT_TOKEN_KEY = "JWT_TOKEN_KEY"
    const val NAME_KEY = "NAME_KEY"
    const val EMAIL_KEY = "EMAIL_KEY"

    const val BASE_URL = "https://notesdb-server.herokuapp.com"
    private const val API_VERSION = "/v1"
    const val USERS_ENDPOINT = "$API_VERSION/users"
    const val NOTES_ENDPOINT = "$API_VERSION/notes"

    const val MINIMUM_PASSWORD_LENGTH = 4
    const val MAXIMUM_PASSWORD_LENGTH = 8
}