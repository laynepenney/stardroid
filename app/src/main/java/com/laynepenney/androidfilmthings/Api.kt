package com.laynepenney.androidfilmthings

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@JsonClass(generateAdapter = true)
data class FilmsResponse(
    val count: Int,
    val results: List<Film>
)

@JsonClass(generateAdapter = true)
data class Film(
    val title: String,
    val episode_id: Int,
    val opening_crawl: String,
    val director: String,
    val producer: String,
    val release_date: String
)

// TODO: dependency injection
class Api(
    val moshi: Moshi
) {
    constructor() : this(
        moshi = Moshi.Builder()
            // TODO: any custom factories
            .addLast(KotlinJsonAdapterFactory())
            .build()
    )
}
