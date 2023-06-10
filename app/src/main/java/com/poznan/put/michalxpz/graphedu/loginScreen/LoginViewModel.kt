package com.poznan.put.michalxpz.graphedu.loginScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    var state by mutableStateOf(LoginScreenState(status = Status.NONE))
        private set
    fun onLoginSuccess(user: FirebaseUser?) {
        if (user != null) {
            state = state.copy(status = Status.LOGGEDIN)
        }
    }

    fun onLoginError(exception: Exception?) {
        state = state.copy(status = Status.ERROR)
    }
}
