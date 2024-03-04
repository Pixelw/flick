package tech.pixelw.flick.core.network

import retrofit2.converter.moshi.MoshiConverterFactory

object MoshiConverterFactory {
    val DEFAULT: MoshiConverterFactory = MoshiConverterFactory.create(moshi)
}