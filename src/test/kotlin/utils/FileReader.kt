package utils

import com.google.gson.Gson
import java.io.BufferedReader
import java.lang.reflect.ParameterizedType

class FileReader {

    private val gson by lazy { Gson() }

    fun <T : Any?> readJsonFileFromResources(fileName: String): T {
        val iStream = this::class.java.getResourceAsStream("$fileName.json")
            ?: throw IllegalStateException("Unable to read the file requested")
        val data = iStream.bufferedReader().use(BufferedReader::readText)
        val genericType = ((javaClass.genericSuperclass as? ParameterizedType)
            ?.actualTypeArguments?.getOrNull(0) as? Class<T>)
            ?: throw IllegalStateException("Can not find class from generic argument")

        return gson.fromJson(data, genericType)
    }
}