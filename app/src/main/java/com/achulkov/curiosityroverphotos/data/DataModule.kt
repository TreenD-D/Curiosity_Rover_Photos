package com.achulkov.curiosityroverphotos.data

import android.content.Context
import androidx.room.Room
import com.achulkov.curiosityroverphotos.BuildConfig
import com.achulkov.curiosityroverphotos.data.db.RoverDatabase
import com.achulkov.curiosityroverphotos.data.remote.NasaApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun DataRepo(impl: RPhotosDataRepo): DataRepo


    companion object {

        @Provides
        @Singleton
        fun moshi(): Moshi {
            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()


            return moshi.newBuilder()
                .build()
        }

        @Provides
        @Singleton
        fun httpClient(executor: ExecutorService, cacheDir : File): OkHttpClient {
            val builder = OkHttpClient.Builder()
            builder.dispatcher(Dispatcher(executor))
            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(interceptor)
            }
            builder.cache(
                Cache(
                    File(cacheDir, "http_cache"),
                    200L * 1024L * 1024L // 200 MiB
                )
            )
            builder.connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
            return builder.build()
        }

        @Provides
        @Singleton
        fun retrofit(httpClient: OkHttpClient, moshi: Moshi): Retrofit {
            val builder: Retrofit.Builder = Retrofit.Builder()
            builder.baseUrl(BuildConfig.API_ENDPOINT)
            builder.client(httpClient.newBuilder()
                .addInterceptor { chain ->
                    val request: Request = chain.request()
                    return@addInterceptor chain.proceed(
                        request.newBuilder()
                            .build()
                    )
                }
                .build())
            builder.addConverterFactory(MoshiConverterFactory.create(moshi))
            builder.addCallAdapterFactory(RxJava3CallAdapterFactory.create())

            return builder.build()
        }

        @Provides
        fun nasaApi(retrofit: Retrofit): NasaApi {
            return retrofit.create(NasaApi::class.java)
        }

        @Provides
        @Singleton
        fun roverDatabase(context: Context): RoverDatabase {
            return Room.databaseBuilder(context, RoverDatabase::class.java, "rover.db")
                .fallbackToDestructiveMigration()
                .build()
        }

    }
}