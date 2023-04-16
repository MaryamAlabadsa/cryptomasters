package com.mas.cryptomasters.core.di

import com.mas.cryptomasters.BuildConfig
import com.mas.cryptomasters.core.api.*
import com.mas.cryptomasters.core.api.api2.ApiRepository2
import com.mas.cryptomasters.core.api.api2.ApiService2
import com.mas.cryptomasters.core.pref.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun baseUrlProvider() = "https://crypto.cardproapp.com/api/v1/"

    fun baseUrl() = "https://api.coingecko.com/api/v3/"

    @Provides
    @Singleton
    @FirstApi
    fun provideRetrofit(BASE_URL: String, preferenceHelper: PreferenceHelper): Retrofit {
        val okHttpClient: OkHttpClient =
            if (BuildConfig.DEBUG) {
                val interceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .callTimeout(10, TimeUnit.MINUTES)
                    .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                        if (preferenceHelper.isGustUser()) {
                            val request = chain.request()
                                .newBuilder()
                                .addHeader("Accept", "application/json")
                                .addHeader("Accept-Language", preferenceHelper.getLanguage())
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .build()
                            chain.proceed(request)
                        } else {
                            val request = chain.request()
                                .newBuilder()
                                .addHeader(
                                    "Authorization",
                                    "Bearer ${preferenceHelper.getUserProfile().apiToken}"
                                )
                                .addHeader("Accept", "application/json")
                                .addHeader("Accept-Language", preferenceHelper.getLanguage())
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .build()
                            chain.proceed(request)
                        }
                    })
                    .addInterceptor(interceptor)
                    .build()
            } else OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @SeconedApi
    fun provideRetrofitSeconed(preferenceHelper: PreferenceHelper): Retrofit {
        val okHttpClient: OkHttpClient =
            if (BuildConfig.DEBUG) {
                val interceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .callTimeout(10, TimeUnit.MINUTES)
                    .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                        if (preferenceHelper.isGustUser()) {
                            val request = chain.request()
                                .newBuilder()
                                .addHeader("Accept", "application/json")
                                .addHeader("Accept-Language", preferenceHelper.getLanguage())
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .build()
                            chain.proceed(request)
                        } else {
                            val request = chain.request()
                                .newBuilder()
                                .addHeader(
                                    "Authorization",
                                    "Bearer ${preferenceHelper.getUserProfile().apiToken}"
                                )
                                .addHeader("Accept", "application/json")
                                .addHeader("Accept-Language", preferenceHelper.getLanguage())
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .build()
                            chain.proceed(request)
                        }
                    })
                    .addInterceptor(interceptor)
                    .build()
            } else OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl(baseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    @Provides
    @Singleton

    fun providesApiService(@FirstApi retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providesApiHelper(apiHelper: ApiHelperImplement): ApiHelper = apiHelper

    @Provides
    @Singleton
    fun providesApiService2(@SeconedApi retrofit: Retrofit): ApiService2 =
        retrofit.create(ApiService2::class.java)

    //
//    @Provides
//    @Singleton
//    fun providesApiHelper2(apiHelper2: ApiHelperImplement): ApiHelperImplement = apiHelper2
    @Provides
    @Singleton
    fun providerRepository2(
        apiService: ApiService2
    ): ApiRepository2 {
        return ApiRepository2(apiService)
    }
}