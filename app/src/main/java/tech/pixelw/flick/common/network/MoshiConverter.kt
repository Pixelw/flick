package tech.pixelw.flick.common.network

import retrofit2.converter.moshi.MoshiConverterFactory

object MoshiConverter {
    val DEFAULT = MoshiConverterFactory.create()
}