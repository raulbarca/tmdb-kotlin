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

data class MovieDetails(val id: Int,
                        val title: String,
                        val poster_path: String,
                        val overview: String,
                        val status: String,
                        val tagline: String,
                        val runtime: Int,
                        val homepage: String?) {

    companion object {
        fun empty() = MovieDetails(0, String.empty(), RemoteImageUtils.IMAGE_BASE_URL, String.empty(),
                String.empty(), String.empty(), 0, String.empty())
    }
}


