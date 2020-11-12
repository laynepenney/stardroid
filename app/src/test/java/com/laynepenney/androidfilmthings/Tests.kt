package com.laynepenney.androidfilmthings

import com.squareup.moshi.adapter
import okio.buffer
import okio.source
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException

class MoshiTest {
    lateinit var api: Api

    private val filmsFile: File
        get() = File("data/films.json")


    @Before
    fun setup() {
        api = Api()
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
        val adapter = api.moshi.adapter<FilmsResponse>()
        println(adapter.toString())
        val response = adapter.fromJson(filmsFile.source().buffer())!!
        assert(response.count == 6) { "count is off" }
        assert(response.results.size == 6) { "results size is off" }
    }
}