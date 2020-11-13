package com.laynepenney.stardroid

import android.app.Application
import android.content.Context

// TODO: @OptIn(...)
@ExperimentalStdlibApi
class App : Application() {
    private lateinit var _repo: Repo

    val repo: Repo
        get() = _repo

    override
    fun onCreate() {
        super.onCreate()
        val api = Api()
        val cache = Cache(this, api)
        _repo = Repo(api, cache)
    }
}

@ExperimentalStdlibApi
val Context.app: App
    get() = when (this) {
        is App -> this
        else -> applicationContext as App
    }
