package ru.geekbrains.movies.domain.usecases

import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.models.ActorsResponse
import ru.geekbrains.movies.domain.repository.MovieRepository

class GetActorsUseCase(private val repository: MovieRepository) {
    suspend fun execute(movieId: Int): AppState<ActorsResponse> =
        repository.getActorsList(movieId)
}