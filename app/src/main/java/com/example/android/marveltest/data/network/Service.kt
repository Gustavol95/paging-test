package com.example.android.marveltest.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit


interface MarvelService {
    @GET("characters")
    fun getCharacters(@Query("limit") limit : Int, @Query("offset") offset : Int): Single<ApiResponse<Character>>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object AuthInterceptor : Interceptor{

    const val PUBLIC_KEY = "9a07ca068591aef98c16152c562294c3"
    const val PRIVATE_KEY = "addaca165f6d79e9508a006204b5ecc1a543e73a"

    override fun intercept(chain: Interceptor.Chain): Response {

        val ts = 1
        val hash = toMD5(ts.toString() + PRIVATE_KEY + PUBLIC_KEY)

        val modifiedUrl = chain.request().url.newBuilder().apply {
            addQueryParameter("apikey", PUBLIC_KEY)
            addQueryParameter("ts",ts.toString())
            addQueryParameter("hash", hash)
        }.build()

        val newRequest = chain.request().newBuilder().apply {
            addHeader("Content-Type", "application/json")
            addHeader("Accept", "application/json")
            url(modifiedUrl)
        }.build()

        return chain.proceed(newRequest)
    }
}

object Network {
    //Instantiate Logging interceptor
    private val logging =  HttpLoggingInterceptor().apply {
        level =  HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor)
        .addInterceptor(logging)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()


    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://gateway.marvel.com/v1/public/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val marvelService: MarvelService = retrofit.create(
        MarvelService::class.java)
}

fun toMD5(s : String): String? {
    try { // Create MD5 Hash
        val digest = MessageDigest.getInstance("MD5")
        digest.update(s.toByteArray())
        val messageDigest = digest.digest()
        // Create Hex String
        val hexString = StringBuffer()
        for (i in messageDigest.indices) hexString.append(
            Integer.toHexString(
                0xFF and messageDigest[i].toInt()
            )
        )
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}