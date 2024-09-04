package tech.pixelw.flick.core.json

import retrofit2.converter.moshi.MoshiConverterFactory

object MoshiConverterFactory {
    val DEFAULT: MoshiConverterFactory = MoshiConverterFactory.create(moshi)
}