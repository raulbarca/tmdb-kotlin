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

class MoviesViewModelTest : AndroidTest() {

    private lateinit var upcomingMoviesViewModel: UpcomingMoviesViewModel

    @Mock private lateinit var getUpcomingMovies: GetUpcomingMovies

    @Before
    fun setUp() {
        upcomingMoviesViewModel = UpcomingMoviesViewModel(getUpcomingMovies)
    }

    @Test fun `loading movies should update live data`() {
        val moviesList = listOf(Movie(0, "IronMan", "poster"), Movie(1, "Batman", "poster"))
        given { runBlocking { getUpcomingMovies.run(eq(any())) } }.willReturn(Right(moviesList))

        upcomingMoviesViewModel.movies.observeForever {
            it!!.size shouldEqualTo 2
            it.elementAt(0).id shouldEqualTo 0
            it.elementAt(0).poster shouldEqualTo "IronMan"
            it.elementAt(1).id shouldEqualTo 1
            it.elementAt(1).poster shouldEqualTo "Batman"
        }

        runBlocking { upcomingMoviesViewModel.loadMovies() }
    }
}