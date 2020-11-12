package com.laynepenney.androidfilmthings

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.BufferedSource
import okio.Okio
import okio.buffer
import okio.source
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.io.File
import java.io.FileNotFoundException

class MoshiTest {
    lateinit var moshi: Moshi

    private val filmsFile: File
        get() = File("data/films.json")


    @Before
    fun setup() {
        moshi = Moshi.Builder()
            // TODO: any custom factories
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Test
    fun testFilmsFileExists() = try {
        assert(filmsFile.exists())
    } catch (e: AssertionError) {
        throw FileNotFoundException("no file found at ${filmsFile.absolutePath}")
    }

    @ExperimentalStdlibApi
    @Test
    fun testFilmsParsing() {
        val adapter = moshi.adapter<FilmsResponse>()
        println(adapter.toString())
        val response = adapter.fromJson(filmsFile.source().buffer())!!
        assert(response.count == 6) { "count is off" }
        assert(response.results.size == 6) { "results size is off" }
    }
}