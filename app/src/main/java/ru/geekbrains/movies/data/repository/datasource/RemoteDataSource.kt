package ru.geekbrains.movies.data.repository.datasource

import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.models.ActorsResponse
import ru.geekbrains.movies.domain.models.MovieResponse
import ru.geekbrains.movies.domain.models.MoviesResponse


interface RemoteDataSource {
    suspend fun getMoviesTopRated(adult: Boolean, page: Int): AppState<MoviesResponse>
    suspend fun getMovieDetailById(movieId: Int): AppState<MovieResponse>
    suspend fun getActorsList(movieId: Int): AppState<ActorsResponse>
    suspend fun searchMovie(query: String): AppState<MoviesResponse>
}