package ru.geekbrains.movies.domain.usecases

import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.models.MovieResponse
import ru.geekbrains.movies.domain.repository.MovieRepository

class GetMovieDetailByIdUseCase(private val repository: MovieRepository) {
    suspend fun execute(movieId: Int): AppState<MovieResponse> =
        repository.getMovieDetailById(movieId)
}