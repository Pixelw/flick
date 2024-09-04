package tech.pixelw.flick.core.json

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

val moshi: Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

val moshiSerializeNull: Moshi = Moshi.Builder()
    .add(SerializeNull.Companion.Factory)
    .addLast(KotlinJsonAdapterFactory())
    .build()