package com.laynepenney.stardroid

sealed class Result<out T : Any> {
    abstract val success: Boolean

    class Success<out T : Any>
    internal constructor(
        val value: T
    ) : Result<T>() {
        override
        val success = true
    }

    class Failure
    internal constructor(
        val error: Throwable
    ) : Result<Nothing>() {
        override
        val success = false
    }

    companion object {
        @JvmStatic
        fun <T : Any> success(
            value: T
        ): Result<T> = Success(value)

        @JvmStatic
        fun <T : Any> failure(
            error: Throwable
        ): Result<T> = Failure(error)
    }
}