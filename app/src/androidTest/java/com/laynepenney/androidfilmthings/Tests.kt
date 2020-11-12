package com.laynepenney.androidfilmthings

import android.app.Instrumentation
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.moshi.adapter
import okio.buffer
import okio.source

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalStdlibApi
@RunWith(AndroidJUnit4::class)
class CacheTest {
    val api = Api()
    val instrumentation: Instrumentation
        get() = InstrumentationRegistry.getInstrumentation()
    val testApp: Context
        get() = instrumentation.context
    val app: Context
        get() = instrumentation.targetContext

    @Test
    fun useAppContext() {
        // Context of the app under test.
        assertEquals("com.laynepenney.androidfilmthings", app.packageName)
    }

    @Test
    fun saveFilms() {
        val films = loadFilms()
        val cache = Cache(app, api)
        assert(cache.save(films)) { "cache didn't save" }
        assertFilmsResult(cache.load())
    }

    private fun loadFilms(): FilmsResponse {
        val adapter = api.moshi.adapter<FilmsResponse>()
        return adapter.fromJson(testApp.assets.open("films.json").source().buffer())!!
    }

    private fun assertFilmsResult(result: FilmsResponse) {
        assert(result.count == 6) { "count is off" }
        assert(result.results.size == 6) { "results size is off" }
    }
}