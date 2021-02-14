package com.example.test.di

import com.example.test.api.ApiInterface
import com.example.test.ui.UserListViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val viewModels = module {
    viewModel { UserListViewModel(get()) }
}

val apiModule = module {
    single {

        val tokenInterceptor = Interceptor { chain ->
            val request =
                chain
                    .request()
                    .newBuilder()
                    .addHeader(
                        "Authorization",
                        "c3c2f4dd13817c990f678478843229a2e21f5649b236b37e75d308c035685600"
                    )
                    .build()

            chain.proceed(request)
        }

        val logInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.HEADERS
        }

        val okHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(logInterceptor)
                .build()

        val retrofit =
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://gorest.co.in/public-api/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

        retrofit.create(ApiInterface::class.java)
    }
}