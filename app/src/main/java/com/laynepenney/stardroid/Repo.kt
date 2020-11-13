package com.laynepenney.stardroid

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger

// Singleton class
class Repo(
    private val api: Api,
    private val cache: Cache
) {
    val films = LiveResult(api.swapi.getFilms())
}

private const val CALL_INITIAL = 0
private const val CALL_INPROGRESS = 1
private const val CALL_FINISHED = 2

// TODO: include cache
// TODO: if call is unsuccessful, reset progress
class LiveResult<T : Any>(
    private val call: Call<T>
) : LiveData<Result<T>>() {
    private val progress = AtomicInteger(CALL_INITIAL)

    override
    fun onActive() {
        if (progress.compareAndSet(CALL_INITIAL, CALL_INPROGRESS)) {
            call.enqueue(callback)
        }
    }

    private val callback = object : Callback<T> {
        override
        fun onResponse(call: Call<T>, response: Response<T>) {
            post(response.result())
        }

        override
        fun onFailure(call: Call<T>, t: Throwable) {
            post(t.result())
        }

        @MainThread
        private fun post(result: Result<T>) {
            if (progress.compareAndSet(CALL_INPROGRESS, CALL_FINISHED)) {
                value = result
            }
        }
    }
}

fun <T : Any> Throwable.result(): Result<T> =
    Result.failure(this)

fun <T : Any> T?.result(): Result<T> = when (this) {
    null -> NullPointerException("object is null").result()
    else -> Result.success(this)
}

fun <T : Any> Response<T>.result(): Result<T> = if (isSuccessful) {
    body().result()
} else {
    HttpException(this).result()
}

