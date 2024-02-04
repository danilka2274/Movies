package ru.geekbrains.movies.domain.usecases

import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.models.MoviesResponse
import ru.geekbrains.movies.domain.repository.MovieRepository

class GetMoviesTopRatedUseCase(private val repository: MovieRepository) {
    suspend fun execute(adult: Boolean = false, page: Int): AppState<MoviesResponse> =
        repository.getMoviesTopRated(adult, page)
}