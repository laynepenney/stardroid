package com.laynepenney.stardroid

import com.squareup.moshi.adapter
import okio.buffer
import okio.source
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.lang.NullPointerException

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
        val result = adapter.fromJson(filmsFile.source().buffer())!!
        assertFilmsResult(result)
    }

    @Test
    fun testFilmsCall() {
        val response: Response<FilmsResponse> = api.swapi.getFilms().execute()
        assert(response.isSuccessful) { "not successful: ${response.errorBody()}" }
        val result: FilmsResponse = response.body()
            ?: throw NullPointerException("response is null")
        assertFilmsResult(result)
    }

    private fun assertFilmsResult(result: FilmsResponse) {
        assert(result.count == 6) { "count is off" }
        assert(result.results.size == 6) { "results size is off" }
    }
}