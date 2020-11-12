package com.laynepenney.androidfilmthings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

const val FilmsFile = "films"

// TODO: when switching to coroutines, switch to datastore instead of preferences
//class CoroutineCache(
//    private val dataStore: DataStore<Preferences>
//) {
//    constructor(
//        context: Context
//    ) : this(
//        dataStore = context.createDataStore(FilmsFile)
//    )
//}

class Cache(
    private val prefs: SharedPreferences,
    private val moshi: Moshi
) {
    constructor(
        context: Context,
        api: Api
    ) : this(
        prefs = context.getSharedPreferences(FilmsFile, MODE_PRIVATE),
        moshi = api.moshi
    )


    // TODO: ensure not main thread
    @ExperimentalStdlibApi
    private fun load(): FilmsResponse {
        val ids = prefs.getStringSet("ids", emptySet())!!
        // TODO: handle errors better
        val films = ids.map { id ->
            val json = prefs.getString(id, null)
            json!!.fromJson<Film>(moshi)
        }
        return FilmsResponse(films.size, films)
    }

    // TODO: ensure not main thread
    @ExperimentalStdlibApi
    private fun save(
        response: FilmsResponse
    ): Boolean {
        val films = response.results.associate { film ->
            Pair(
                film.episode_id.toJson(moshi),
                film.toJson(moshi)
            )
        }
        val ids = films.keys
        return commit {
            putStringSet("ids", ids)
            ids.forEach { id -> putString(id, films[id]) }
        }
    }

    private inline fun commit(
        action: SharedPreferences.Editor.() -> Unit
    ): Boolean {
        val edit = prefs.edit()
        edit.action()
        return edit.commit()
    }
}

@ExperimentalStdlibApi
inline fun <reified T : Any> T.toJson(
    moshi: Moshi
): String = moshi.adapter<T>()
    .toJson(this)

@ExperimentalStdlibApi
inline fun <reified T : Any> String.fromJson(
    moshi: Moshi
): T = moshi.adapter<T>()
    .fromJson(this)!!
