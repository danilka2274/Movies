package ru.geekbrains.movies.di

import android.os.Build
import androidx.annotation.RequiresApi
import ru.geekbrains.movies.BuildConfig
import ru.geekbrains.movies.data.api.ApiInterceptor
import ru.geekbrains.movies.data.api.MovieApi
import ru.geekbrains.movies.data.repository.MovieRepositoryImpl
import ru.geekbrains.movies.data.repository.datasource.RemoteDataSource
import ru.geekbrains.movies.data.repository.datasource.RemoteDataSourceImpl
import ru.geekbrains.movies.domain.repository.MovieRepository
import ru.geekbrains.movies.domain.usecases.GetActorsUseCase
import ru.geekbrains.movies.domain.usecases.GetMovieDetailByIdUseCase
import ru.geekbrains.movies.domain.usecases.GetMoviesTopRatedUseCase
import ru.geekbrains.movies.domain.usecases.SearchMoviesUseCase
import ru.geekbrains.movies.ui.detail.DetailViewModel
import ru.geekbrains.movies.ui.movies.MoviesViewModel
import ru.geekbrains.movies.ui.settings.SettingsViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration


object Di {
    fun viewModelModule() = module {

        viewModel() {
            MoviesViewModel(
                getMoviesTopRatedUseCase = get(),
                searchMoviesUseCase = get()
            )
        }

        viewModel() {
            DetailViewModel(
                getMovieDetailByIdUseCase = get(),
                getActorsUseCase = get()
            )
        }

        viewModel() {
            SettingsViewModel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun apiModule() = module {
        single<Interceptor> {
            ApiInterceptor()
        }

        single<Gson> {
            GsonBuilder()
                .create()
        }

        single<MovieApi> {
            Retrofit.Builder()
                .baseUrl(BuildConfig.MOVIE_BASE_URL)
                .client(
                    OkHttpClient.Builder()
                        .callTimeout(Duration.ofSeconds(15))
                        .connectTimeout(Duration.ofSeconds(15))
                        .readTimeout(Duration.ofSeconds(15))
                        .writeTimeout(Duration.ofSeconds(15))
                        .addInterceptor(interceptor = get())
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                            if (BuildConfig.DEBUG) {
                                HttpLoggingInterceptor.Level.BODY
                            } else {
                                HttpLoggingInterceptor.Level.NONE
                            }
                        })
                        .build()
                )
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(get()))
                .build()
                .create(MovieApi::class.java)
        }
    }

    fun repositoryModule() = module {
        single<MovieRepository>() {
            MovieRepositoryImpl(
                dataSource = get()
            )
        }

        single<RemoteDataSource> {
            RemoteDataSourceImpl(movieApi = get())
        }
    }

    fun useCasesModule() = module {
        factory<GetMoviesTopRatedUseCase> {
            GetMoviesTopRatedUseCase(repository = get())
        }

        factory<GetMovieDetailByIdUseCase> {
            GetMovieDetailByIdUseCase(repository = get())
        }

        factory<GetActorsUseCase> {
            GetActorsUseCase(repository = get())
        }

        factory<SearchMoviesUseCase> {
            SearchMoviesUseCase(repository = get())
        }
    }
}