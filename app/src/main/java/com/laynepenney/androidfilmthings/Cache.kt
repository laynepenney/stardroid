package com.laynepenney.androidfilmthings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

const val FilmsFile = "films"
const val Ids = "ids"

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

@ExperimentalStdlibApi
class Cache(
    private val prefs: SharedPreferences,
    private val moshi: Moshi
) {
    private val EmptyList: String = emptyList<String>()
        .toJson(moshi)

    constructor(
        context: Context,
        api: Api
    ) : this(
        prefs = context.applicationContext.getSharedPreferences(FilmsFile, MODE_PRIVATE),
        moshi = api.moshi
    )


    // TODO: ensure not main thread
    fun load(): FilmsResponse {
        val ids: List<String> = prefs.getString(Ids, EmptyList)!!
            .fromJson(moshi)
        // TODO: handle errors better
        val films = ids.map { id ->
            val json = prefs.getString(id, null)!!
            json.fromJson<Film>(moshi)
        }
        return FilmsResponse(films.size, films)
    }

    // TODO: ensure not main thread
    fun save(
        response: FilmsResponse
    ): Boolean {
        val films = response.results
        val ids: List<String> = films.map { film -> film.episode_id.toString() }
        return commit {
            putString(Ids, ids.toJson(moshi))
            films.forEach { film ->
                val key = film.episode_id.toJson(moshi)
                putString(key, film.toJson(moshi))
            }
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
