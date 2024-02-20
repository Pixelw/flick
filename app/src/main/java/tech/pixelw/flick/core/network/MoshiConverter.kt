package tech.pixelw.flick.core.network

import retrofit2.converter.moshi.MoshiConverterFactory

object MoshiConverter {
    val DEFAULT: MoshiConverterFactory = MoshiConverterFactory.create()
}