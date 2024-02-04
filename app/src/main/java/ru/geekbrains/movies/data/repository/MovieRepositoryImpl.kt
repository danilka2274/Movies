package ru.geekbrains.movies.data.repository

import ru.geekbrains.movies.data.repository.datasource.RemoteDataSource
import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.models.ActorsResponse
import ru.geekbrains.movies.domain.models.MovieResponse
import ru.geekbrains.movies.domain.models.MoviesResponse
import ru.geekbrains.movies.domain.repository.MovieRepository

class MovieRepositoryImpl(private val dataSource: RemoteDataSource) : MovieRepository {
    override suspend fun getMoviesTopRated(adult: Boolean, page: Int): AppState<MoviesResponse> =
        dataSource.getMoviesTopRated(adult, page)

    override suspend fun getMovieDetailById(movieId: Int): AppState<MovieResponse> =
        dataSource.getMovieDetailById(movieId)

    override suspend fun getActorsList(movieId: Int): AppState<ActorsResponse> =
        dataSource.getActorsList(movieId)

    override suspend fun searchMovie(query: String): AppState<MoviesResponse> =
        dataSource.searchMovie(query)
}