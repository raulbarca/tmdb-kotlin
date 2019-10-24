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

import android.arch.lifecycle.MutableLiveData
import com.barca.tmdb.core.platform.BaseViewModel
import javax.inject.Inject

class UpcomingMoviesViewModel
@Inject constructor(private val getUpcomingMovies: GetUpcomingMovies) : BaseViewModel() {

    var movies: MutableLiveData<Set<MovieView>> = MutableLiveData()

    fun loadMovies(page: Int = 1) = getUpcomingMovies(page) { it.either(::handleFailure, ::handleMovieList) }

    private fun handleMovieList(movies: List<Movie>) {
        val map = movies.map { MovieView(it.id, it.title, it.poster) }.toSet()
        this.movies.value = this.movies.value?.plus(map) ?: map
    }
}