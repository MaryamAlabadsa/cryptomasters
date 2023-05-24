package com.mas
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.nio.charset.Charset

class LenientGsonConverter(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return Converter<ResponseBody, Any> { value ->
            val reader = InputStreamReader(value.byteStream())
            try {
                val jsonReader = gson.newJsonReader(reader)
                jsonReader.isLenient = true
                adapter.read(jsonReader)
            } finally {
                value.close()
            }
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return Converter<Any, RequestBody> { value ->
            val buffer = Buffer()
            val writer = OutputStreamWriter(buffer.outputStream(), Charset.forName("UTF-8"))
            val jsonWriter = gson.newJsonWriter(writer)
            jsonWriter.isLenient = true
            adapter.write(jsonWriter, value as Nothing?)
            jsonWriter.close()
            RequestBody.create(MEDIA_TYPE, buffer.readByteString())
        }
    }

    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()
    }
}
