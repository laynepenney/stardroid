package com.laynepenney.androidfilmthings

import com.squareup.moshi.JsonClass

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
