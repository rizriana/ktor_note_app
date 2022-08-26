package com.learn.ktornoteapp.ui.users.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.ktornoteapp.data.remote.model.User
import com.learn.ktornoteapp.data.repository.users.UserRepo
import com.learn.ktornoteapp.utils.Constant.MAXIMUM_PASSWORD_LENGTH
import com.learn.ktornoteapp.utils.Constant.MINIMUM_PASSWORD_LENGTH
import com.learn.ktornoteapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo: UserRepo,
) : ViewModel() {
    private val _registerState = MutableSharedFlow<Result<String>>()
    val registerState: SharedFlow<Result<String>> = _registerState

    private val _loginState = MutableSharedFlow<Result<String>>()
    val loginState: SharedFlow<Result<String>> = _loginState

    private val _currentUserState = MutableSharedFlow<Result<User>>()
    val currentUserState: SharedFlow<Result<User>> = _currentUserState

    fun registerUser(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ) = viewModelScope.launch {
        _registerState.emit(Result.Loading())

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || password != confirmPassword) {
            _registerState.emit(Result.Error(""))
            return@launch
        }

        if (!isEmailValid(email)) {
            _registerState.emit(Result.Error(""))
            return@launch
        }

        if (!isPasswordValid(password)) {
            _registerState.emit(Result.Error(""))
            return@launch
        }

        val newUser = User(
            name,
            email,
            password
        )
        _registerState.emit(userRepo.createUser(newUser))
    }

    fun loginUser(
        name: String,
        email: String,
        password: String,
    ) = viewModelScope.launch {
        _loginState.emit(Result.Loading())

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _loginState.emit(Result.Error("as"))
            return@launch
        }

        if (!isEmailValid(email)) {
            _loginState.emit(Result.Error(""))
            return@launch
        }

        if (!isPasswordValid(password)) {
            _loginState.emit(Result.Error("Password should be between $MINIMUM_PASSWORD_LENGTH and $MAXIMUM_PASSWORD_LENGTH"))
            return@launch
        }

        val newUser = User(
            name,
            email,
            password
        )
        _loginState.emit(userRepo.loginUser(newUser))
    }

    fun getCurrentUser() = viewModelScope.launch {
        _currentUserState.emit(Result.Loading())
        _currentUserState.emit(userRepo.getUser())
    }

    fun logout() = viewModelScope.launch {
        val result = userRepo.logoutUser()
        if (result is Result.Success) {
            getCurrentUser()
        }
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return (password.length in MINIMUM_PASSWORD_LENGTH..MAXIMUM_PASSWORD_LENGTH)
    }
}