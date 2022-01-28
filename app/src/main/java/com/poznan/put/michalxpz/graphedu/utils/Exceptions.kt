package com.poznan.put.michalxpz.graphedu.utils

import java.lang.RuntimeException

class NullArgumentException(argumentName: String): RuntimeException("Argument $argumentName is null.")

class CannotSaveToFileException() : Exception() {
    override fun toString(): String {
        return "cannot save to file exception"
    }
}