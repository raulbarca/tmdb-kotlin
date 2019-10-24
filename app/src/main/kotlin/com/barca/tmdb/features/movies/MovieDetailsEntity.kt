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

import com.barca.tmdb.core.extension.empty
import com.barca.tmdb.core.utils.RemoteImageUtils

data class MovieDetailsEntity(private val id: Int,
                              private val title: String,
                              private val poster_path: String?,
                              private val overview: String,
                              private val status: String,
                              private val tagline: String,
                              private val runtime: Int,
                              private val homepage: String?,
                              private val backdrop_path: String?) {

    companion object {
        fun empty() = MovieDetailsEntity(0, String.empty(), String.empty(), String.empty(),
                String.empty(), String.empty(), 0, String.empty(), String.empty())
    }

    fun toMovieDetails() = MovieDetails(id, title, RemoteImageUtils.IMAGE_BASE_URL + (poster_path ?: backdrop_path), overview, status, tagline, runtime, homepage)
}
