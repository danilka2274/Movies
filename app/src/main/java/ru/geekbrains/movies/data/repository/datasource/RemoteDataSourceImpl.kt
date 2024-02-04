package ru.geekbrains.movies.data.repository.datasource

import ru.geekbrains.movies.data.api.MovieApi
import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.models.ActorsResponse
import ru.geekbrains.movies.domain.models.MovieResponse
import ru.geekbrains.movies.domain.models.MoviesResponse

class RemoteDataSourceImpl(private val movieApi: MovieApi) : RemoteDataSource {
    override suspend fun getMoviesTopRated(adult: Boolean, page: Int): AppState<MoviesResponse> =
        try {
            val result = movieApi.getMoviesTopRatedAsync(adult, page).await()

            if (result.movies.isNotEmpty()) {
                AppState.Success(result)
            } else {
                AppState.Error(Exception(NO_DATA))
            }
        } catch (err: Exception) {
            AppState.Error(err)
        }

    override suspend fun getMovieDetailById(movieId: Int): AppState<MovieResponse> =
        try {
            val result = movieApi.getMovieDetailByIdAsync(movieId).await()
            AppState.Success(result)
        } catch (err: Exception) {
            AppState.Error(err)
        }

    override suspend fun getActorsList(movieId: Int): AppState<ActorsResponse> =
        try {
            val result = movieApi.getActorsListAsync(movieId).await()

            if (result.cast.isNotEmpty()) {
                AppState.Success(result)
            } else {
                AppState.Error(Exception(NO_DATA))
            }
        } catch (err: Exception) {
            AppState.Error(err)
        }

    override suspend fun searchMovie(query: String): AppState<MoviesResponse> =
        try {
            val result = movieApi.searchMovieAsync(query).await()

            if (result.movies.isNotEmpty()) {
                AppState.Success(result)
            } else {
                AppState.Error(Exception(NO_DATA))
            }
        } catch (err: Exception) {
            AppState.Error(err)
        }

    companion object {
        const val NO_DATA = "Отсутствуют данные"
    }
}