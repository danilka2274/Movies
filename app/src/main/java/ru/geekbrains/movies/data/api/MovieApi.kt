package ru.geekbrains.movies.data.api

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.geekbrains.movies.BuildConfig
import ru.geekbrains.movies.domain.models.ActorsResponse
import ru.geekbrains.movies.domain.models.MovieResponse
import ru.geekbrains.movies.domain.models.MoviesResponse


interface MovieApi {

    @GET("movie/top_rated?api_key=${BuildConfig.MOVIE_API_KEY}&language=en-US")
    fun getMoviesTopRatedAsync(
        @Query("include_adult") adult: Boolean,
        @Query("page") page: Int
    ): Deferred<MoviesResponse>

    @GET("movie/{movie_id}?api_key=${BuildConfig.MOVIE_API_KEY}&language=en-US")
    fun getMovieDetailByIdAsync(@Path("movie_id") movieId: Int): Deferred<MovieResponse>

    @GET("movie/{movie_id}/casts?api_key=${BuildConfig.MOVIE_API_KEY}&language=en-US")
    fun getActorsListAsync(@Path("movie_id") movieId: Int): Deferred<ActorsResponse>

    @GET("search/movie?api_key=${BuildConfig.MOVIE_API_KEY}&language=en-US")
    fun searchMovieAsync(@Query("query") query: String): Deferred<MoviesResponse>
}