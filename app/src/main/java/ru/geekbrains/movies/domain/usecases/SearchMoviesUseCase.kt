package ru.geekbrains.movies.domain.usecases

import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.models.MoviesResponse
import ru.geekbrains.movies.domain.repository.MovieRepository

class SearchMoviesUseCase(private val repository: MovieRepository) {
    suspend fun execute(query: String): AppState<MoviesResponse> =
        repository.searchMovie(query)
}