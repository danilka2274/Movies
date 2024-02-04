package ru.geekbrains.movies.ui.detail

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.models.ActorsResponse
import ru.geekbrains.movies.domain.models.MovieResponse
import ru.geekbrains.movies.domain.usecases.GetActorsUseCase
import ru.geekbrains.movies.domain.usecases.GetMovieDetailByIdUseCase
import ru.geekbrains.movies.ui.base.BaseViewModel

class DetailViewModel(
    private val movieLiveData: MutableLiveData<AppState<MovieResponse>> = MutableLiveData<AppState<MovieResponse>>(),
    private val actorLiveData: MutableLiveData<AppState<ActorsResponse>> = MutableLiveData<AppState<ActorsResponse>>(),
    private val getMovieDetailByIdUseCase: GetMovieDetailByIdUseCase,
    private val getActorsUseCase: GetActorsUseCase,
) : BaseViewModel() {

    override fun handleError(throwable: Throwable) {}

    fun getMovieLiveData() = movieLiveData
    fun getActorLiveData() = actorLiveData

    fun getMovieDetail(movieId: Int): Job =
        viewModelScopeCoroutine.launch {
            getMovieLiveData().postValue(AppState.Loading)
            getMovieLiveData().postValue(getMovieDetailByIdUseCase.execute(movieId))
        }

    fun getActors(movieId: Int): Job =
        viewModelScopeCoroutine.launch {
            getActorLiveData().postValue(AppState.Loading)
            getActorLiveData().postValue(getActorsUseCase.execute(movieId))
        }
}