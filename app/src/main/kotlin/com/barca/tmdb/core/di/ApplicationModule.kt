/**
 * Copyright (C) 2019 Raul Barca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.barca.tmdb.core.di

import android.content.Context
import com.barca.tmdb.AndroidApplication
import com.barca.tmdb.BuildConfig
import com.barca.tmdb.features.movies.MoviesRepository
import com.google.gson.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton


@Module
class ApplicationModule(private val application: AndroidApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder()
                .registerTypeAdapter(List::class.java, ResultsDeserializer<Results>())
                .create()

        return Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
                .addInterceptor {
                    val request = it.request()
                    val newUrl = request.url().newBuilder()
                            .addQueryParameter("api_key", "YOUR_API_KEY")
                            .build()
                    it.proceed(
                            request.newBuilder()
                                    .url(newUrl)
                                    .build())
                }

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    internal inner class ResultsDeserializer<T> : JsonDeserializer<T> {
        @Throws(JsonParseException::class)
        override fun deserialize(je: JsonElement, type: Type, jdc: JsonDeserializationContext): T {
            val results = je.asJsonObject
            val array = results.asJsonObject.getAsJsonArray("results")

            return Gson().fromJson(array, type)
        }
    }

    internal class Results(val t: Any)

    @Provides
    @Singleton
    fun provideMoviesRepository(dataSource: MoviesRepository.Network): MoviesRepository = dataSource
}
