package com.hanifan.subwave.core.network

import com.hanifan.subwave.common.Constant
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.collections.get

class ApiResponseAdapter: JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: Set<Annotation?>,
        moshi: Moshi,
    ): JsonAdapter<*>? {
        val rawType = Types.getRawType(type)
        if (rawType != ResponseDto::class.java) return null

        val payloadType = (type as ParameterizedType).actualTypeArguments[0]
        val adapter: JsonAdapter<ResponseDetailsDto<Any?>> =
            moshi.adapter(Types.newParameterizedType(ResponseDetailsDto::class.java, payloadType))

        return SubsonicAdapter(adapter, moshi)
    }
}

private class SubsonicAdapter<T> (
    private val adapter: JsonAdapter<ResponseDetailsDto<T>>,
    private val moshi: Moshi
): JsonAdapter<ResponseDto<T>>() {
    override fun fromJson(reader: JsonReader): ResponseDto<T>? {
        val json = reader.readJsonValue() as Map<*, *>
        val root = json[Constant.SUBSONIC_ROOT_KEY] as? Map<*, *> ?: return null

        val knownKeys = setOf(
            "status",
            "version",
            "type",
            "serverVersion",
            "error",
            "openSubsonic"
        )

        val payloadKey = root.keys.firstOrNull { it !in knownKeys }

        val mutable = root.toMutableMap()
        if (payloadKey != null) {
            mutable["data"] = mutable.remove(payloadKey)
        }

        val jsonStr = moshi.adapter(Map::class.java).toJson(mutable)
        val response = adapter.fromJson(jsonStr)
        return response?.let { ResponseDto(it) }
    }

    override fun toJson(
        p0: JsonWriter,
        p1: ResponseDto<T>?,
    ) {
        throw UnsupportedOperationException("im not gonna use it")
    }

}