package org.example.utils

import com.google.gson.Gson
import java.io.BufferedReader

class TestHelper {

    private val gson by lazy { Gson() }

    /**
     * Read json files from resources and turn into object of type <T>
     */
    fun <T : Any?> readJsonFileFromResources(fileName: String, clazz: Class<T>): T {
        val iStream = this::class.java.getResourceAsStream("/$fileName.json")
            ?: throw IllegalStateException("Unable to read the file requested")
        val data = iStream.bufferedReader().use(BufferedReader::readText)
        return gson.fromJson(data, clazz)
    }

    fun toJsonString(any: Any) = gson.toJson(any)
}