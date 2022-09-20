package org.soma.everyonepick.common.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    companion object {
        private const val BASE_URL = "http://ec2-3-38-142-213.ap-northeast-2.compute.amazonaws.com:8080/"
        // private const val BASE_URL = "http://10.0.2.2:8080/"

        fun <T> create(service: Class<T>): T {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(service)
        }

        fun String.toBearerToken() = "Bearer $this"
    }
}