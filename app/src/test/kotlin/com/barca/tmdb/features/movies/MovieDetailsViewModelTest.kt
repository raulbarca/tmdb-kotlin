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

import com.barca.tmdb.AndroidTest
import com.barca.tmdb.core.functional.Either.Right
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class MovieDetailsViewModelTest : AndroidTest() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    @Mock private lateinit var getMovieDetails: GetMovieDetails

    @Before
    fun setUp() {
        movieDetailsViewModel = MovieDetailsViewModel(getMovieDetails)
    }

    @Test fun `loading movie details should update live data`() {
        val movieDetails = MovieDetails(0, "IronMan", "poster_path", "overview",
                "status", "tagline", 120, "homepage")
        given { runBlocking { getMovieDetails.run(eq(any())) } }.willReturn(Right(movieDetails))

        movieDetailsViewModel.movieDetails.observeForever {
            with(it!!) {
                id shouldEqualTo 0
                title shouldEqualTo "IronMan"
                poster shouldEqualTo "poster_path"
                overview shouldEqualTo "overview"
                status shouldEqualTo "status"
                tagline shouldEqualTo "tagline"
                runtime shouldEqualTo 120
                homepage shouldEqualTo "homepage"
            }
        }

        runBlocking { movieDetailsViewModel.loadMovieDetails(0) }
    }
}