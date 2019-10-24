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

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.barca.tmdb.R
import com.barca.tmdb.core.exception.Failure
import com.barca.tmdb.core.exception.Failure.NetworkConnection
import com.barca.tmdb.core.exception.Failure.ServerError
import com.barca.tmdb.core.extension.*
import com.barca.tmdb.core.navigation.Navigator
import com.barca.tmdb.core.platform.BaseFragment
import com.barca.tmdb.features.movies.MovieFailure.ListNotAvailable
import kotlinx.android.synthetic.main.fragment_upcoming_movies.*
import javax.inject.Inject


class UpcomingMoviesFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var moviesAdapter: MoviesAdapter


    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private val lastVisibleItemPosition: Int
        get() =
            (movieList.getLayoutManager() as StaggeredGridLayoutManager).findLastVisibleItemPositions(intArrayOf()).last()

    private val layoutManager: StaggeredGridLayoutManager
        get() = movieList.getLayoutManager() as StaggeredGridLayoutManager
    private var loading = true

    private var page: Int = 1

    private lateinit var upcomingMoviesViewModel: UpcomingMoviesViewModel

    override fun layoutId() = R.layout.fragment_upcoming_movies

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        upcomingMoviesViewModel = viewModel(viewModelFactory) {
            observe(movies, ::renderMoviesList)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        loadMoviesList(page)
    }


    private fun initializeView() {
        movieList.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        movieList.adapter = moviesAdapter
        moviesAdapter.clickListener = { movie, navigationExtras ->
            navigator.showMovieDetails(activity!!, movie, navigationExtras)
        }


        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0)
                //for vertical scrolling
                {
                    val visibleItems = layoutManager.findLastVisibleItemPositions(null)
                    val lastItem = Math.max(visibleItems[0], visibleItems[1])
                    if (lastItem > moviesAdapter.getItemCount() - 5 && !loading && dy > 0) {
                        loading = true
                        loadMoviesList(++page)
                    }
                }
            }

        }
        movieList.addOnScrollListener(scrollListener)
    }

    private fun loadMoviesList(page: Int) {
        emptyView.invisible()
        movieList.visible()
        showProgress()
        upcomingMoviesViewModel.loadMovies(page)
    }

    private fun renderMoviesList(movies: Set<MovieView>?) {
        moviesAdapter.collection = movies.orEmpty().toList()
        hideProgress()
        loading = false
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is ServerError -> renderFailure(R.string.failure_server_error)
            is ListNotAvailable -> renderFailure(R.string.failure_movies_list_unavailable)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        movieList.invisible()
        emptyView.visible()
        hideProgress()
        notifyWithAction(message, R.string.action_refresh) { loadMoviesList(page) }
    }
}
