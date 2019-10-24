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
package com.barca.tmdb.features.movies

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface MoviesApi {
    companion object {
        private const val PARAM_MOVIE_ID = "movieId"
        private const val PARAM_LANGUAGE = "language"
        private const val PARAM_PAGE = "page"
        private const val UPCOMING_MOVIES = "movie/upcoming"
        private const val MOVIE_DETAILS = "movie/{$PARAM_MOVIE_ID}"
    }

    @GET(UPCOMING_MOVIES)
    fun upcomingMovies(@Query(PARAM_LANGUAGE) language: String = "en-US"
                       , @Query(PARAM_PAGE) page: Int = 1): Call<List<MovieEntity>>

    @GET(MOVIE_DETAILS)
    fun movieDetails(@Path(PARAM_MOVIE_ID) movieId: Int
                     , @Query(PARAM_LANGUAGE) language: String = "en-US"): Call<MovieDetailsEntity>
}
