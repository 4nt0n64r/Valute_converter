package com.a4nt0n64r.valuteconverter.repository

import com.a4nt0n64r.valuteconverter.BuildConfig
import com.a4nt0n64r.valuteconverter.models.ValCurs
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("XML_daily.asp")
    fun getValutes(): Call<ValCurs>

    companion object {
        operator fun invoke(): ApiService {
            val gson: Gson = GsonBuilder()
                .setLenient()
                .create()

            val interceptor = HttpLoggingInterceptor().also {
                if (BuildConfig.DEBUG) {
                    it.level = HttpLoggingInterceptor.Level.BODY
                } else {
                    it.level = HttpLoggingInterceptor.Level.NONE
                }
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.cbr.ru/scripts/")
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
                .create(ApiService::class.java)

            return retrofit
        }
    }

}
