package com.poznan.put.michalxpz.graphedu.loginScreen

data class LoginScreenState(
    val status: Status
)

enum class Status {
    NONE, LOGGEDIN, ERROR
}
