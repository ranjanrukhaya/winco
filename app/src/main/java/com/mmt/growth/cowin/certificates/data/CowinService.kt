package com.mmt.growth.cowin.certificates.data

import com.mmt.growth.cowin.certificates.model.CertificatesResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit
import io.reactivex.Observable

const val BASE_URL = "https://run.mocky.io/v3/";

interface CowinService {

    companion object {
        fun create(): CowinService {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor).build()


            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(CowinService::class.java)
        }
    }

    @GET("412a81ab-a9b5-453c-bdd4-5c84fd4099bd")
    fun getCertificates(): Observable<CertificatesResponse>

    @GET("a7dae7fa-00df-40c2-aae4-0d77ff651f00")
    suspend fun getCertificatesCoroutine(): CertificatesResponse

    @GET("9df1f9f0-7de9-41d1-953d-c6b6f9c289eb")
    suspend fun getEmptyCertificatesCoroutine(): CertificatesResponse

    @GET("f1dc29b9-33dd-4fee-bc5b-981942fff056")
    suspend fun getErrorCertificatesCoroutine(): CertificatesResponse
}