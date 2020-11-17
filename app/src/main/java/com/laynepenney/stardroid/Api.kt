package com.laynepenney.stardroid

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET

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
    val moshi: Moshi,
    val retrofit: Retrofit
) {
    val swapi: Swapi = retrofit.create()

    constructor() : this(
        moshi = Moshi.Builder()
            // TODO: any custom factories
            .addLast(KotlinJsonAdapterFactory())
            .build()
    )

    constructor(
        moshi: Moshi
    ) : this(
        moshi = moshi,
        retrofit = Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.HEADERS
                    })
                    .build()
            )
            .baseUrl("https://swapi.dev/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    )
}

interface Swapi {
    @GET("films")
    fun getFilms(): Call<FilmsResponse>
}
