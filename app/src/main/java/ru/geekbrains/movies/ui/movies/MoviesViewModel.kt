package ru.geekbrains.movies.ui.movies

import androidx.lifecycle.MutableLiveData
import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.models.MoviesResponse
import ru.geekbrains.movies.domain.usecases.GetMoviesTopRatedUseCase
import ru.geekbrains.movies.domain.usecases.SearchMoviesUseCase
import ru.geekbrains.movies.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val moviesLiveData: MutableLiveData<AppState<MoviesResponse>> = MutableLiveData<AppState<MoviesResponse>>(),
    private val getMoviesTopRatedUseCase: GetMoviesTopRatedUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
) : BaseViewModel() {

    private val allMovies: ArrayList<MoviesResponse.Movie> = arrayListOf()

    private var currentPage: Int = ONE_VALUE

    fun setCurrentPage(value: Int, totalPage: Int) {
        if (value < totalPage) {
            currentPage = value.plus(ONE_VALUE)
        }
    }

    override fun handleError(throwable: Throwable) {}

    fun getMoviesLiveData() = moviesLiveData

    fun getMoviesTopRated(adult: Boolean = false, page: Int = currentPage): Job =
        viewModelScopeCoroutine.launch {
            getMoviesLiveData().postValue(AppState.Loading)
            val movies = getMoviesTopRatedUseCase.execute(adult, page)
            if (movies is AppState.Success) {
                when (val moviesList = movies.data) {
                    is MoviesResponse -> {
                        allMovies.addAll(moviesList.movies)
                        getMoviesLiveData().postValue(
                            AppState.Success<MoviesResponse>(moviesList.copy(movies = allMovies))
                        )
                    }
                }
            }
        }

    fun searchMovies(query: String): Job =
        viewModelScopeCoroutine.launch {
            coroutineContext.cancelChildren()
            allMovies.clear()
            setCurrentPage(ONE_VALUE, ONE_VALUE)
            getMoviesLiveData().postValue(AppState.Loading)
            val movies = searchMoviesUseCase.execute(query)
            getMoviesLiveData().postValue(movies)
        }

    companion object {
        private const val ONE_VALUE = 1
    }
}